package de.sightly_robot.sightly_robot.robot.interfaces;

import de.sightly_robot.sightly_robot.controller.interfaces.IRobotController;
import de.sightly_robot.sightly_robot.model.interfaces.IPosition.Orientation;

/**
 * The AbstractAi is meant to act like an interface between any AI
 * implementation and hardware as well as software robots.
 * 
 * @author Lenard Spiecker
 */
public abstract class AbstractAI {

	private AiEventObserver aiEventObserver;
	protected IRobotController iRobotController;

	/**
	 * Constructs a new AbstractAi.
	 * 
	 * @param iRobotController
	 *            the controller of the robot this AI belongs to
	 */
	public AbstractAI(IRobotController iRobotController) {
		this.iRobotController = iRobotController;
	}

	/**
	 * Sets calculated relative move durations.<br>
	 * The AI could use them to find the fastest way.
	 * 
	 * @param forwards
	 *            the relative move duration for moving forwards
	 * @param sidewards
	 *            the relative move duration for moving sidewards
	 * @param backwards
	 *            the relative move duration for moving backwards
	 */
	public abstract void setRelativeSpeed(double forwards, double sidewards, double backwards);

	/**
	 * Sets the AiEventObserver for observing the direction decisions.
	 * 
	 * @param aiEventObserver
	 *            the AiEventObserver to set
	 */
	public void setAiEventObserver(AiEventObserver aiEventObserver) {
		this.aiEventObserver = aiEventObserver;
	}

	/**
	 * Notifies the AiEventObserver about the calculated next direction.
	 * 
	 * @param orientation
	 *            the orientation of the next position
	 * @return {@code true} if there is an observer and it accepts the event,
	 *         {@code false} otherwise
	 */
	protected boolean fireNextOrientationEvent(Orientation orientation) {
		if (aiEventObserver != null) {
			return aiEventObserver.nextOrientationEvent(orientation);
		}
		return false;
	}

	/**
	 * Notifies the AiEventObserver about the calculated next but one direction.
	 * <br>
	 * The next but one direction should not be used for driving and for this
	 * reason it is not needed to check for acceptance.
	 * 
	 * @param orientation
	 *            the orientation of the next but one position.
	 */
	protected void fireNextButOneOrientationEvent(Orientation orientation) {
		if (aiEventObserver != null) {
			aiEventObserver.nextButOneOrientationEvent(orientation);
		}
	}

	public IRobotController getController() {
		return this.iRobotController;
	}
}
