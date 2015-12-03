package de.unihannover.swp2015.robots2.controller.interfaces;

/**
 * This enumeration contains the possible types of info messages.
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public enum InfoType {
	DEBUG("debug"), INFO("info"), WARNING("warning"), ERROR("error");
	

	private final String mqttName;
	
	private InfoType (String mqttName) {
		this.mqttName = mqttName;
	}
	
	@Override
	public String toString() {
		return this.mqttName;
	}
}
