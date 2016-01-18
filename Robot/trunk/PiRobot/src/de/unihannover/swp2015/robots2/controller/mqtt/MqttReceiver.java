package de.unihannover.swp2015.robots2.controller.mqtt;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

	private Logger log = LogManager.getLogger(MqttReceiver.class.getName());

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
				log.trace("Processing message of Topic:\"{}\"",
						message.getTopic());
				try {
					handler.handleMqttMessage(message.getTopic(), new String(
							message.getMessage().getPayload(),
							StandardCharsets.UTF_8));
				} catch (Exception e) {
					this.log.error(
							"Uncaught Exception while prossessing the following MQTT message: {}\n{}\n{}\n",
							message.getTopic(), new String(message.getMessage()
									.getPayload(), StandardCharsets.UTF_8));
					this.log.error(e);
				}
			} catch (InterruptedException e) {
				log.info("MQTT receive worker aborted by interrupt exception.",e);
			}
		}
	}
}
