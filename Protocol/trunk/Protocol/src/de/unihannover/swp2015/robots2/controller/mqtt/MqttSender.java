package de.unihannover.swp2015.robots2.controller.mqtt;

import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

	private Logger log = LogManager.getLogger(this.getClass().getName());

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
				log.trace("Sending message of Topic \"{}\".",message.getTopic());
				
				// Wait until MQTT connected
				synchronized(this.waitForConnect) {
					while(!client.isConnected()) {
						log.debug("MQTT client not connected. Waiting to send until client is connected.");
						this.waitForConnect.wait();
					}
				}
				
				// (Re)try sending
				while(true) {
					try{
						client.publish(message.getTopic(), message.getMessage());
						break;
					} catch (MqttException e) {
						log.debug("Sending message failed. Retrying in 500ms.",e);
						Thread.sleep(500);
					}
				}
			}
		} catch (InterruptedException e) {
			log.info("MQTT Send worker aborted by interrupt exception.",e);
		}
	}
}
