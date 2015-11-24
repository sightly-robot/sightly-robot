package de.unihannover.swp2015.robots2.controller.main;

import java.util.Arrays;

import org.eclipse.paho.client.mqttv3.MqttException;

import de.unihannover.swp2015.robots2.controller.interfaces.IServerController;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttController;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class ServerMainController extends AbstractMainController implements IServerController {

	public ServerMainController() {
		super();
		
		this.infoComponent = "server";
	}
	
	@Override
	public boolean startMqtt(String brokerUrl) {
		if (this.mqttController == null) {
			String clientId = "server";
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
		} else {
			return true;
		}
	}

	@Override
	public void handleMqttMessage(String topic, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateScore(String robotId, int score) {
		this.game.getRobotsWriteable().get(robotId).addScore(score);
	}

	@Override
	public void updateFood(int x, int y, int value) {
		for( int i=0; i<value; i++ ) {
			this.game.getStageWriteable().getFieldWriteable(x, y).incrementFood();
		}
	}
}
