package de.unihannover.swp2015.robots2.controller.mqtt;

public interface IMqttController {
	public void sendMessage(String topic, String message);
}
