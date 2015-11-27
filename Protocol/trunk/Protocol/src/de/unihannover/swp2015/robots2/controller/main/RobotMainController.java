package de.unihannover.swp2015.robots2.controller.main;

import java.util.Arrays;
import java.util.UUID;

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
public class RobotMainController extends AbstractMainController implements
		IRobotController, IFieldTimerController {

	private IRobotWriteable myself;

	public RobotMainController(boolean hardwareRobot) {
		super();

		String id = UUID.randomUUID().toString().substring(0, 8);
		this.myself = new Robot(id, hardwareRobot, true);
		this.game.addRobot(this.myself);

		this.fieldStateModelController.setFieldTimerCallback(this);
		this.infoComponent = "robot/" + id;
	}

	@Override
	public boolean startMqtt(String brokerUrl) {
		if (this.mqttController == null) {
			String clientId = "robot_" + this.myself.getId();
			// TODO subscription list
			String[] subscribeTopics = {};

			try {
				this.mqttController = new MqttController(brokerUrl, clientId,
						this, Arrays.asList(subscribeTopics));
				return true;
			} catch (MqttException e) {
				e.printStackTrace();
				return false;
			}

			// TODO send robot/type
			// TODO send robot/new
			// TODO send robot/discover
		} else {
			return true;
		}
	}

	@Override
	public void handleMqttMessage(String topic, String message) {
		MqttTopic mqtttopic = MqttTopic.getBy(topic);
		String key = mqtttopic.getKey(topic);

		switch (mqtttopic) {
		case ROBOT_DISCOVER:
			/* Should be deprecated when using retained messages */
			String hardwareRobot = (this.myself.isHardwareRobot()) ? "real"
					: "virtual";
			this.sendMqttMessage(MqttTopic.ROBOT_TYPE, this.myself.getId(),
					hardwareRobot);
			break;

		case ROBOT_NEW:
			// TODO send all occupied fields
			break;

		case ROBOT_TYPE:
			this.gameModelController.mqttAddRobot(key, message);
			break;

		case ROBOT_POSITION:
		case ROBOT_SETPOSITION:
			this.robotModelController.mqttRobotPosition(key, message,
					mqtttopic == MqttTopic.ROBOT_SETPOSITION);
			// TODO echo field to robot/position occupy field on
			// robot/setposition
			break;

		case CONTROL_VIRTUALSPEED:
			this.gameModelController.mqttSetRobotVirtualspeed(Float
					.valueOf(message));
			break;

		case MAP_WALLS:
			this.stageModelController.mqttSetWalls(message);
			break;

		case MAP_FOOD:
			this.stageModelController.mqttSetFood(message);
			break;

		case FIELD_FOOD:
			this.stageModelController.mqttSetFieldFood(key, message);
			break;

		case FIELD_OCCUPIED_LOCK:
			if (message != this.myself.getId())
				this.fieldStateModelController.mqttFieldLock(key, message);
			break;

		case FIELD_OCCUPIED_SET:
			if (message != this.myself.getId())
				this.fieldStateModelController.mqttFieldOccupy(key, message);
			break;

		case FIELD_OCCUPIED_RELEASE:
			if (message != this.myself.getId())
				this.fieldStateModelController.mqttFieldRelease(key, message);
			break;

		case CONTROL_STATE:
			this.gameModelController.mqttSetGameState(message);
			break;

		default:
			break;
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

		this.sendMqttMessage(MqttTopic.FIELD_OCCUPIED_LOCK, (x + "-" + y),
				this.myself.getId());
		this.fieldStateModelController.setFieldLock(x, y);
	}

	@Override
	public void releaseField(int x, int y) {
		this.sendMqttMessage(MqttTopic.FIELD_OCCUPIED_RELEASE, (x + "-" + y),
				this.myself.getId());
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
		this.sendMqttMessage(MqttTopic.FIELD_OCCUPIED_SET, (x + "-" + y),
				this.myself.getId());
		this.fieldStateModelController.setFieldOccupy(x, y);
	}
}
