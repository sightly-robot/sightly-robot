package de.unihannover.swp2015.robots2.controller.main;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttException;

import de.unihannover.swp2015.robots2.controller.externalInterfaces.IHardwareRobot;
import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttController;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttTopic;
import de.unihannover.swp2015.robots2.model.implementation.Robot;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent.UpdateType;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
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
	private IHardwareRobot hardwareRobot;

	public RobotMainController(boolean hardwareRobot) {
		super();

		String id = UUID.randomUUID().toString().substring(0, 8);
		this.myself = new Robot(id, hardwareRobot, true);
		this.game.addRobot(this.myself);

		this.fieldStateModelController.setFieldTimerCallback(this);
		this.infoComponent = "robot/" + id;
		
		// Start MQTTController
		try {
			String clientId = "robot_" + this.myself.getId();
			String[] subscribeTopics = { "robot/#", "map/walls", "map/food", "map/food/+", "map/occupied/#",
					"control/state" };
			this.mqttController = new MqttController(clientId, this, Arrays.asList(subscribeTopics));
		} catch (MqttException e) {
		}
	}

	@Override
	public void startMqtt(String brokerUrl) throws MqttException {
			
		this.mqttController.connect(brokerUrl);

		String hardwareRobot = (this.myself.isHardwareRobot()) ? "real" : "virtual";
		this.sendMqttMessage(MqttTopic.ROBOT_TYPE, this.myself.getId(), hardwareRobot);
		this.sendMqttMessage(MqttTopic.ROBOT_NEW, null, this.myself.getId());
		this.sendMqttMessage(MqttTopic.ROBOT_DISCOVER, null, "");
	}

	@Override
	public void handleMqttMessage(String topic, String message) {
		MqttTopic mqtttopic = MqttTopic.getBy(topic);
		String key = mqtttopic.getKey(topic);

		switch (mqtttopic) {
		// TODO handle robot settings
		case ROBOT_DISCOVER:
			/* Should be deprecated when using retained messages */
			String hardwareRobot = (this.myself.isHardwareRobot()) ? "real" : "virtual";
			this.sendMqttMessage(MqttTopic.ROBOT_TYPE, this.myself.getId(), hardwareRobot);
			break;

		case ROBOT_NEW:
			List<String> ourFields = this.stageModelController.getOurFields();
			for (String ourField : ourFields) {
				this.sendMqttMessage(MqttTopic.FIELD_OCCUPIED_SET, ourField, this.myself.getId());
			}
			break;

		case ROBOT_TYPE:
			this.gameModelController.mqttAddRobot(key, message);
			break;

		case ROBOT_SETPOSITION:
			String[] positionParts = message.split(",");
			this.occupyField(Integer.parseInt(positionParts[0]), Integer.parseInt(positionParts[1]));
		case ROBOT_POSITION:
			this.robotModelController.mqttRobotPosition(key, message, mqtttopic == MqttTopic.ROBOT_SETPOSITION);
			break;

		case ROBOT_BLINK:
			if (this.hardwareRobot != null) {
				// TODO correct color handling
				this.hardwareRobot.blink(this.myself.getColor());
			}
			break;

		case CONTROL_VIRTUALSPEED:
			this.gameModelController.mqttSetRobotVirtualspeed(Float.valueOf(message));
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
			if (!message.equals(this.myself.getId()))
				this.fieldStateModelController.mqttFieldLock(key, message);
			break;

		case FIELD_OCCUPIED_SET:
			if (!message.equals(this.myself.getId()))
				this.fieldStateModelController.mqttFieldOccupy(key, message);
			break;

		case FIELD_OCCUPIED_RELEASE:
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
		this.sendMqttMessage(MqttTopic.ROBOT_POSITION, this.myself.getId(), x + "," + y + "," + orientation.toString());
		this.myself.setPosition(x, y, orientation);
		this.myself.emitEvent(UpdateType.ROBOT_POSITION);
		this.myself.emitEvent(UpdateType.ROBOT_PROGRESS);
	}

	@Override
	public void updatePositionProgress(int progress) {
		this.sendMqttMessage(MqttTopic.ROBOT_PROGRESS, this.myself.getId(), String.valueOf(progress));
		this.myself.setProgress(progress);
		this.myself.emitEvent(UpdateType.ROBOT_PROGRESS);
	}

	@Override
	public void requestField(int x, int y) {
		if (this.game.getStage().getField(x, y).getState() != State.FREE)
			return;

		this.sendMqttMessage(MqttTopic.FIELD_OCCUPIED_LOCK, (x + "-" + y), this.myself.getId());
		this.fieldStateModelController.setFieldLock(x, y);
	}

	@Override
	public void releaseField(int x, int y) {
		IField f = this.game.getStage().getField(x, y);
		if (f.getState() != State.OURS && f.getState() != State.RANDOM_WAIT)
			return;

		if (f.getState() == State.OURS)
			this.sendMqttMessage(MqttTopic.FIELD_OCCUPIED_RELEASE, (x + "-" + y), "");
		this.fieldStateModelController.setFieldRelease(x, y);
	}

	@Override
	public void setRobotReady() {
		this.myself.setSetupState(false);
		this.sendMqttMessage(MqttTopic.ROBOT_READY, this.myself.getId(), "");
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
		this.sendMqttMessage(MqttTopic.FIELD_OCCUPIED_SET, (x + "-" + y), this.myself.getId());
		this.fieldStateModelController.setFieldOccupy(x, y);
	}

	@Override
	public void registerHardwareRobot(IHardwareRobot hardwareRobot) {
		this.hardwareRobot = hardwareRobot;
	}
}
