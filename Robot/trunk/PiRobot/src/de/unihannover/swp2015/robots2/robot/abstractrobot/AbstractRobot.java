package de.unihannover.swp2015.robots2.robot.abstractrobot;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;

import de.unihannover.swp2015.robots2.ai.core.AI;
import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.controller.main.RobotMainController;
import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.robot.abstractrobot.automate.AbstractAutomate;
import de.unihannover.swp2015.robots2.robot.interfaces.AbstractAI;

/**
 * The AbstractRobot is the base class for all hardware (real) and software
 * (virtual) robots.
 * 
 * @author Lenard Spiecker
 * @author Philipp Rohde
 */
public abstract class AbstractRobot {

	/** LOGGER: */
	private static Logger LOGGER = LogManager.getLogger(AbstractRobot.class.getName());

	/** The controller of the robot. */
	protected IRobotController robotController;

	/** The AI of the robot. */
	protected AbstractAI ai;

	/** The automate of the robot. */
	protected AbstractAutomate automate;

	/**
	 * Initializes the AbstractRobot instance by initializing the robot
	 * controller and AI.
	 */
	public AbstractRobot(boolean isHardware, String brokerIP) {

		robotController = new RobotMainController(isHardware);

		LOGGER.info("My ID: " + robotController.getMyself().getId());

		if (brokerIP == null) {
			LOGGER.info("Try loading BrokerIP from Properties");
			// read broker IP from properties
			Properties properties = new Properties();
			BufferedInputStream is;
			try {
				is = new BufferedInputStream(new FileInputStream("../config.properties"));
				properties.load(is);
				is.close();

			} catch (FileNotFoundException fnfe) {
				fnfe.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			brokerIP = properties.getProperty("brokerIP");
		}
		LOGGER.info("Loaded IP: " + brokerIP);

		LOGGER.info("Starting MQTT-Client");
		while (!robotController.getGame().isSynced()) {
			try {
				robotController.startMqtt("tcp://" + brokerIP);
			} catch (Exception e) {
				LOGGER.error("Start MQTT",e);
			}
		}
		LOGGER.info("MQTT-Client successfully connected!");

		robotController.getGame().observe(new IModelObserver() {
			@Override
			public void onModelUpdate(IEvent event) {
				switch (event.getType()) {
				case ROBOT_DELETE:
					if (event.getObject() == robotController.getMyself()) {
						LOGGER.info("ROBOT_DELETE event received");
						LOGGER.warn("Shutt down JVM NOW!");
						System.exit(0);
					}
					break;
				default:
					break;
				}
			}
		});
		/*
		 * while(!robotController.startMqtt("tcp://192.168.1.66")) { try {
		 * Thread.sleep(1000); } catch (InterruptedException e) {
		 * e.printStackTrace(); } }
		 */

		// IModelObserver mo = new IModelObserver() {
		// @Override
		// public void onModelUpdate(IEvent event) {
		// System.out.println(event.getType().name());
		// }
		// };
		//
		// robotController.getGame().observe(mo);
		// robotController.getGame().getStage().observe(mo);

		// TODO Init AbstractAI
		ai = new AI(robotController);
		ai.setRelativeSpeed(1, 1, 1);

		LOGGER.info("AI initialized!");
	}
}
