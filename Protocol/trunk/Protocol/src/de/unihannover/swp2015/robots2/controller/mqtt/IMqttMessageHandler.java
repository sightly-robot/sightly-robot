package de.unihannover.swp2015.robots2.controller.mqtt;

/**
 * Interface for the controller component that may receive messages from the
 * MqttController.
 * 
 * @author Michael Thies
 */
public interface IMqttMessageHandler {
	/**
	 * Handle a new message that was received by the MQTT client.
	 * 
	 * @param topic
	 *            The MQTT topic of the received message
	 * @param message
	 *            The payload of the received MQTT message
	 */
	public void handleMqttMessage(String topic, String message);
}
