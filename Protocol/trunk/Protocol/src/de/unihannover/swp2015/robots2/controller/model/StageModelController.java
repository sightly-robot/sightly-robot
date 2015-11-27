package de.unihannover.swp2015.robots2.controller.model;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent.UpdateType;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IFieldWriteable;
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
	 * Sets the current food state of a single field.
	 * 
	 * @param key
	 *            The field id extracted from MQTT topic
	 * @param message
	 *            The MQTT message payload containing the new food value
	 */
	public void mqttSetFieldFood(String key, String message) {
		String[] coordinates = key.split("-");
		if (coordinates.length == 2) {
			int x = Integer.parseInt(coordinates[0]);
			int y = Integer.parseInt(coordinates[1]);
			int food = Integer.parseInt(message);
			
			if (x >= 0 && x < this.stage.getWidth() && y >= 0 && y < this.stage.getWidth()) {
				IFieldWriteable f = this.stage.getFieldWriteable(x, y);
				f.setFood(food);
				f.emitEvent(UpdateType.FIELD_FOOD);
			}
		}
	}

}
