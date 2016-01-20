package de.unihannover.swp2015.robots2.utils.robotMarshall;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;

import de.unihannover.swp2015.robots2.controller.interfaces.ProtocolException;
import de.unihannover.swp2015.robots2.controller.main.AbstractMainController;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttController;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttTopic;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IField.State;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class MarshalMainController extends AbstractMainController {

	private Logger log = LogManager.getLogger("MarshalMainController");
	private Map<IRobot, Set<IField>> robotFieldsMap = new HashMap<>();

	public MarshalMainController() {
		super();

		String id = UUID.randomUUID().toString().substring(0, 8);

		this.infoComponent = "robot/" + id;

		// Start MQTTController
		try {
			String[] subscribeTopics = { "robot/#", "map/walls",
					"map/occupied/#" };
			this.mqttController = new MqttController("marshal", this,
					Arrays.asList(subscribeTopics));
		} catch (MqttException e) {
		}
	}

	@Override
	public void handleMqttMessage(String topic, String message) {
		MqttTopic mqtttopic = MqttTopic.getBy(topic);
		String key = mqtttopic.getKey(topic);
		
		switch (mqtttopic) {
		// TODO handle robot settings
		case ROBOT_TYPE:
			this.gameModelController.mqttAddRobot(key, message);
			if( !message.equals("") )  {
				//this.robotFieldsMap.put( this.getGame().getRobots().get(key), new HashSet<>() );
			}
			break;

		case ROBOT_STATE:
			this.robotModelController.mqttRobotState(key, message);
			break;

		case ROBOT_POSITION:
			this.checkRobotPosition(key, message);
			break;

		case MAP_WALLS:
			this.stageModelController.mqttSetWalls(message);
			break;

		case FIELD_OCCUPIED_LOCK: // sind >2 enthalten?
			this.checkFieldAndLock(key, message);
			break;

		case FIELD_OCCUPIED_SET: // erst hinzufügen, dann sind >2 enthalten?
			this.checkFieldAndSet(key, message);
			break;

		case FIELD_OCCUPIED_RELEASE: // entfernen
			this.checkFieldAndRelease(key, message);
			break;

		default:
			break;
		}
	}

	private void checkFieldAndRelease(String key, String message) {
		String[] coord = key.split("-");
		int x = Integer.parseInt(coord[0]);
		int y = Integer.parseInt(coord[1]);

		if (x < 0 || y < 0 || x >= this.getGame().getStage().getWidth()
				|| y >= this.getGame().getStage().getHeight()) {
			log.error("Robot on invalid position (" + x + "," + y + ").");
			return;
		}

		this.fieldStateModelController.mqttFieldRelease(key);
	}

	private void checkFieldAndLock(String key, String message) {
		String[] coord = key.split("-");
		int x = Integer.parseInt(coord[0]);
		int y = Integer.parseInt(coord[1]);

		if (x < 0 || y < 0 || x >= this.getGame().getStage().getWidth()
				|| y >= this.getGame().getStage().getHeight()) {
			log.error("Robot on invalid position (" + x + "," + y + ").");
			return;
		}

		IField f = this.getGame().getStage().getField(x, y);
		if (f.getState() != State.FREE && f.getState() != State.LOCKED) {
			log.debug(message + " tries to lock (" + x + "," + y + ") which "
					+ f.getLockedBy() + " already locks.");
		}

		this.fieldStateModelController.mqttFieldLock(key, message);
	}

	private void checkFieldAndSet(String key, String message) {
		String[] coord = key.split("-");
		int x = Integer.parseInt(coord[0]);
		int y = Integer.parseInt(coord[1]);

		if (x < 0 || y < 0 || x >= this.getGame().getStage().getWidth()
				|| y >= this.getGame().getStage().getHeight()) {
			log.error("Robot on invalid position (" + x + "," + y + ").");
			return;
		}

		IField f = this.getGame().getStage().getField(x, y);
		if (f.getState() == State.OCCUPIED) {
			log.warn(message + " tries to lock (" + x + "," + y + ") which "
					+ f.getLockedBy() + " already locks.");
		} else {
			if (f.getState() != State.LOCKED
					|| !f.getLockedBy().equals(message)) {
				log.warn("occupy without lock on (" + x + "," + y + ") from "
						+ message);
			}

			this.fieldStateModelController.mqttFieldOccupy(key, message);
		}
	}

	private void checkRobotPosition(String key, String message) {
		IRobot r = this.getGame().getRobots().get(key);

		if (r == null) {
			log.error("Robot " + key + " does not exist!");
			return;
		}

		String[] positionParts = message.split(",");
		int x = Integer.parseInt(positionParts[0]);
		int y = Integer.parseInt(positionParts[1]);

		if (x < 0 || y < 0 || x >= this.getGame().getStage().getWidth()
				|| y >= this.getGame().getStage().getHeight()) {
			log.error("Robot on invalid position (" + x + "," + y + ").");
			return;
		}

		IField f = this.getGame().getStage().getField(x, y);
		if (f.getState() != State.OCCUPIED
				|| !f.getLockedBy().equals(r.getId())) {
			log.warn(r.getId() + " tries to move to (" + x + "," + y
					+ ") which is not occupied.");
		}

		this.robotModelController.mqttRobotPosition(key, message);
	}
}
