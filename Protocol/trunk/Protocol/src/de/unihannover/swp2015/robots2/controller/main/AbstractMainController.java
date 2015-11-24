package de.unihannover.swp2015.robots2.controller.main;


import de.unihannover.swp2015.robots2.controller.interfaces.IController;
import de.unihannover.swp2015.robots2.controller.interfaces.InfoType;
import de.unihannover.swp2015.robots2.controller.model.GameModelController;
import de.unihannover.swp2015.robots2.controller.model.RobotModelController;
import de.unihannover.swp2015.robots2.controller.model.StageModelController;
import de.unihannover.swp2015.robots2.controller.mqtt.IMqttController;
import de.unihannover.swp2015.robots2.controller.mqtt.IMqttMessageHandler;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttTopic;
import de.unihannover.swp2015.robots2.model.implementation.Game;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IGameWriteable;

public abstract class AbstractMainController implements IController,
		IMqttMessageHandler {

	protected final IGameWriteable game;
	protected IMqttController mqttController;

	protected final StageModelController stageModelController;
	protected final RobotModelController robotModelController;
	protected final GameModelController gameModelController;

	private InfoType infoType = InfoType.WARNING;
	protected String infoComponent = "general";

	public AbstractMainController() {
		this.game = new Game();

		this.stageModelController = new StageModelController(
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
			this.sendMqttMessage("event/" + type.toString() + "/"
					+ this.infoComponent + "/" + topic, message);
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
	 * @param topic MQTT topic of the message
	 * @param message Payload of the MQTT message
	 */
	protected void sendMqttMessage(String topic, String message) {
		if (this.mqttController != null) {
			this.mqttController.sendMessage(topic, message);
		}
	}

}
