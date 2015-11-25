package de.unihannover.swp2015.robots2.controller.main;

import java.util.Arrays;

import org.eclipse.paho.client.mqttv3.MqttException;

import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttController;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttTopic;
import de.unihannover.swp2015.robots2.model.implementation.Robot;
import de.unihannover.swp2015.robots2.model.interfaces.IField.State;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IRobotWriteable;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class RobotMainController extends AbstractMainController implements IRobotController, IFieldTimerController {

	private IRobotWriteable myself;

	public RobotMainController(boolean hardwareRobot) {
		super();

		// TODO generate proper ID
		String id = "TESTID";
		this.myself = new Robot(id, hardwareRobot, true);
		this.game.addRobot(this.myself);

		this.infoComponent = "robot/" + id;
	}

	@Override
	public boolean startMqtt(String brokerUrl) {
		if (this.mqttController == null) {
			String clientId = "robot_" + this.myself.getId();
			// TODO subscription list
			String[] subscribeTopics = {};

			try {
				this.mqttController = new MqttController(brokerUrl, clientId, this, Arrays.asList(subscribeTopics));
				return true;
			} catch (MqttException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return true;
		}
	}

	@Override
	public void handleMqttMessage(String topic, String message) {
		MqttTopic mqtttopic = MqttTopic.getBy(topic);
		String key = MqttTopic.getKey(topic);

		if (mqtttopic != null) {
			switch (mqtttopic) {
			case ROBOT_DISCOVER:
				String hardwareRobot = (this.myself.isHardwareRobot()) ? "real" : "virtual";
				this.sendMqttMessage("robot/type/" + this.myself.getId(), hardwareRobot);
				break;
			case ROBOT_NEW:
				String fieldId = this.myself.getPosition().getX() + "-" + this.myself.getPosition().getX();
				this.sendMqttMessage("map/occupy/" + fieldId, this.myself.getId());
				break;
			case ROBOT_TYPE:
				this.gameModelController.mqttAddRobot(key, message);
				break;
			case ROBOT_POSITION:

			case ROBOT_SETPOSITION:
				if (!this.game.isRunning() && key.equals(this.myself.getId())) {
					String[] positionParts = message.split(",");
					this.myself.setPosition(Integer.valueOf(positionParts[0]), Integer.valueOf(positionParts[1]),
							Orientation.getBy(positionParts[2]));
				}
				break;
			case ROBOT_VIRTUALSPEED:
				this.gameModelController.mqttSetRobotVirtualspeed(Float.valueOf(message));
				break;
			case MAP_WALLS:
				this.stageModelController.mqttSetWalls(message);
				break;
			case MAP_FOOD:
				this.stageModelController.mqttSetFood(message);
				break;
			case FIELD_FOOD:
				String[] coordinates = key.split("-");
				if (coordinates.length == 2) {
					this.stageModelController.mqttSetFieldFood(Integer.valueOf(coordinates[0]),
							Integer.valueOf(coordinates[1]), Integer.valueOf(message));
				}
				break;
			case FIELD_OCCUPIED_LOCK:
				this.fieldStateModelController.mqttFieldLock(topic, message);
				break;
			case FIELD_OCCUPIED_SET:
				this.fieldStateModelController.mqttFieldOccupy(topic, message);
				break;
			case FIELD_OCCUPIED_RELEASE:
				this.fieldStateModelController.mqttFieldRelease(topic, message);
				break;
			case CONTROL_STATE:
				this.gameModelController.mqttSetGameState(message);
				break;
			default:
				break;
			}
		}

	}

	@Override
	public void updatePosition(int x, int y, Orientation orientation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updatePositionProgress(int progress) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestField(int x, int y) {
		if (this.game.getStage().getField(x, y).getState() != State.FREE)
			return;

		this.sendMqttMessage(MqttTopic.FIELD_OCCUPIED_LOCK.toString(x + "-" + y), this.myself.getId());
		this.fieldStateModelController.setFieldLock(x, y);
	}

	@Override
	public void releaseField(int x, int y) {
		this.sendMqttMessage(MqttTopic.FIELD_OCCUPIED_RELEASE.toString(x + "-" + y), this.myself.getId());
		this.fieldStateModelController.setFieldRelease(x, y);
	}

	@Override
	public void setRobotReady() {
		// TODO Auto-generated method stub

	}

	@Override
	public IRobot getMyself() {
		return this.myself;
	}

	@Override
	public void retryLockField(int x, int y) {
		this.requestField(x, y);
	}

	@Override
	public void occupyField(int x, int y) {
		this.sendMqttMessage(MqttTopic.FIELD_OCCUPIED_SET.toString(x + "-" + y), this.myself.getId());
		this.fieldStateModelController.setFieldOccupy(x, y);
	}
}
