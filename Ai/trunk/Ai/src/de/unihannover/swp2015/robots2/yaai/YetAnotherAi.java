package de.unihannover.swp2015.robots2.yaai;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot.RobotState;
import de.unihannover.swp2015.robots2.robot.interfaces.AbstractAI;
import de.unihannover.swp2015.robots2.yaai.compute.CalculationWorker;

/**
 * Main component of the better Ai. This Class does all the initialization and
 * event handling.
 * 
 * @author Michael Thies
 */
public class YetAnotherAi extends AbstractAI implements IModelObserver {
	private enum AiState {
		STANDING, DRIVING, WATING_FOR_OURS, WAITING_FOR_FREE, WAITING_FOR_GAME,
	}

	private IField currentField, nextField;
	private AiState state;
	private CalculationWorker worker;

	private Logger log = LogManager.getLogger(this.getClass().getName());

	public YetAnotherAi(IRobotController iRobotController) {
		super(iRobotController);
		log.debug("Constructing Ai...");
		this.iRobotController.getMyself().observe(this);
		this.iRobotController.getGame().observe(this);

		this.worker = new CalculationWorker(iRobotController, this);

		Thread thread = new Thread(this.worker);
		thread.start();
	}

	@Override
	public void setRelativeSpeed(double forwards, double sidewards,
			double backwards) {
	}

	@Override
	public void onModelUpdate(IEvent event) {
		switch (event.getType()) {
		case FIELD_STATE:
			IField f = (IField) event.getObject();
			if (f == this.nextField)
				this.onFieldStateChange(f.getState());
			break;

		case ROBOT_STATE:
			this.onGameStateChange();
			break;

		case GAME_STATE:
			this.onGameStateChange();
			break;

		case ROBOT_POSITION:
			IRobot r = (IRobot) event.getObject();
			IField field = this.iRobotController.getGame().getStage()
					.getField(r.getPosition().getX(), r.getPosition().getY());
			this.onReachField(field);
			break;

		default:
			break;
		}

	}

	public void onNewFieldComputed() {
		if (this.worker.getNextField() != this.nextField)
			this.requestNewField(this.worker.getNextField());
	}

	/**
	 * Set a field as next field and try to lock it.
	 * 
	 * This method will only work if we are standing or waiting, game is running
	 * and the given field is FREE.
	 * 
	 * @param field
	 *            The field we want to go to next.
	 */
	private void requestNewField(IField field) {
		switch (this.state) {
		case STANDING:
		case WAITING_FOR_GAME:
		case WAITING_FOR_FREE:
			break;

		default:
			// Skip if already busy
			log.trace(
					"New target field {}-{} was refused because we are busy.",
					field.getX(), field.getY());
			return;
		}

		if (field == null)
			return;

		log.debug("We are targeting field {}-{} now...", field.getX(),
				field.getY());

		// Skip if invald field or we are already on this field
		if (field == this.currentField) {
			log.debug("but we are already standing there.");
			return;
		}

		int dx = this.currentField.getX() - field.getX();
		int dy = this.currentField.getY() - field.getY();
		if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
			log.debug(
					"but the field seems not neighboured to current field {}-{}.",
					this.currentField.getX(), this.currentField.getY());
			return;
		}

		if (this.nextField != null)
			this.nextField.unobserve(this);
		field.observe(this);
		this.nextField = field;

		// Wait if field not free
		if (field.getState() != IField.State.FREE) {
			this.state = AiState.WAITING_FOR_FREE;
			log.debug("but its {} by {}. So we will wait.", field.getState()
					.name(), field.getLockedBy());
			return;
		}
		// Wait if game not started
		if (!this.isReadyToDrive()) {
			this.state = AiState.WAITING_FOR_GAME;
			log.debug("but the current game/robot state doesn't allow us to drive.");
			return;
		}

