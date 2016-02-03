package de.unihannover.swp2015.robots2.yaai;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.controller.externalInterfaces.IRemoteAi;
import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;

/**
 * An adapter to place between {@link YetAnotherAi} and its
 * {@link IYaaiCalculator} to add support for remote controlled ai. This adaptor
 * can be registered as {@link IRemoteAi} and will kick in when receiving a
 * 'remote enable message'.
 * 
 * When remote control is disabled, we will pass new 'current positions', 'next
 * fields' and 'new field computed' events transparently. When remote control is
 * switched on, we will still provide new current positions to the calculator
 * but suppress propagation of its 'new field computed' events. Instead we will
 * call this event handler of the YAAI when receiving a new orientation event
 * from remote and answer all calls of {@link #getNextField()} with the field
 * selected by user.
 * 
 * @author Michael Thies
 */
public class YaaiRemoteAdapter implements IComputedFieldHandler,
		IYaaiCalculator, IRemoteAi {

	private final IYaaiCalculator realCalculator;
	private IComputedFieldHandler handler;
	private final IRobotController controller;

	private IField currentField;
	private Orientation nextOrientation;
	private boolean remoteEnabled;

	private static final Logger LOGGER = LogManager
			.getLogger(YetAnotherAi.class.getName());

	/**
	 * Construct a new remote adapter using the given next calculator and robot
	 * main controller.
	 * 
	 * This remote adapter will register itself as {@link IRemoteAi} at the main
	 * controller and used its model for checking allowed movement directions.
	 * 
	 * @param realCalculator
	 *            The real calculator that will be used when not in remote
	 *            controlled mode.
	 * @param controller
	 *            The main controller of this robot (to register as remote AI
	 *            and use Stage model).
	 */
	public YaaiRemoteAdapter(IYaaiCalculator realCalculator,
			IRobotController controller) {
		this.realCalculator = realCalculator;
		this.realCalculator.setHandler(this);
		this.controller = controller;
		this.controller.registerRemoteAi(this);
	}

	@Override
	public void onEnableMessage(boolean enable) {
		LOGGER.info("Switching to {}", enable ? "remote control"
				: "autonomous driving");
		this.remoteEnabled = enable;
		this.handler.onNewFieldComputed();
	}

	@Override
	public void onOrientationMessage(Orientation orientation) {
		LOGGER.debug("Received new Orientation from remote control: {}",
				orientation == null ? "null" : orientation.name());
		this.nextOrientation = orientation;
		if (this.remoteEnabled)
			this.handler.onNewFieldComputed();
	}

	@Override
	public IField getNextField() {
		if (this.remoteEnabled)
			return this.calculateNextField();
		else
			return this.realCalculator.getNextField();
	}

	@Override
	public void setCurrentPosition(IField field, Orientation orientation) {
		if (field != this.currentField)
			this.nextOrientation = null;
		this.currentField = field;
		this.realCalculator.setCurrentPosition(field, orientation);
	}

	@Override
	public void setHandler(IComputedFieldHandler handler) {
		this.handler = handler;
	}

	@Override
	public void onNewFieldComputed() {
		if (!this.remoteEnabled && this.handler != null)
			this.handler.onNewFieldComputed();
	}

	/**
	 * Helper method to calculate next field based on users remote wish for next
	 * field.
	 * 
	 * @return The next Field we want to and are allowed (regarding walls) to
	 *         drive to.
	 */
	private IField calculateNextField() {
		if (this.nextOrientation == null)
			return this.currentField;

		// Stay on current field if wall prevents driving in targeted
		// orientation
		if (currentField.isWall(this.nextOrientation))
			return this.currentField;

		try {
			IStage stage = this.controller.getGame().getStage();

			switch (this.nextOrientation) {
			case NORTH:
				return stage.getField(this.currentField.getX(),
						this.currentField.getY() - 1);
			case EAST:
				return stage.getField(this.currentField.getX() + 1,
						this.currentField.getY());
			case SOUTH:
				return stage.getField(this.currentField.getX(),
						this.currentField.getY() + 1);
			case WEST:
				return stage.getField(this.currentField.getX() - 1,
						this.currentField.getY());
			default:
				return this.currentField;
			}
		} catch (IndexOutOfBoundsException e) {
			// Stay on current field if next field is out of Stage bounds.
			return this.currentField;
		}
	}
}
