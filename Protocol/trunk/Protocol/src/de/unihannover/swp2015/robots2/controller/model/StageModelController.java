package de.unihannover.swp2015.robots2.controller.model;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent.UpdateType;
import de.unihannover.swp2015.robots2.model.interfaces.IField.State;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IFieldWriteable;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IStageWriteable;

public class StageModelController implements IModelController {

	private final IStageWriteable stage;

	public StageModelController(IStageWriteable stage) {
		this.stage = stage;
	}

	@Override
	public void handleMqttMessage(String topic, String message) {
		// TODO Auto-generated method stub

	}

	/**
	 * Handle an FIELD_OCCUPY_LOCK message.
	 * 
	 * Updates the state of the field (given by id in MQTT topic) dependent on
	 * its current state.
	 * 
	 * @param topic
	 *            MQTT Topic string of the message
	 * @param message
	 *            MQTT payload
	 */
	public void mqttFieldLock(String topic, String message) {
		int[] coordinates = parseFieldFromTopic(topic);

		IFieldWriteable f = this.stage.getFieldWriteable(coordinates[0],
				coordinates[1]);

		switch (f.getState()) {
		case FREE:
			f.setState(State.LOCKED);
			f.setLockedBy(message);
			f.emitEvent(UpdateType.FIELD_STATE);
			break;

		case LOCK_WAIT:
			// TODO check if lock is from us and start random timer if not
			break;

		case RANDOM_WAIT:
			f.getStateTimer().cancel();
			f.setState(State.LOCKED);
			f.setLockedBy(message);
			f.emitEvent(UpdateType.FIELD_STATE);
			break;

		default:
			break;
		}
	}

	/**
	 * Handle an FIELD_OCCUPY_SET message.
	 * 
	 * Updates the state of the field (given by id in MQTT topic) dependent on
	 * its current state.
	 * 
	 * @param topic
	 *            MQTT Topic string of the message
	 * @param message
	 *            MQTT payload
	 */
	public void mqttFieldOccupy(String topic, String message) {
		int[] coordinates = parseFieldFromTopic(topic);

		IFieldWriteable f = this.stage.getFieldWriteable(coordinates[0],
				coordinates[1]);

		if (f.getState() != State.OURS) {
			f.setState(State.OCCUPIED);
			f.setLockedBy(message);
			f.emitEvent(UpdateType.FIELD_STATE);
		}
	}

	/**
	 * Handle an FIELD_OCCUPY_RELEASE message.
	 * 
	 * Updates the state of the field (given by id in MQTT topic) dependent on
	 * its current state.
	 * 
	 * @param topic
	 *            MQTT Topic string of the message
	 * @param message
	 *            MQTT payload
	 */
	public void mqttFieldRelease(String topic, String message) {
		int[] coordinates = parseFieldFromTopic(topic);

		IFieldWriteable f = this.stage.getFieldWriteable(coordinates[0],
				coordinates[1]);

		switch (f.getState()) {
		case LOCKED:
		case OCCUPIED:
			f.setState(State.FREE);
			f.setLockedBy("");
			f.emitEvent(UpdateType.FIELD_STATE);
			break;
		default:
			break;
		}
	}

	/**
	 * Parse coordinates of field id from a topic. The ID must be in format
	 * "x-y" and make up the last part of the topic.
	 * 
	 * @param topic
	 *            The MQTT topic string
	 * @return An integer array with the field coordinates [0] = x and [1] = y
	 */
	private int[] parseFieldFromTopic(String topic) {
		String[] split1 = topic.split("/");
		String[] split2 = split1[split1.length - 1].split("-");
		int x = Integer.parseInt(split2[0]);
		int y = Integer.parseInt(split2[0]);

		return new int[] { x, y };
	}
}
