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
	 * Returns a read-only IGame, which holds all important model-classes
	 * 
	 * @return
	 */
	public IGame getGame();

	/**
	 * Boardcasts the given mqtt-message to the network
	 * 
	 * @param topic
	 *            The topicname, where the message will send.
	 * @param message
	 *            The message itself.
	 */
	public void sendInfoMessage(String topic, String message);
}
