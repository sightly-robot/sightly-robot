package de.unihannover.swp2015.robots2.controller.main;

import org.eclipse.paho.client.mqttv3.MqttException;

import de.unihannover.swp2015.robots2.controller.interfaces.IController;
import de.unihannover.swp2015.robots2.controller.interfaces.InfoType;
import de.unihannover.swp2015.robots2.controller.model.FieldStateModelController;
import de.unihannover.swp2015.robots2.controller.model.GameModelController;
import de.unihannover.swp2015.robots2.controller.model.RobotModelController;
import de.unihannover.swp2015.robots2.controller.model.StageModelController;
import de.unihannover.swp2015.robots2.controller.mqtt.IMqttController;
import de.unihannover.swp2015.robots2.controller.mqtt.IMqttMessageHandler;
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

	private InfoType infoType = InfoType.WARNING;
	protected String infoComponent = "general";

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

	@Override
	public void sendInfoMessage(InfoType type, String topic, String message) {
		if (type.compareTo(this.infoType) >= 0) {
			this.mqttController.sendMessage("event/" + type.toString() + "/"
					+ this.infoComponent + "/" + topic, message, false);
			System.out.println(type.toString() + ": " + message);
		}
	}

	@Override
	public void setInfoLevel(InfoType type) {
		this.infoType = type;
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
	public void startMqtt(String brokerUrl) throws MqttException {
		this.mqttController.connect(brokerUrl);
	}
}
