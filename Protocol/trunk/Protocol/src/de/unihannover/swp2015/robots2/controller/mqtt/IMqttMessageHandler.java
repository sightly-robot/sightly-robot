package de.unihannover.swp2015.robots2.controller.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface IMqttMessageHandler {
	public void handleMqttMessage(String topic, MqttMessage message);
}
