package de.unihannover.swp2015.robots2.server.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.controller.interfaces.IServerController;
import de.unihannover.swp2015.robots2.controller.interfaces.InfoType;
import de.unihannover.swp2015.robots2.controller.main.ServerMainController;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttReceiver;
import de.unihannover.swp2015.robots2.server.main.farm.Farmer;

public class Main {
	
	private static Logger log = LogManager.getLogger(Main.class.getName());
	
	/**
	 * main method for game server.
	 * 
	 * @param args Cli arguments
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			log.error("Wrong number of cli arguments.");
			log.error("Please specify exactly 1 argument: The MQTT broker location in format");
			log.error("tcp://HostnameOrIP");
			return;
		}
		
		IServerController controller = new ServerMainController();
		controller.setInfoLevel(InfoType.DEBUG);
		
		Harvester harvester = new Harvester(controller);
		Farmer farmer = new Farmer(controller);
		
		try {
			controller.startMqtt(args[0]);
		} catch (Exception e) {
			log.error("Could not connect to broker.");
			log.error(e.toString());
			return;
		}
		
		
		try {
			while(true) {
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			log.info("Server is terminating.");
		}
	}

}
