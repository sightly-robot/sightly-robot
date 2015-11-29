package de.unihannover.swp2015.robots2.controller.model;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent.UpdateType;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IFieldWriteable;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IStageWriteable;

public class StageModelController {

	private final IStageWriteable stage;

	public StageModelController(IStageWriteable stage) {
		this.stage = stage;
	}

	/**
	 * Handle an update of the walls, received via MQTT topic MAP_WALLS.
	 * 
	 * This method resizes the stage (if necessary), updates all fields and
	 * emits the applicable Events on the Stage.
	 * 
	 * @param message
	 *            The MQTT message payload
	 */
	public void mqttSetWalls(String message) {
		String[] parts = message.split(",");
		if (parts.length < 3)
			return;

		int width = Integer.parseInt(parts[0]);
		int height = Integer.parseInt(parts[1]);

		if (parts.length != width * height + 2)
			return;

		if (width != this.stage.getWidth() || height != this.stage.getHeight()) {
			this.stage.changeSize(width, height);
			this.stage.emitEvent(UpdateType.STAGE_SIZE);
		}

		for (int i = 2; i < parts.length; i++) {
			IFieldWriteable f = this.stage.getFieldWriteable((i - 2) % width,
					(i - 2) / width);
			for (Orientation o : Orientation.values()) {
				// Add walls to outer stage borders if not existent
				boolean stageBorder = false;
				switch(o) {
				case WEST:
					stageBorder = (i - 2) % width == 0;
					break;
				case EAST:
					stageBorder = (i - 2) % width == width - 1;
					break;
				case NORTH:
					stageBorder = (i - 2) / width == 0;
					break;
				case SOUTH:
					stageBorder = (i - 2) / width == height - 1;
					break;
				};
				
				f.setWall(o, parts[i].contains(o.toString()) || stageBorder);
			}
		}
		this.stage.emitEvent(UpdateType.STAGE_WALL);

	}

	/**
	 * Handle an update of the food for whole map, received via MQTT topic
	 * MAP_FOOD.
	 * 
	 * This method updates all fields and emits the applicable Events on the
	 * Stage.
	 * 
	 * @param message
	 *            The MQTT message payload
	 */
	public void mqttSetFood(String message) {
		String[] parts = message.split(",");
		if (parts.length < 3)
			return;

		int width = Integer.parseInt(parts[0]);
		int height = Integer.parseInt(parts[1]);

		if (parts.length != width * height + 2
				|| width != this.stage.getWidth()
				|| height != this.stage.getHeight())
			return;

		for (int i = 2; i < parts.length; i++) {
			IFieldWriteable f = this.stage.getFieldWriteable((i - 2) % width,
					(i - 2) / width);
			int food = Integer.parseInt(parts[i]);
			f.setFood(food);
			f.emitEvent(UpdateType.FIELD_FOOD);
		}

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

			if (x >= 0 && x < this.stage.getWidth() && y >= 0
					&& y < this.stage.getWidth()) {
				IFieldWriteable f = this.stage.getFieldWriteable(x, y);
				f.setFood(food);
				f.emitEvent(UpdateType.FIELD_FOOD);
			}
		}
	}

}
