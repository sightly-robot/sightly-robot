package de.unihannover.swp2015.robots2.controller.model;

import java.util.Map;

import de.unihannover.swp2015.robots2.model.interfaces.IRobot;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class RobotModelController implements IModelController {

	private Map<String, ? extends IRobot> robots;
	
	public RobotModelController(Map<String, ? extends IRobot> robots) {
		this.robots = robots;
	}

	@Override
	public void handleMqttMessage(String topic, String message) {
		// TODO Auto-generated method stub
		
	}
}
