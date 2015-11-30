package de.unihannover.swp2015.robots2.controller.model;

import de.unihannover.swp2015.robots2.model.implementation.Robot;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent.UpdateType;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IGameWriteable;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IRobotWriteable;

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
		if (message.equals("")) {
			IRobotWriteable r = this.game.getRobotsWriteable().get(robotId);
			if (r != null) {
				// TODO agree on this behaviour
				this.game.getRobotsWriteable().remove(robotId);
				this.game.emitEvent(UpdateType.ROBOT_DELETE, r);
			}

			// Else check if robot does exist and add if not
		} else if (!this.game.getRobots().containsKey(robotId)) {
			IRobotWriteable r = new Robot(robotId, message.equals("real"),
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
		this.game.setRunning(message.equals("running"));
		this.game.emitEvent(UpdateType.GAME_STATE);
	}

	/**
	 * Handle a ROBOT_VIRTUALSPEED message.
	 * 
	 * @param speed
	 */
	public void mqttSetRobotVirtualspeed(float speed) {
		this.game.setVRobotSpeed(speed);
		this.game.emitEvent(UpdateType.GAME_PARAMETER);
	}

	/**
	 * Update game parameter "Robot hesitation time" when published via MQTT
	 * topic CONTROL_HESITATIONTIME.
	 * 
	 * @param message
	 */
	public void mqttSetRobotHesitationTime(String message) {
		this.game.setHesitationTime(Integer.parseInt(message));
		this.game.emitEvent(UpdateType.GAME_PARAMETER);
	}
}
