package de.unihannover.swp2015.robots2.controller.model;

import java.util.ArrayList;
import java.util.List;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent.UpdateType;
import de.unihannover.swp2015.robots2.model.interfaces.IField.State;
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
	 * @return An array containing the new stage size [width,height] or NULL if
	 *         message was not valid.
	 */
	public int[] mqttSetWalls(String message) {
		String[] parts = message.split(",", -1);
		if (parts.length < 3)
			return null;

		int width = Integer.parseInt(parts[0]);
		int height = Integer.parseInt(parts[1]);

		if (parts.length != width * height + 2)
			return null;

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
				switch (o) {
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
				}
				;

				f.setWall(o, parts[i].contains(o.toString()) || stageBorder);
			}
		}
		this.stage.emitEvent(UpdateType.STAGE_WALL);
		return new int[]{width,height};
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
	 * @return An array containing the size of the updated field [width,height]
	 *         or NULL if message was not valid.
	 */
	public int[] mqttSetFood(String message) {
		String[] parts = message.split(",");
		if (parts.length < 3)
			return null;

		int width = Integer.parseInt(parts[0]);
		int height = Integer.parseInt(parts[1]);

		this.resizeStage(width - 1, height - 1);

		if (parts.length != width * height + 2
				|| width != this.stage.getWidth()
				|| height != this.stage.getHeight())
			return null;

		for (int i = 2; i < parts.length; i++) {
			IFieldWriteable f = this.stage.getFieldWriteable((i - 2) % width,
					(i - 2) / width);
			int food = Integer.parseInt(parts[i]);
			f.setFood(food);
			f.emitEvent(UpdateType.FIELD_FOOD);
		}
		return new int[] { width, height };
	}

	/**
	 * Handle an update of the growing rate for whole map, received via MQTT
	 * topic MAP_INIT_GROWINGRATE.
	 * 
	 * This method updates all fields.
	 * 
	 * @param message
	 *            The MQTT message payload
	 */
	public void mqttSetGrowingrate(String message) {
		String[] parts = message.split(",");
		if (parts.length < 3)
			return;

		int width = Integer.parseInt(parts[0]);
		int height = Integer.parseInt(parts[1]);

		this.resizeStage(width - 1, height - 1);

		if (parts.length != width * height + 2
				|| width != this.stage.getWidth()
				|| height != this.stage.getHeight())
			return;

		for (int i = 2; i < parts.length; i++) {
			IFieldWriteable f = this.stage.getFieldWriteable((i - 2) % width,
					(i - 2) / width);
			int growingRate = Integer.parseInt(parts[i]);
			f.setGrowingRate(growingRate);
			f.emitEvent(UpdateType.GAME_PARAMETER);
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

			this.resizeStage(x, y);

			if (x >= 0 && x < this.stage.getWidth() && y >= 0
					&& y < this.stage.getHeight()) {
				IFieldWriteable f = this.stage.getFieldWriteable(x, y);
				f.setFood(food);
				f.emitEvent(UpdateType.FIELD_FOOD);
			}
		}
	}

	/**
	 * Handle new startpositions received via MQTT topic MAP_STARTPOSITIONS.
	 * 
	 * @param message
	 *            MQTT message payload containing the new start positions
	 */
	public void mqttSetStartpositions(String message) {
		String[] parts = message.split(",");
		if (parts.length % 3 != 0)
			return;

		this.stage.clearStartPositions();
		for (int i = 0; i < parts.length; i += 3) {
			int x = Integer.parseInt(parts[i]);
			int y = Integer.parseInt(parts[i + 1]);
			Orientation o = Orientation.getBy(parts[i + 2].charAt(0));

			if (x >= 0 && y >= 0 && o != null)
				this.stage.addStartPosition(x, y, o);
		}
		this.stage.emitEvent(UpdateType.STAGE_STARTPOSITIONS);
	}

	/**
	 * Returns all fields from the stage, which are hold by us.
	 * 
	 * @return a list of formated strings for the mqtt-message
	 */
	public List<String> getOurFields() {
		List<String> occupiedFields = new ArrayList<>();

		for (int y = 0; y < this.stage.getHeight(); y++) {
			for (int x = 0; x < this.stage.getWidth(); x++) {
				if (this.stage.getField(x, y).getState() == State.OURS) {
					occupiedFields.add(x + "-" + y);
				}
			}
		}

		return occupiedFields;
	}

	/**
	 * Resize the stage to fit the given coordinates.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 */
	private void resizeStage(int x, int y) {
		if (x >= this.stage.getWidth() || y >= this.stage.getHeight()) {
			this.stage.changeSize(Math.max(x + 1, this.stage.getWidth()),
					Math.max(y + 1, this.stage.getHeight()));
			this.stage.emitEvent(UpdateType.STAGE_SIZE);
		}
	}
}
