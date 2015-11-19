package de.unihannover.swp2015.robots2.controller.mqtt;

/**
 * Provides a simple interface for the MqttController to send MQTT messages.
 * 
 * @author Michael Thies
 */
public interface IMqttController {
	/**
	 * Send an MQTT message with the given payload to the given MQTT topic.
	 * 
	 * @param topic
	 *            The MQTT topic to send the message to
	 * @param message
	 *            The payload of the MQTT message
	 */
	public void sendMessage(String topic, String message);
}
