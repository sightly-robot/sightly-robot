package de.unihannover.swp2015.robots2.controller.externalInterfaces;

import de.unihannover.swp2015.robots2.controller.interfaces.IGuiController;

/**
 * Interface for a software component that is able to remote control hardware
 * robots.
 * 
 * @version 0.1
 * @author Michael Thies
 */
public interface IHardwareRobotControl {

	/**
	 * Receive settings that were requested from a specific hardware robot.
	 * 
	 * This method will be called when the current hardware settings of a robot
	 * arrive via MQTT after they were requested with
	 * {@link IGuiController#getRobotSettings(String)}.Or if another
	 * HardwareRobotControl updates the settings of a robot via
	 * {@link IGuiController#setRobotSettings(String, String)}.
	 * 
	 * @param settings
	 *            The String coding of the robot's current hardware settings
	 * @param robotId
	 *            The robot's id
	 */
	public void receiveSettings(String settings, String robotId);

}
