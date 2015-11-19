package de.unihannover.swp2015.robots2.controller.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Wraps a MqttMessage and its Topic as String.
 * 
 * @author Michael Thies
 */
public class MqttFullMessage {
	private final String topic;
	private final MqttMessage message;

	public MqttFullMessage(String topic, MqttMessage message) {
		this.topic = topic;
		this.message = message;
	}

	public String getTopic() {
		return topic;
	}

	public MqttMessage getMessage() {
		return message;
	}
}
