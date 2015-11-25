package de.unihannover.swp2015.robots2.controller.model;

import de.unihannover.swp2015.robots2.model.implementation.Robot;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent.UpdateType;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IGameWriteable;

public class GameModelController {

	private IGameWriteable game;

	public GameModelController(IGameWriteable game) {
		this.game = game;
	}

	/**
	 * Handle a ROBOT_TYPE message.
	 * 
	 * @param message
	 */
	public void mqttAddRobot(String robotId, String message) {
		if (this.game.getRobots().containsKey(robotId))
			this.game
					.addRobot(new Robot(robotId, message.equals("real"), false));
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
}
