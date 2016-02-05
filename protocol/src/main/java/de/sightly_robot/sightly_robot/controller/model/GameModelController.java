package de.sightly_robot.sightly_robot.controller.model;

import de.sightly_robot.sightly_robot.model.implementation.Robot;
import de.sightly_robot.sightly_robot.model.interfaces.IEvent.UpdateType;
import de.sightly_robot.sightly_robot.model.writeableInterfaces.IGameWriteable;
import de.sightly_robot.sightly_robot.model.writeableInterfaces.IRobotWriteable;

public class GameModelController {

	private IGameWriteable game;

	public GameModelController(IGameWriteable game) {
		this.game = game;
	}

	/**
	 * Handle a ROBOT_TYPE message.
	 * 
	 * This will add the robot if not existent or delete it (if message == "").
	 * 
	 * @param robotId
	 *            Robot Id extracted from MQTT topic
	 * @param message
	 *            MQTT message payload
	 */
	public void mqttAddRobot(String robotId, String message) {
		// Empty message means: delete robot
		if ("".equals(message)) {
			IRobotWriteable r = this.game.getRobotsWriteable().get(robotId);
			if (r != null) {
				this.game.getRobotsWriteable().remove(robotId);
				this.game.emitEvent(UpdateType.ROBOT_DELETE, r);
			}

			// Else check if robot does exist and add if not
		} else if (!this.game.getRobots().containsKey(robotId)) {
			IRobotWriteable r = new Robot(robotId, "real".equals(message),
					false);
			this.game.addRobot(r);
			this.game.emitEvent(UpdateType.ROBOT_ADD, r);
		}
	}

	/**
	 * Handle a CONTROL_STATE message.
	 * 
	 * @param message
	 */
	public void mqttSetGameState(String message) {
		this.game.setRunning("running".equals(message));
		this.game.emitEvent(UpdateType.GAME_STATE);
	}

	/**
	 * Handle a ROBOT_VIRTUALSPEED message.
	 * 
	 * @param speed
	 */
	public void mqttSetRobotVirtualspeed(String message) {
		String[] parts = message.split(",");
		
		try {
		switch (parts.length) {
			case 1:
				// legacy support for old protocol
				this.game.setVRobotSpeed(Float.parseFloat(parts[0]));			
				break;
			
			case 2:
				this.game.setVRobotSpeed(Float.parseFloat(parts[0]));
				this.game.setVRobotRotationSpeed(Float.parseFloat(parts[1]));
				break;
				
			default:
				// Invalid format. Skip message
				return;
			}
			this.game.emitEvent(UpdateType.GAME_PARAMETER);
		} catch (NumberFormatException e) {
			// Skip message if number format invalid
		}
	}
}
