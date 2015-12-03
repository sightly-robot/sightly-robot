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
	 * @param retained
	 *            Send the message as retained message, so the MQTT broker will
	 *            store the value for new connected clients.
	 */
	public void sendMessage(String topic, String message, boolean retained);

	/**
	 * Publish a message with empty payload to the given topic. This will delete
	 * a retained message that was sent previously to the broker using this
	 * topic.
	 * 
	 * @param topic
	 *            MQTT topic to delete the retained message from
	 */
	public void deleteRetainedMessage(String topic);
}