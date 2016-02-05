package de.sightly_robot.sightly_robot.controller.interfaces;

import de.sightly_robot.sightly_robot.model.interfaces.IGame;

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
	 * @throws ProtocolException
	 *             when an error occurs while connecting to the MQTT broker.
	 */
	public void startMqtt(String brokerUrl) throws ProtocolException;

	/**
	 * Returns a read-only IGame, which holds all important model-classes
	 * 
	 * @return
	 */
	public IGame getGame();
}
