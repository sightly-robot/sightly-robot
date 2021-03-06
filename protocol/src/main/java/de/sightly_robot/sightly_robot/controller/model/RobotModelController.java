package de.sightly_robot.sightly_robot.controller.model;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.sightly_robot.sightly_robot.model.interfaces.IEvent.UpdateType;
import de.sightly_robot.sightly_robot.model.interfaces.IPosition.Orientation;
import de.sightly_robot.sightly_robot.model.interfaces.IRobot.RobotState;
import de.sightly_robot.sightly_robot.model.writeableInterfaces.IRobotWriteable;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class RobotModelController {

	private Map<String, IRobotWriteable> robots;

	private static final Logger LOGGER = LogManager
			.getLogger(RobotModelController.class.getName());

	public RobotModelController(Map<String, IRobotWriteable> robots) {
		this.robots = robots;
	}

	/**
	 * 
	 * @param key
	 *            ID of the robot extracted from MQTT topic
	 * @param message
	 *            MQTT payload
	 * @param setPosition
	 *            true if the MQTT topic was ROBOT_SETPOSITION
	 */
	public void mqttRobotPosition(String key, String message) {
		IRobotWriteable r = this.robots.get(key);

		if (r != null && !r.isMyself()) {
			try {
				String[] positionParts = message.split(",");

				r.setPosition(Integer.parseInt(positionParts[0]),
						Integer.parseInt(positionParts[1]),
						Orientation.getBy(positionParts[2]));
				r.setProgress(0);
				r.emitEvent(UpdateType.ROBOT_POSITION);
				r.emitEvent(UpdateType.ROBOT_PROGRESS);
			} catch (NumberFormatException e) {
				// Skip message if invalid number format
			}
		}
	}

	/**
	 * Handle a drive progress update, published by robot via the ROBOT_PROGRESS
	 * MQTT topic.
	 * 
	 * @param key
	 *            ID of the robot extracted from MQTT topic
	 * @param message
	 *            MQTT payload containing the new progress
	 */
	public void mqttRobotProgress(String key, String message) {
		IRobotWriteable r = this.robots.get(key);

		if (r != null && !r.isMyself()) {
			try {
				r.setProgress(Integer.parseInt(message));
				r.emitEvent(UpdateType.ROBOT_PROGRESS);
			} catch (NumberFormatException e) {
				// Skip message if invalid number format
			}
		}
	}

	/**
	 * Handle a score update that was received via MQTT topic ROBOT_SCORE.
	 * 
	 * @param key
	 *            Robot id extracted from MQTT topic
	 * @param message
	 *            MQTT message payload containing the new score
	 */
	public void mqttScoreUpdate(String key, String message) {
		IRobotWriteable r = this.robots.get(key);
		if (r == null)
			return;

		try {
			r.setScore(Integer.parseInt(message));
			r.emitEvent(UpdateType.ROBOT_SCORE);
		} catch (NumberFormatException e) {
			// Skip message if invalid number format
		}
	}

	/**
	 * Handle a robot state update/set that was received via MQTT topic
	 * ROBOT_STATE.
	 * 
	 * @param key
	 *            Robot id extracted from MQTT topic
	 * @param message
	 *            MQTT message payload containing the encoded robot state
	 */
	public void mqttRobotState(String key, String message) {
		RobotState state = RobotState.getBy(message);
		IRobotWriteable r = this.robots.get(key);

		// Break if invalid parameters received
		if (r == null || state == null)
			return;

		// The state of a robot can only be changed from outside the robot on a
		// manual disable from gui. All other status updates should be sent by
		// the robot itself and therefore ignored when returning.
		if (r.isMyself() && state != RobotState.MANUAL_DISABLED_GUI)
			return;

		LOGGER.debug("Robot {} changed state to {}.", r.getId(), state.name());
		r.setRobotState(state);
		r.emitEvent(UpdateType.ROBOT_STATE);
	}

	/**
	 * Handle a robot disconnect (or reconnect) messge received via MQTT topic
	 * EVENT_ROBOT_CONNECTION.
	 * 
	 * @param key
	 *            Robot id extracted from MQTT topic
	 * @param message
	 *            MQTT message payload to check if disconnect or reconnect of
	 *            robot.
	 */
	public void mqttRobotConnectionState(String key, String message) {
		IRobotWriteable r = this.robots.get(key);

		// Break if invalid parameters received
		if (r == null || r.isMyself())
			return;

		boolean connected = "".equals(message);
		LOGGER.debug("Robot {}'s connection state changed to {}.", r.getId(),
				connected ? "connected" : "disconnected");
		r.setRobotConnectionState(connected);
		r.emitEvent(UpdateType.ROBOT_STATE);
	}

	/**
	 * Handle a robot blink message.
	 * 
	 * This method emits a ROBOT_BLINK event from the robot.
	 * 
	 * @param key
	 *            Robot id extracted from MQTT topic
	 */
	public void mqttBlink(String key) {
		IRobotWriteable r = this.robots.get(key);

		// Break if invalid parameters received
		if (r != null)
			r.emitEvent(UpdateType.ROBOT_BLINK);
	}
}
