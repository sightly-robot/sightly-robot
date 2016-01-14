package de.unihannover.swp2015.robots2.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.controller.interfaces.IServerController;
import de.unihannover.swp2015.robots2.controller.interfaces.ProtocolException;
import de.unihannover.swp2015.robots2.controller.main.ServerMainController;
import de.unihannover.swp2015.robots2.server.farm.Farmer;

public class Main {

	private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

	// Prevent creation of Main object
	private Main() {
	}

	/**
	 * main method for game server.
	 * 
	 * @param args
	 *            Cli arguments
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			LOGGER.error("Wrong number of cli arguments.\n"
					+ "Please specify exactly 1 argument: The MQTT broker location in format\n"
					+ "tcp://HostnameOrIP");
			return;
		}

		IServerController controller = new ServerMainController();
		LOGGER.info("Controller initialized.");

		Harvester harvester = new Harvester(controller);
		Farmer farmer = new Farmer(controller);
		LOGGER.info("Worker Threads started.");

		try {
			controller.startMqtt(args[0]);
			LOGGER.info("Connection to MQTT broker established.");
		} catch (ProtocolException e) {
			LOGGER.error("Could not connect to broker.", e);
			return;
		}

		try {
			while (true) {
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			LOGGER.info("Server is terminating.");
		}
	}

}
