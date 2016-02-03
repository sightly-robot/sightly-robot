package de.unihannover.swp2015.robots2.controller.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Helper class that wraps a MqttMessage and its Topic as String.
 * 
 * Used for queuing of messages.
 * 
 * @author Michael Thies
 */
public class MqttFullMessage {
	/** MQTT topic of the message */
	private final String topic;
	/** Contents and flags of the message */
	private final MqttMessage message;

	/**
	 * Creates a mqtt message with the given topic and message.
	 * 
	 * @param topic
	 *            the mqtt-topic. Can contains wildcards like + and #
	 * @param message
	 *            the content of the mqtt-message
	 */
	public MqttFullMessage(String topic, MqttMessage message) {
		this.topic = topic;
		this.message = message;
	}

	/**
	 * Get the MQTT topic of this message.
	 * 
	 * @return The MQTT topic
	 */
	public String getTopic() {
		return topic;
	}

	/**
	 * Get the Message (including payload and other properties).
	 * 
	 * @return The MQTT message
	 */
	public MqttMessage getMessage() {
		return message;
	}
}