		log.debug("so, request it!");
		this.iRobotController.requestField(this.nextField.getX(),
				this.nextField.getY());
		this.state = AiState.WATING_FOR_OURS;
	}

	/**
	 * Event handler to be called when the field we are waiting for changes it's
	 * state.
	 */
	private void onFieldStateChange(IField.State state) {
		log.debug("Next field targeted by Ai changed state to {}.",
				state.name());
		switch (state) {
		case OURS:
			// We got the field so let's drive there!
			this.driveToNextField();
			break;

		case LOCKED:
		case OCCUPIED:
		case RANDOM_WAIT:
			this.state = AiState.WAITING_FOR_FREE;
			break;

		case FREE:
			// Yay, the field got free. Request it!
			this.iRobotController.requestField(this.nextField.getX(),
					this.nextField.getY());
			this.state = AiState.WATING_FOR_OURS;

		default:
			break;
		}
	}

	/**
	 * Event handler to be called when game state or our robot state changes.
	 * 
	 * Checks if we already own a field or if one was calculated.
	 */
	private void onGameStateChange() {
		if (!this.isReadyToDrive())
			return;

		log.debug("Game or Robot changed state. Ai is running again.");

		// If we already own a field and try to drive there
		if (this.nextField != null
				&& this.nextField.getState() == IField.State.OURS)
			this.driveToNextField();
		// If we are sure about a new field but don't own it, try to get it
		else if (this.nextField != null)
			this.requestNewField(this.nextField);
		else
			this.requestNewField(this.worker.getNextField());

	}

	/**
	 * Tell our hardware to go the the next field.
	 * 
	 * This method will only work if game is running. Please only call this
	 * method after making shure, the nextField is OURS.
	 */
	private void driveToNextField() {
		log.debug("We are now driving to the next Field: {}-{}",
				this.nextField.getX(), this.nextField.getY());

		// this.nextField.unobserve(this);
		// TODO ... looking for workaround

		// Wait if game not started
		if (!this.isReadyToDrive()) {
			this.state = AiState.WAITING_FOR_GAME;
			log.debug("but we should wait until game and robot state allow us to.");
			return;
		}

		int dx = this.nextField.getX() - this.currentField.getX();
		int dy = this.nextField.getY() - this.currentField.getY();
		Orientation o = null;
		if (dx == 1) {
			o = Orientation.EAST;
		} else if (dx == -1) {
			o = Orientation.WEST;
		} else if (dy == 1) {
			o = Orientation.SOUTH;
		} else if (dy == -1) {
			o = Orientation.NORTH;
		} else {
			log.debug(
					"The field is not a neighbor of current field {}-{}. Aborting drive.",
					this.currentField.getX(), this.currentField.getY());
			return;
		}
		log.debug("Direction is {} from our current field {}-{}. And ... go!",
				o.name());

		this.fireNextOrientationEvent(o);
		this.state = AiState.DRIVING;
		this.worker.setCurrentPosition(this.nextField);
	}

	/**
	 * Event handler to be called when a field is reached after driving there.
	 */
	private void onReachField(IField field) {
		if (field == currentField)
			return;

		log.debug("We reached a new Field: {}-{}", field.getX(), field.getY());

		// Release current field (if any)
		if (this.currentField != null)
			this.iRobotController.releaseField(this.currentField.getX(),
					this.currentField.getY());
		this.currentField = field;
		this.nextField = null;
		this.state = AiState.STANDING;
		this.worker.setCurrentPosition(field);

		// If new field was calculated, drive there
		this.requestNewField(this.worker.getNextField());
	}

	/**
	 * Helper function to check if we are allowed to drive: The game must be
	 * running and we must be enabled by the user.
	 * 
	 * @return {@code true} if we are allowed to drive
	 */
	private boolean isReadyToDrive() {
		return this.iRobotController.getMyself().getState() == RobotState.ENABLED
				&& this.iRobotController.getGame().isRunning();
	}

}
