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
	private final Object waitForConnect;

	public MqttSender(BlockingQueue<MqttFullMessage> queue, MqttClient client, Object waitObject) {
		this.queue = queue;
		this.client = client;
		this.waitForConnect = waitObject;
	}

	@Override
	public void run() {
		try {
			while (true) {
				// Take message from queue or wait till one is available
				MqttFullMessage message = this.queue.take();
				
				// Wait until MQTT connected
				synchronized(this.waitForConnect) {
					while(!client.isConnected()) {
						this.waitForConnect.wait();
					}
				}
				
				// (Re)try sending
				while(true) {
					try{
						client.publish(message.getTopic(), message.getMessage());
						break;
					} catch (MqttException e) {
						Thread.sleep(500);
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
