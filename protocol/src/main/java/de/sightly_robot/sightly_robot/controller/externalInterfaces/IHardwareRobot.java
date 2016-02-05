package de.sightly_robot.sightly_robot.controller.externalInterfaces;

import java.awt.Color;

import de.sightly_robot.sightly_robot.controller.interfaces.IGuiController;

/**
 * Interface for a software component that is controlling a (hardware) robot. It
 * may get new hardware settings from a IHardwareRobotControl interface, be
 * asked for its current settings and be asked to flash the LED lights of the
 * robot.
 * 
 * @version 0.1
 * @author Michael Thies
 */
public interface IHardwareRobot {

	/**
	 * Update this robot's hardware settings.
	 * 
	 * @param settings
	 *            The encoded settings as sent by the control component.
	 */
	public void setSettings(String settings);

	/**
	 * Return the current hardware settings of this robot, encoded to be
	 * transmitted to the control component.
	 * 
	 * This function will be used after a call of
	 * {@link IGuiController#getRobotSettings(String)} and the returned string
	 * will be passed to
	 * {@link IHardwareRobotControl#receiveSettings(String, String)} of each
	 * connected IHardwareRobotControl.
	 * 
	 * @return Encoded hardware settings
	 */
	public String getSettings();

	/**
	 * Flash the hardware robot's LEDs performing a nice pattern in the given
	 * color.
	 * 
	 * @param color
	 *            Main color to use for the LED pattern.
	 */
	public void blink(Color color);

}
