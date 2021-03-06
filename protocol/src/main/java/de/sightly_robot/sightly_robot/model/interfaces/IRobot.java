package de.sightly_robot.sightly_robot.model.interfaces;

import java.awt.Color;

import de.sightly_robot.sightly_robot.model.interfaces.IPosition;

/**
 * Interface for all the read-only actions on a Robot object to be used by the
 * controllers.
 * 
 * A Robot objects represents any kind of Robot that takes part in the game.
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public interface IRobot extends IAbstractModel {

	/**
	 * Describes the state of a robot.
	 */
	public enum RobotState {
		/**
		 * Robot disabled. Cause: The robot is not connected to the MQTT broker
		 * (anymore).
		 */
		DISCONNECTED("d"),

		/**
		 * Robot disabled. Cause: The robot initially connected or reconnected
		 * after connection loss. It must be manually restarted by the user.
		 */
		CONNECTED("c"),

		/**
		 * Robot disabled. Cause: A user manually disabled the robot via it's
		 * button or interation with the robot's software.
		 */
		MANUAL_DISABLED_ROBOT("x"),

		/**
		 * Robot disabled. Cause: A user manually disabled the robot via a GUI.
		 */
		MANUAL_DISABLED_GUI("y"),

		/**
		 * Robot disabled. Cause: The robot encountered a problem concerning the
		 * robotics hardware.
		 */
		ROBOTICS_ERROR("r"),

		/**
		 * Robot is in setup state. The user assigned a position to the robot
		 * but the robot didn't confirmed it's ready yet.
		 */
		SETUPSTATE("s"),

		/** The robot is enabled and therefore ready-to-drive or driving. */
		ENABLED("");
		
		private final String mqttEncoding;

		private RobotState(String mqttEncoding) {
			this.mqttEncoding = mqttEncoding;
		}

		public static RobotState getBy(String mqttEncoding) {
			for (RobotState state : RobotState.values()) {
				if (state.mqttEncoding.equals(mqttEncoding))
					return state;
			}
			return null;
		}

		@Override
		public String toString() {
			return this.mqttEncoding;
		}
	}

	/**
	 * Returns the Robot-Identification-String
	 * 
	 * @return the Robot-Id
	 */
	public String getId();

	/**
	 * Returns the Robot-Name
	 * 
	 * @return the Robot-Name
	 */
	public String getName();

	/**
	 * Returns true if it's a hardware-robot and false, if the robot is a
	 * virtual one.
	 * 
	 * @return true: the robot exists physically; false: if not
	 */
	public boolean isHardwareRobot();

	/**
	 * Returns the current read-only position of the robot.
	 * 
	 * @return an IPosition object with the current position
	 */
	public IPosition getPosition();

	/**
	 * Returns the current score of the robot.
	 * 
	 * @return the current score
	 */
	public int getScore();

	/**
	 * Get the current State of the Robot. Indicating whether the Robot is
	 * disconnected, enabled, in setup state or disabled by any cause.
	 * 
	 * @return the Robot's current state.
	 */
	public RobotState getState();

	/**
	 * Returns true, if the instance of the class run on the robot who's
	 * described by the instance.
	 * 
	 * @return true: the instance described myself; false: if not
	 */
	public boolean isMyself();

	/**
	 * Returns the color of the robot.
	 * 
	 * @return A generated color.
	 */
	public Color getColor();

}
