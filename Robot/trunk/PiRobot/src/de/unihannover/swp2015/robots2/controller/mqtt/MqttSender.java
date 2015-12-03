package de.unihannover.swp2015.robots2.controller.mqtt;

import java.util.concurrent.BlockingQueue;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Worker thread for sending MQTT messages. Sequentially reads the messages from
 * the given BlockingQueue and publishes them via the given MQTT client.
 * 
 * @author Michael Thies
 */
public class MqttSender implements Runnable {
	private final BlockingQueue<MqttFullMessage> queue;
	private final MqttClient client;

	public MqttSender(BlockingQueue<MqttFullMessage> queue, MqttClient client) {
		this.queue = queue;
		this.client = client;
	}

	@Override
	public void run() {
		while (true) {
			try {
				MqttFullMessage message = this.queue.take();
				client.publish(message.getTopic(), message.getMessage());
			} catch (InterruptedException | MqttException e) {
				e.printStackTrace();
			}
		}
	}
}
