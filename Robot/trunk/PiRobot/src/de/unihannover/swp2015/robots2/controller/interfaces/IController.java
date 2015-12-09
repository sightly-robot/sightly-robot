package de.unihannover.swp2015.robots2.controller.interfaces;

import de.unihannover.swp2015.robots2.model.interfaces.IGame;

/**
 * This interface holds the methods to access the model or send messages over
 * the network.
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IController {

	/**
	 * Start MQTT client of this Controller.
	 * 
	 * @param brokerUrl
	 *            URL of the MQTT broker. Format should be "tcp://IpOrHostName".
	 * @throws Exception
	 *             when an error occurs while connecting to the MQTT broker.
	 */
	public void startMqtt(String brokerUrl) throws Exception;

	/**
	 * Returns a read-only IGame, which holds all important model-classes
	 * 
	 * @return
	 */
	public IGame getGame();

	/**
	 * Broadcasts the given message to an MQTT event-topic for debugging and
	 * logging purposes. The exact MQTT topic will be determined from type and
	 * topic parameters.
	 * 
	 * A message will only be sent if the given type is more important or equal
	 * to the minimum Info Level that was set with setInfoLevel().
	 * 
	 * @param type
	 *            Debuglevel / Type of this event
	 * @param topic
	 *            The topicname, where the message will send.
	 * @param message
	 *            The message itself.
	 */
	public void sendInfoMessage(InfoType type, String topic, String message);

	/**
	 * Set minimum info level for messages to be sent with sendInfoMessage().
	 * 
	 * @param level
	 *            New minimum info type.
	 */
	public void setInfoLevel(InfoType level);
}
