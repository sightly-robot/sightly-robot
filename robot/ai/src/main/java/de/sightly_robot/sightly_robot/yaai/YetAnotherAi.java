package de.sightly_robot.sightly_robot.yaai;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.sightly_robot.sightly_robot.controller.interfaces.IRobotController;
import de.sightly_robot.sightly_robot.model.externalInterfaces.IModelObserver;
import de.sightly_robot.sightly_robot.model.interfaces.IEvent;
import de.sightly_robot.sightly_robot.model.interfaces.IField;
import de.sightly_robot.sightly_robot.model.interfaces.IRobot;
import de.sightly_robot.sightly_robot.model.interfaces.IPosition.Orientation;
import de.sightly_robot.sightly_robot.model.interfaces.IRobot.RobotState;
import de.sightly_robot.sightly_robot.robot.interfaces.AbstractAI;
import de.sightly_robot.sightly_robot.yaai.compute.CalculationWorker;

/**
 * Main component of YAAI - Yet Another Ai. This Class does all the
 * initialization and event handling.
 * 
 * @author Michael Thies
 */
public class YetAnotherAi extends AbstractAI implements IModelObserver,
		IComputedFieldHandler {
	private enum AiState {
		STANDING, DRIVING, WATING_FOR_OURS, WAITING_FOR_FREE, WAITING_FOR_GAME,
	}

	private IField currentField, nextField;
	private AiState state;
	private IYaaiCalculator calculator;

	private static final Logger LOGGER = LogManager
			.getLogger(YetAnotherAi.class.getName());

	/**
	 * Constructs new YAAI, working with the given controller and using it's
	 * model.
	 * 
	 * This constructor instantly starts a calculation thread periodically
	 * recalculating a path to drive.
	 * 
	 * @param controller
	 *            The main controller to be used by this AI
	 */
	public YetAnotherAi(IRobotController controller) {
		super(controller);
		LOGGER.debug("Constructing YAAI Ai...");
		this.iRobotController.getMyself().observe(this);
		this.iRobotController.getGame().observe(this);

		// Init calculation worker and remote adapter
		CalculationWorker worker = new CalculationWorker(controller);
		this.calculator = new YaaiRemoteAdapter(worker, controller);
		this.calculator.setHandler(this);

		// Start calculation worker
		Thread thread = new Thread(worker);
		thread.start();
	}

	@Override
	public void setRelativeSpeed(double forwards, double sidewards,
			double backwards) {
		// Not used by current path calculation implementation
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
			this.onReachField(field, r.getPosition().getOrientation());
			break;

		default:
			break;
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * If the new field is actually different from current nextField, try to
	 * initiate driving there.
	 */
	@Override
	public void onNewFieldComputed() {
		if (this.calculator.getNextField() != this.nextField) {
			this.requestNewField(this.calculator.getNextField());

			// If currently driving let robot know about next direction
			if (this.state == AiState.DRIVING) {
				try {
					Orientation o = calcOrientation(this.nextField,
							this.calculator.getNextField());
					this.fireNextButOneOrientationEvent(o);
				} catch (IllegalArgumentException e) {
					// Do nothing. No dramatical situation if robot can't
					// indicate direction
				}
			}
		}
	}

	/**
	 * Set a field as next field and try to request it.
	 * 
	 * This method will only work if we are standing or waiting, game is running
	 * and the given field is FREE. In other cases it will discard the new field
	 * or change to wait state.
	 * 
	 * @param field
	 *            The field we want to go to next.
	 */
	private void requestNewField(IField field) {
		// Skip if already busy
		if (this.state == AiState.DRIVING
				|| this.state == AiState.WATING_FOR_OURS) {
			LOGGER.trace(
					"New target field {}-{} was refused because we are busy.",
					field.getX(), field.getY());
			return;
		}

		if (field == null)
			return;

		LOGGER.debug("We are targeting field {}-{} now...", field.getX(),
				field.getY());

		// Skip if invald field or we are already on this field
		if (field == this.currentField) {
			LOGGER.debug("but we are already standing there.");
			return;
		}

		// Check if field is reachable from current position.
		int dx = this.currentField.getX() - field.getX();
		int dy = this.currentField.getY() - field.getY();
		if (Math.abs(dx) + Math.abs(dy) != 1) {
			LOGGER.debug(
					"but the field seems not neighboured to current field {}-{}.",
					this.currentField.getX(), this.currentField.getY());
			return;
		}

		// Observe correct field to be notified about changes
		if (field != this.nextField) {
			if (this.nextField != null)
				this.nextField.unobserve(this);
			field.observe(this);
			this.nextField = field;
		}
		// Wait if game not started
		if (!this.isReadyToDrive()) {
			this.state = AiState.WAITING_FOR_GAME;
			LOGGER.debug("but the current game/robot state doesn't allow us to drive.");
			return;
		}

		// If field is FREE: Request field
		switch (field.getState()) {
		case FREE:
			// Request field
			LOGGER.debug("so, request it!");
			this.iRobotController.requestField(this.nextField.getX(),
					this.nextField.getY());
			this.state = AiState.WATING_FOR_OURS;
			break;
		
		case OURS:
			// Should not happpen, but if the field is ours
			LOGGER.debug("The field seems to be already ours.");
			this.driveToNextField();
			break;
				
		default:
			// Wait for field getting free
			this.state = AiState.WAITING_FOR_FREE;
			LOGGER.debug("but its {} by {}. So we will wait.", field.getState()
					.name(), field.getLockedBy());
			break;
		}
	}

	/**
	 * Event handler to be called when the field we are waiting for changes it's
	 * state.
	 * 
	 * On change to FREE: Lock it. On change to any LOCKED state: Go to WAIT
	 * state. On change to OURS: drive to field.
	 */
	private void onFieldStateChange(IField.State state) {
		LOGGER.debug("Next field targeted by Ai changed state to {}.",
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
			// Yay, the field got free. Request it (again)!
			this.requestNewField(this.nextField);
			break;

		default:
			break;
		}
	}

	/**
	 * Event handler to be called when game state or our robot state changes.
	 * 
	 * If we are allowed to drive, check our current state:
	 * 
	 * In case we already own our targeted next field: drive there. In other
	 * cases try to lock our targeted field.
	 */
	private void onGameStateChange() {
		if (!this.isReadyToDrive())
			return;

		LOGGER.debug("Game or Robot changed state. Ai is doing things again.");

		// If we already own a field and try to drive there
		if (this.nextField != null
				&& this.nextField.getState() == IField.State.OURS)
			this.driveToNextField();
		// If we are sure about a new field but don't own it, try to get it
		else
			this.requestNewField(this.nextField);

	}

	/**
	 * Tell our robot to go the the next field.
	 * 
	 * This method will only work if game is running. Please only call this
	 * method after making sure, the nextField is OURS.
	 */
	private void driveToNextField() {
		LOGGER.debug("We are now driving to the next Field: {}-{}",
				this.nextField.getX(), this.nextField.getY());

		// Wait if game not started
		if (!this.isReadyToDrive()) {
			this.state = AiState.WAITING_FOR_GAME;
			this.iRobotController.releaseField(this.nextField.getX(),
					this.nextField.getY());
			LOGGER.debug("but we should wait until game and robot state allow us to.");
			return;
		}

		try {
			Orientation o = calcOrientation(this.currentField, this.nextField);
			LOGGER.debug(
					"Direction is {} from our current field {}-{}. And ... go!",
					this.currentField.getX(), this.currentField.getY(),
					o.name());

			// Fire orientation
			this.fireNextOrientationEvent(o);
			this.state = AiState.DRIVING;
			this.calculator.setCurrentPosition(this.nextField, o);
		} catch (IllegalArgumentException e) {
			LOGGER.debug(
					"The field is not a neighbor of current field {}-{}. Aborting drive.",
					this.currentField.getX(), this.currentField.getY());
		}
	}

	/**
	 * Event handler to be called when a field is reached after driving there.
	 * 
	 * This method releases the old field and initiates targeting the next
	 * calculated field.
	 */
	private void onReachField(IField field, Orientation orientation) {
		// Skip event if the field didn't actually change
		if (field == currentField)
			return;

		LOGGER.debug("We reached a new Field: {}-{}", field.getX(),
				field.getY());

		// Release current field (if any)
		if (this.currentField != null)
			this.iRobotController.releaseField(this.currentField.getX(),
					this.currentField.getY());
		this.currentField = field;
		if (this.nextField != null) {
			this.nextField.unobserve(this);
			this.nextField = null;
		}
		this.state = AiState.STANDING;
		this.calculator.setCurrentPosition(field, orientation);

		// If new field was calculated, drive there
		this.requestNewField(this.calculator.getNextField());
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

	/**
	 * Calculate the driving orientation to get from one field to next.
	 * 
	 * @param start
	 *            The field to start from
	 * @param destination
	 *            The field to go to
	 * @return The orientation of the destination field as seen from start field
	 * @throws IllegalArgumentException
	 *             if the two fields are not neighbouring
	 */
	private static Orientation calcOrientation(IField start, IField destination)
			throws IllegalArgumentException {
		int dx = destination.getX() - start.getX();
		int dy = destination.getY() - start.getY();
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
			throw new IllegalArgumentException("Fields are not neighbouring.");
		}
		return o;
	}
}
