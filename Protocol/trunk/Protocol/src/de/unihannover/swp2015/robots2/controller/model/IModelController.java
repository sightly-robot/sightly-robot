package de.unihannover.swp2015.robots2.controller.model;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IModelController {

	/**
	 * 
	 * @param topic
	 * @param message
	 */
	public void handleMqttMessage(String topic, String message);
}
