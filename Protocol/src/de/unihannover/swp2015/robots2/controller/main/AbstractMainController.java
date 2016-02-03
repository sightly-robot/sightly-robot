package de.unihannover.swp2015.robots2.controller.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;

import de.unihannover.swp2015.robots2.controller.interfaces.IController;
import de.unihannover.swp2015.robots2.controller.interfaces.ProtocolException;
import de.unihannover.swp2015.robots2.controller.model.FieldStateModelController;
import de.unihannover.swp2015.robots2.controller.model.GameModelController;
import de.unihannover.swp2015.robots2.controller.model.RobotModelController;
import de.unihannover.swp2015.robots2.controller.model.StageModelController;
import de.unihannover.swp2015.robots2.controller.mqtt.IMqttController;
import de.unihannover.swp2015.robots2.controller.mqtt.IMqttMessageHandler;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttController;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttTopic;
import de.unihannover.swp2015.robots2.model.implementation.Game;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent.UpdateType;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IGameWriteable;

public abstract class AbstractMainController implements IController,
		IMqttMessageHandler {

	protected final IGameWriteable game;
	protected IMqttController mqttController;

	protected final StageModelController stageModelController;
	protected final FieldStateModelController fieldStateModelController;
	protected final RobotModelController robotModelController;
	protected final GameModelController gameModelController;

	protected Logger log = LogManager
			.getLogger(this.getClass().getSimpleName());

	public AbstractMainController() {
		this.game = new Game();

		this.stageModelController = new StageModelController(
				this.game.getStageWriteable());
		this.fieldStateModelController = new FieldStateModelController(
				this.game.getStageWriteable());
		this.robotModelController = new RobotModelController(
				this.game.getRobotsWriteable());
		this.gameModelController = new GameModelController(this.game);
	}

	@Override
	public IGame getGame() {
		return this.game;
	}

	/**
	 * Generate a list of topics to subscribe to pass them to the constructor of
	 * the {@link MqttController}.
	 * 
	 * The list will include the (wildcarded) string representation of all
	 * general topics (which can be processed by the
	 * {@link #processGeneralMessage(MqttTopic, String, String)} method and the
	 * string representation of all topics passed via the argument.
	 * 
	 * @param extendedTopics
	 *            A list of MQTT topics that should by subscribed additionally
	 *            to the general topics.
	 * @return The full list of topic strings
	 */
	protected String[] getSubscribeTopcis(MqttTopic[] extendedTopics) {
		MqttTopic[] generalTopics = { MqttTopic.CONTROL_STATE,
				MqttTopic.CONTROL_RESET, MqttTopic.ROBOT_TYPE,
				MqttTopic.ROBOT_STATE, MqttTopic.ROBOT_SCORE,
				MqttTopic.ROBOT_BLINK, MqttTopic.EVENT_ERROR_ROBOT_CONNECTION,
				MqttTopic.ROBOT_POSITION, MqttTopic.MAP_WALLS,
				MqttTopic.MAP_FOOD, MqttTopic.FIELD_FOOD,
				MqttTopic.FIELD_OCCUPIED_LOCK, MqttTopic.FIELD_OCCUPIED_SET,
				MqttTopic.FIELD_OCCUPIED_RELEASE };

		String[] subscribeTopics = new String[generalTopics.length
				+ extendedTopics.length];

		for (int i = 0; i < generalTopics.length; i++)
			subscribeTopics[i] = generalTopics[i].toString();
		for (int i = 0; i < extendedTopics.length; i++)
			subscribeTopics[generalTopics.length + i] = extendedTopics[i]
					.toString();

		return subscribeTopics;
	}

	/**
	 * Process an MQTT message of an specific topic.
	 * 
	 * This method catches all topics that should be processed by any kind of
	 * main controller. If it returns {@code false}, check if the message of
	 * this topic needs a special treatment in your particular main controller.
	 * 
	 * @param topic
	 *            The parsed MQTT topic
	 * @param key
	 *            The key extracted from the MQTT topic
	 * @param message
	 *            The MQTT message
	 * @return {@code true} if the message was processed by this message,
	 *         {@code false} if this method is not responsible for this topic
	 */
	protected boolean processGeneralMessage(MqttTopic topic, String key,
			String message) {

		switch (topic) {

		case CONTROL_STATE:
			this.gameModelController.mqttSetGameState(message);
			break;

		case CONTROL_RESET:
			this.game.emitEvent(UpdateType.GAME_RESET);
			break;

		case ROBOT_TYPE:
			this.gameModelController.mqttAddRobot(key, message);
			if ("".equals(message))
				this.fieldStateModelController.releaseFieldsOfRobot(key);
			break;

		case ROBOT_STATE:
			this.robotModelController.mqttRobotState(key, message);
			break;

		case ROBOT_SCORE:
			this.robotModelController.mqttScoreUpdate(key, message);
			break;

		case ROBOT_BLINK:
			this.robotModelController.mqttBlink(key);
			break;

		case EVENT_ERROR_ROBOT_CONNECTION:
			this.robotModelController.mqttRobotConnectionState(key, message);
			break;

		case ROBOT_POSITION:
			this.robotModelController.mqttRobotPosition(key, message);
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
			this.fieldStateModelController.mqttFieldLock(key, message);
			break;

		case FIELD_OCCUPIED_SET:
			this.fieldStateModelController.mqttFieldOccupy(key, message);
			break;

		case FIELD_OCCUPIED_RELEASE:
			this.fieldStateModelController.mqttFieldRelease(key);
			break;

		default:
			return false;
		}

		return true;
	}

	/**
	 * Send an MQTT message with this controller's mqtt controller – if it was
	 * started properly.
	 * 
	 * The Retained flag will be set according to the topic's setting.
	 * 
	 * @param topic
	 *            MQTT topic of the message
	 * @param topicKey
	 *            Id or key that will be inserted in the topic string. NULL if
	 *            you don't want to insert a key.
	 * @param message
	 *            Payload of the MQTT message
	 */
	protected void sendMqttMessage(MqttTopic topic, String topicKey,
			String message) {
		if (this.mqttController != null) {
			String fullTopic = (topicKey == null) ? topic.toString() : topic
					.toString(topicKey);
			this.mqttController.sendMessage(fullTopic, message,
					topic.isRetained());
		}
	}

	@Override
	public void onMqttStateChange(boolean connected) {
		this.game.setSynced(connected);
		this.game.emitEvent(UpdateType.MODEL_SYNC_STATE);
	}

	@Override
	public void startMqtt(String brokerUrl) throws ProtocolException {
		try {
			this.mqttController.connect(brokerUrl);
		} catch (MqttException e) {
			throw new ProtocolException("MQTT Connect failed.", e);
		}
	}
}
