package de.unihannover.swp2015.robots2.controller.mqtt;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

/**
 * Worker thread for receiving MQTT messages. Sequentially reads the messages
 * from the given BlockingQueue and calls the callback method of the given
 * message handler.
 * 
 * @author Michael Thies
 */
public class MqttReceiver implements Runnable {
	private final BlockingQueue<MqttFullMessage> queue;
	private final IMqttMessageHandler handler;

	public MqttReceiver(BlockingQueue<MqttFullMessage> queue,
			IMqttMessageHandler handler) {
		this.queue = queue;
		this.handler = handler;
	}

	@Override
	public void run() {
		while (true) {
			try {
				MqttFullMessage message = this.queue.take();
				handler.handleMqttMessage(message.getTopic(), new String(
						message.getMessage().getPayload(),StandardCharsets.UTF_8));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
