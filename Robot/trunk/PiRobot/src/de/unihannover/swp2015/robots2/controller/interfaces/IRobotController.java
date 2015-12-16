package de.unihannover.swp2015.robots2.controller.interfaces;

import de.unihannover.swp2015.robots2.controller.externalInterfaces.IHardwareRobot;
import de.unihannover.swp2015.robots2.model.interfaces.*;

/**
 * This interface specifies methods of the main controller to be used by the
 * robots.
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IRobotController extends IController {

	/**
	 * Broadcasts the given position to the network and save it in the model.
	 * 
	 * @param x
	 *            The x-coordinate of the field.
	 * @param y
	 *            The y-coordinate of the field.
	 * @param orientation
	 *            The orientation of the robot.
	 */
	public void updatePosition(int x, int y, IPosition.Orientation orientation);

	/**
	 * Sends the new position-progress value to the network.
	 * 
	 * @param progress
	 *            An integer between 0 and 1000 to describe the current position
	 *            between two fields. 0 means, that we stay in the center of our
	 *            current field; 1000 means, that we stay in the center of the
	 *            next field. The visualization can draw the exact robot
	 *            position with this value.
	 */
	public void updatePositionProgress(int progress);

	/**
	 * Lock the free field on the given coordinates and send an information to
	 * the other robots. If the requested field is not free, do nothing.
	 * 
	 * @param x
	 *            The x-coordinate of the field.
	 * @param y
	 *            The y-coordinate of the field.
	 */
	public void requestField(int x, int y);

	/**
	 * If the field on the given coordinated is marked as ours, a release
	 * message will be boardcast.
	 * 
	 * @param x
	 *            The x-coordinate of the field.
	 * @param y
	 *            The y-coordinate of the field.
	 */
	public void releaseField(int x, int y);

	/**
	 * Set this robot to enabled state.
	 * 
	 * Should be called in SETUPSTATE. Hardware robot: After user confirms, that
	 * robot is placed correctly, by pressing button. Software robot: On
	 * entering setup state.
	 */
	public void setRobotReady();

	/**
	 * Disables this robot setting it do robotics error state and reports the
	 * error via MQTT.
	 */
	public void reportRoboticsError();

	/**
	 * Disable this robot when user asks to (e.g. hardware button press) and
	 * report the new state via MQTT.
	 */
	public void disableMyself();

	/**
	 * Remove this robot from the game. The robot will be erased completely from
	 * all parts of the game.
	 */
	public void deleteMyself();

	/**
	 * Returns an Robot instance of the robot on which this thread is running..
	 * 
	 * @return The Robot-instance.
	 */
	public IRobot getMyself();

	/**
	 * Register a hardware robot to receive hardware robot settings and blink
	 * messages.
	 * 
	 * @param hardwareRobot
	 *            The hardware robot.
	 */
	public void registerHardwareRobot(IHardwareRobot hardwareRobot);
}
