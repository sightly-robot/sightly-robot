package de.sightly_robot.sightly_robot.controller.mqtt;

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

	/**
	 * Handle a MQTT connection state change.
	 * 
	 * This method is called whenever the state of the connection to the MQTT
	 * broker changes.
	 * 
	 * @param connected
	 *            True if the connection is active now.
	 */
	public void onMqttStateChange(boolean connected);
}
