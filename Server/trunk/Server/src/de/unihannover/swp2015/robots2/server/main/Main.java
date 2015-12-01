package de.unihannover.swp2015.robots2.server.main;

import de.unihannover.swp2015.robots2.controller.interfaces.IServerController;
import de.unihannover.swp2015.robots2.controller.interfaces.InfoType;
import de.unihannover.swp2015.robots2.controller.main.ServerMainController;

public class Main {

	/**
	 * main method for game server.
	 * 
	 * @param args Cli arguments
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Wrong number of cli arguments.");
			System.out.println("Please specify exactly 1 argument: The MQTT broker location in format");
			System.out.println("tcp://HostnameOrIP");
			return;
		}
		
		IServerController controller = new ServerMainController();
		controller.setInfoLevel(InfoType.DEBUG);
		
		Harvester harvester = new Harvester(controller);
		
		if (!controller.startMqtt(args[0])) {
			System.out.println("Connection could not connect to broker.");
			return;
		}
		
		
		
		try {
			while(true) {
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			System.out.println("Server is terminating.");
		}
	}

}
