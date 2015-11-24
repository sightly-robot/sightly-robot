/**
 * 
 */
package de.unihannover.swp2015.robots2.controller.main;

import java.util.Arrays;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttException;

import de.unihannover.swp2015.robots2.controller.interfaces.IVisualizationController;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttController;

/**
 * 
 * @author Michael Thies
 */
public class VisualizationMainController extends AbstractMainController
		implements IVisualizationController {

	public VisualizationMainController() {
		super();

		this.infoComponent = "visualization";
	}

	@Override
	public boolean startMqtt(String brokerUrl) {
		if (this.mqttController == null) {
			String clientId = "visu_" + UUID.randomUUID().toString();
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

	public void handleMqttMessage(String topic, String message) {
		// TODO Auto-generated method stub

	}

}
