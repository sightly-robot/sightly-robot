package de.unihannover.swp2015.robots2.controller.main;

import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttException;

import de.unihannover.swp2015.robots2.controller.interfaces.IController;
import de.unihannover.swp2015.robots2.controller.interfaces.InfoType;
import de.unihannover.swp2015.robots2.controller.model.GameModelController;
import de.unihannover.swp2015.robots2.controller.model.RobotModelController;
import de.unihannover.swp2015.robots2.controller.model.StageModelController;
import de.unihannover.swp2015.robots2.controller.mqtt.IMqttController;
import de.unihannover.swp2015.robots2.controller.mqtt.IMqttMessageHandler;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttController;
import de.unihannover.swp2015.robots2.model.implementation.Game;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IGameWriteable;

public abstract class AbstractMainController implements IController, IMqttMessageHandler {
	
	protected final IGameWriteable game;
	protected IMqttController mqttController;
	
	protected final StageModelController stageModelController;
	protected final RobotModelController robotModelController;
	protected final GameModelController gameModelController;
	
	private InfoType infoType;
	
	public AbstractMainController() {
		this.game = new Game();
		
		this.stageModelController = new StageModelController( this.game.getStageWriteable() );
		this.robotModelController = new RobotModelController( this.game.getRobotsWriteable() );
		this.gameModelController = new GameModelController( this.game );
	}
	
	@Override
	public IGame getGame() {
		return this.game;
	}
	
	protected void sendMqttMessage(String topic, String message) {
		if( this.mqttController != null ) {
			this.mqttController.sendMessage(topic, message);
		}
	}
	
	public void startMqtt( String brokerUrl ) throws MqttException {
		if( this.mqttController == null ) {
			String clientId = "";
			this.mqttController = new MqttController(brokerUrl, clientId, this, this.getMqttSubscribeTopics());
		} else {
			// TODO throw exception
		}
	}
	
	protected abstract List<String> getMqttSubscribeTopics();

}
