package de.unihannover.swp2015.robots2.controller.model;

import de.unihannover.swp2015.robots2.model.writeableInterfaces.IStageWriteable;

public class StageModelController {

	private final IStageWriteable stage;

	public StageModelController(IStageWriteable stage) {
		this.stage = stage;
	}
	
	/**
	 * Handle a MAP_WALLS message.
	 * 
	 * @param message
	 */
	public void mqttSetWalls(String message) {
		// TODO implement method
		// TODO emit Event (Stage)
	}
	
	/**
	 * Handle a MAP_FOOD message.
	 * 
	 * @param message
	 */
	public void mqttSetFood(String message) {
		// TODO implement method
		// TODO emit Event (Field)
	}
	
	/**
	 * Handle a FIELD_FOOD message.
	 * 
	 * @param message
	 */
	public void mqttSetFieldFood(int x, int y, int value) {
		// TODO implement method
		// TODO emit Event (Field)
	}

}
