package de.unihannover.swp2015.robots2.controller.externalInterfaces;

import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

/**
 * Defines an interface for a remote controllable AI and defines event handlers
 * to be called when receiving associated MQTT messages.
 * 
 * @author Michael Thies
 */
public interface IRemoteAi {

	/**
	 * Event handler to be called when a remote control enable/disable message
	 * is received.
	 * 
	 * The remote AI component should listen to remote orientation messages (as
	 * given by {@link #onOrientationMessage(Orientation)}) when set to
	 * {@code true} and start driving autonomously when set to {@code false}.
	 * 
	 * @param enable
	 *            {@code true} if user starts to control robot, {@code false} if
	 *            user returns control to AI
	 */
	public void onEnableMessage(boolean enable);

	/**
	 * Event handler to be called, when a remote orientation message for our
	 * robot is received.
	 * 
	 * The remote AI component should consider driving moving in this
	 * orientation if remote control is enabled.
	 * 
	 * @param orientation
	 *            The orientation to move in or {@code null} if the user wants
	 *            the robot to stop
	 */
	public void onOrientationMessage(Orientation orientation);

}
