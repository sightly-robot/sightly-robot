package de.unihannover.swp2015.robots2.controller.main;

import java.util.Arrays;
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
import de.unihannover.swp2015.robots2.model.interfaces.IRobot.RobotState;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IFieldWriteable;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IRobotWriteable;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class RobotMainController extends AbstractMainController implements
		IRobotController, IFieldTimerController {

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
			String[] subscribeTopics = { "robot/#", "map/walls", "map/food",
					"map/food/+", "map/occupied/#", "control/state",
					"extension/2/robot/#" };
			this.mqttController = new MqttController(clientId, this,
					Arrays.asList(subscribeTopics));
		} catch (MqttException e) {
			log.fatal("Error constructing MqttController:", e);
		}
	}

	@Override
	public void startMqtt(String brokerUrl) throws MqttException {

		this.mqttController.connect(brokerUrl);

		String hardwareRobot = (this.myself.isHardwareRobot()) ? "real"
				: "virtual";
		this.sendMqttMessage(MqttTopic.ROBOT_TYPE, this.myself.getId(),
				hardwareRobot);
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
			String hardwareRobot = (this.myself.isHardwareRobot()) ? "real"
					: "virtual";
			this.sendMqttMessage(MqttTopic.ROBOT_TYPE, this.myself.getId(),
					hardwareRobot);
			break;

		case ROBOT_NEW:
			// Inform the new robot about fields we have occupied
			for (IFieldWriteable ourField : this.stageModelController
					.getOurFields()) {
				this.sendMqttMessage(MqttTopic.FIELD_OCCUPIED_SET,
						ourField.getX() + "-" + ourField.getY(),
						this.myself.getId());
			}
			break;

		case ROBOT_TYPE:
			this.gameModelController.mqttAddRobot(key, message);
			break;

		case ROBOT_STATE:
			this.robotModelController.mqttRobotState(key, message);
			break;

		case ROBOT_SETPOSITION:
			// TODO refactor to own method
			if (this.myself.getId().equals(key)) {
				String[] positionParts = message.split(",");
				int x = Integer.parseInt(positionParts[0]);
				int y = Integer.parseInt(positionParts[1]);
				Orientation o = Orientation.getBy(positionParts[2]);

				// Don't do anything if the target field is occupied by another
				// robot
				IField.State state = this.game.getStage().getField(x, y)
						.getState();
				if (state == State.LOCKED || state == State.OCCUPIED)
					return;

				// Occupy target field and release old fields
				for (IFieldWriteable ourField : this.stageModelController
						.getOurFields()) {
					this.releaseField(ourField.getX(), ourField.getY());
				}
				this.occupyField(x, y);

				// Broadcast new position and SETUPSTATE
				this.sendMqttMessage(MqttTopic.ROBOT_POSITION,
						this.myself.getId(), Integer.toString(x) + ","
								+ Integer.toString(y) + "," + o.toString());
				this.sendMqttMessage(MqttTopic.ROBOT_STATE,
						this.myself.getId(), RobotState.SETUPSTATE.toString());

				// Change local model (position and state)
				this.myself.setPosition(x, y, o);
				this.myself.setRobotState(RobotState.SETUPSTATE);

				// Emit model events
				this.myself.emitEvent(UpdateType.ROBOT_POSITION);
				this.myself.emitEvent(UpdateType.ROBOT_PROGRESS);
				this.myself.emitEvent(UpdateType.ROBOT_STATE);
			}
			break;

		case ROBOT_POSITION:
			this.robotModelController.mqttRobotPosition(key, message);
			break;

		case ROBOT_BLINK:
			if (this.hardwareRobot != null) {
				// TODO correct color handling
				this.hardwareRobot.blink(this.myself.getColor());
			}
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
	public void onMqttStateChange(boolean connected) {
		// First do the same thing as any other main controller
		this.game.setSynced(connected);

		// then update our robot state and inform other clients about state and
		// reconnect

		switch (this.myself.getState()) {
		// If we are not disabled for any other cause, we will disable ourself
		// due to connection loss and reconnect
		case ENABLED:
		case SETUPSTATE:
		case DISCONNECTED:
			this.myself.setRobotState(RobotState.CONNECTED);
			this.sendMqttMessage(MqttTopic.ROBOT_STATE, this.myself.getId(),
					RobotState.CONNECTED.toString());
			break;

		default:
			break;
		}

		this.myself.setRobotConnectionState(connected);
		this.sendMqttMessage(MqttTopic.EVENT_ERROR_ROBOT_CONNECTION,
				this.myself.getId(), null);

		// Emit correct model events
		this.game.emitEvent(UpdateType.MODEL_SYNC_STATE);
		this.myself.emitEvent(UpdateType.ROBOT_STATE);
	}

	@Override
	public void updatePosition(int x, int y, Orientation orientation) {
		this.sendMqttMessage(MqttTopic.ROBOT_POSITION, this.myself.getId(), x
				+ "," + y + "," + orientation.toString());
		this.myself.setPosition(x, y, orientation);
		this.myself.emitEvent(UpdateType.ROBOT_POSITION);
		this.myself.emitEvent(UpdateType.ROBOT_PROGRESS);
	}

	@Override
	public void updatePositionProgress(int progress) {
		this.sendMqttMessage(MqttTopic.ROBOT_PROGRESS, this.myself.getId(),
				String.valueOf(progress));
		this.myself.setProgress(progress);
		this.myself.emitEvent(UpdateType.ROBOT_PROGRESS);
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
		IField f = this.game.getStage().getField(x, y);

		// Releasing is not possible if not our field
		if (f.getState() != State.OURS)
			return;

		// Send release message if has been occupied by us
		this.sendMqttMessage(MqttTopic.FIELD_OCCUPIED_RELEASE, (x + "-" + y),
				"");
		this.fieldStateModelController.setFieldRelease(x, y);
	}

	@Override
	public void setRobotReady() {
		if (this.myself.getState() == RobotState.SETUPSTATE) {
			sendMqttMessage(MqttTopic.ROBOT_STATE, this.myself.getId(),
					RobotState.ENABLED.toString());
			this.myself.setRobotState(RobotState.ENABLED);
			this.myself.emitEvent(UpdateType.ROBOT_STATE);
		}
	}

	@Override
	public void reportRoboticsError() {
		sendMqttMessage(MqttTopic.ROBOT_STATE, this.myself.getId(),
				RobotState.ROBOTICS_ERROR.toString());
		// This message is only for protocol standard compliance
		sendMqttMessage(MqttTopic.EVENT_ERROR_ROBOT_ROBOTICS,
				this.myself.getId(), "error");
		this.myself.setRobotState(RobotState.ROBOTICS_ERROR);
		this.myself.emitEvent(UpdateType.ROBOT_STATE);
	}

	@Override
	public void disableMyself() {
		sendMqttMessage(MqttTopic.ROBOT_STATE, this.myself.getId(),
				RobotState.MANUAL_DISABLED_ROBOT.toString());
		this.myself.setRobotState(RobotState.MANUAL_DISABLED_ROBOT);
		this.myself.emitEvent(UpdateType.ROBOT_STATE);
	}

	@Override
	public void deleteMyself() {
		sendMqttMessage(MqttTopic.ROBOT_TYPE, this.myself.getId(), null);
		sendMqttMessage(MqttTopic.ROBOT_STATE, this.myself.getId(), null);
		sendMqttMessage(MqttTopic.ROBOT_POSITION, this.myself.getId(), null);
		for (IFieldWriteable ourField : this.stageModelController
				.getOurFields()) {
			this.releaseField(ourField.getX(), ourField.getY());
		}

	}

	@Override
	public IRobot getMyself() {
		return this.myself;
	}

	@Override
	public void occupyField(int x, int y) {
		this.sendMqttMessage(MqttTopic.FIELD_OCCUPIED_SET, (x + "-" + y),
				this.myself.getId());
		this.fieldStateModelController.setFieldOccupy(x, y);
	}

	@Override
	public void registerHardwareRobot(IHardwareRobot hardwareRobot) {
		this.hardwareRobot = hardwareRobot;
	}
}
