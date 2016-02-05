package de.sightly_robot.sightly_robot.controller.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Provides a simple interface for the MqttController to send MQTT messages.
 * 
 * @author Michael Thies
 */
public interface IMqttController {
	/**
	 * Send an MQTT message with the given payload to the given MQTT topic. The
	 * message will be converted to an UTF-8 encoded byte array.
	 * 
	 * @param topic
	 *            The MQTT topic to send the message to
	 * @param message
	 *            The payload of the MQTT message as String. If null, a message
	 *            with zero length payload will be sent (which will delete
	 *            retained messages of the given topic).
	 * @param retained
	 *            Send the message as retained message, so the MQTT broker will
	 *            store the value for new connected clients.
	 */
	public void sendMessage(String topic, String message, boolean retained);

	/**
	 * Try to connect to the MQTT broker at the given URL.
	 * 
	 * After connection error the MQTTController will try to reconnect by
	 * itself. Each following call of this function after a successfull connect
	 * will disconnect the MQTT client and connect to a new broker.
	 * 
	 * @param brokerUrl
	 *            hostname or URL of the MQTT broker. Format examples:
	 *            tcp://hostname ssl://127.0.0.1
	 * @throws MqttException
	 *             when the connect attempt fails.
	 */
	public void connect(String brokerUrl) throws MqttException;
}
