package de.unihannover.swp2015.robots2.controller.mqtt;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * 
 * @author Michael Thies
 */
public class MqttController implements IMqttController {
	LinkedBlockingQueue<MqttFullMessage> receiveQueue;
	LinkedBlockingQueue<MqttFullMessage> sendQueue;
	MqttClient client;

	/**
	 * 
	 * @param mqttBrokerUrl
	 * @param mqttClientId
	 * @param receiveHandler
	 * @param subscribeTopcis
	 */
	public MqttController(String mqttBrokerUrl, String mqttClientId,
			IMqttMessageHandler receiveHandler, List<String> subscribeTopcis)
			throws MqttException {

		this.client = new MqttClient(mqttBrokerUrl, mqttClientId,
				new MemoryPersistence());

		// mqtt connection options
		MqttConnectOptions connOpt = new MqttConnectOptions();
		connOpt.setCleanSession(true);
		connOpt.setKeepAliveInterval(10);
		// TODO Set last will
		// connOpt.setWill("error/connection",
		// "The important client lost its connection.".getBytes(), 0, false);
		connOpt.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);

		// mqttclient connect
		client.connect(connOpt);

		// mqttclient subscribes
		for (String topic : subscribeTopcis) {
			client.subscribe(topic);
		}

		// mqtt client callbacks
		client.setCallback(new MqttCallback() {

			@Override
			public void messageArrived(String topic, MqttMessage message) {
				try {
					MqttController.this.receiveQueue.put(new MqttFullMessage(
							topic, message));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken arg0) {

			}

			@Override
			public void connectionLost(Throwable arg0) {
				// TODO
			}
		});

		// send- and receive worker threads
		Thread receiveWorker = new Thread(new MqttReceiver(receiveQueue,
				receiveHandler));
		receiveWorker.start();
		Thread sendWorker = new Thread(new MqttSender(sendQueue, client));
		sendWorker.start();
	}

	@Override
	public void sendMessage(String topic, String message) {
		MqttMessage mqttMessage = new MqttMessage(message.getBytes());
		try {
			this.sendQueue.put(new MqttFullMessage(topic, mqttMessage));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
