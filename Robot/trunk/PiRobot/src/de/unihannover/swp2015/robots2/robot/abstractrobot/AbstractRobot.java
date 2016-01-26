package de.unihannover.swp2015.robots2.robot.abstractrobot;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.ai.core.AI;
import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.controller.main.RobotMainController;
import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent.UpdateType;
import de.unihannover.swp2015.robots2.robot.abstractrobot.automate.AbstractAutomate;
import de.unihannover.swp2015.robots2.robot.interfaces.AbstractAI;
import de.unihannover.swp2015.robots2.yaai.YetAnotherAi;

/**
 * The AbstractRobot is the base class for all hardware (real) and software
 * (virtual) robots.
 * 
 * @author Lenard Spiecker
 * @author Philipp Rohde
 */
public abstract class AbstractRobot {

	/** The logger. */
	private static final Logger LOGGER = LogManager.getLogger(AbstractRobot.class.getName());

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
	public AbstractRobot(boolean isHardware, String brokerURL, boolean useYetAnotherAi) {

		robotController = new RobotMainController(isHardware);

		LOGGER.info("My ID: " + robotController.getMyself().getId());

		if (brokerURL == null) {
			LOGGER.info("Try loading BrokerIP from Properties");
			// read broker IP from properties
			Properties properties = new Properties();
			BufferedInputStream is;
			try {
				is = new BufferedInputStream(new FileInputStream("../config.properties"));
				properties.load(is);
				is.close();

			} catch (FileNotFoundException fnfe) {
				LOGGER.error("config file not found", fnfe);
			} catch (IOException ioe) {
				LOGGER.error("IOException during loading config file", ioe);
			}
			brokerURL = properties.getProperty("brokerIP");
		}
		LOGGER.info("Loaded IP: " + brokerURL);

		LOGGER.info("Starting MQTT-Client");
		while (!robotController.getGame().isSynced()) {
			try {
				robotController.startMqtt(brokerURL);
			} catch (Exception e) {
				LOGGER.error("Start MQTT", e);
			}
		}
		LOGGER.info("MQTT-Client successfully connected!");

		robotController.getGame().observe(new IModelObserver() {
			@Override
			public void onModelUpdate(IEvent event) {
				if (event.getType().equals(UpdateType.ROBOT_DELETE)
						&& event.getObject() == robotController.getMyself()) {
					LOGGER.info("ROBOT_DELETE event received");
					LOGGER.warn("Shut down Automation now!");
					shutdown();
					//LOGGER.warn("Shut down JVM now!");
					//System.exit(0);
				}
			}
		});

		// init AI
		if (useYetAnotherAi)
			ai = new YetAnotherAi(robotController);
		else
			ai = new AI(robotController);
		ai.setRelativeSpeed(1, 1, 1);

		LOGGER.info("AI initialized!");
	}
	
	protected void shutdown()
	{
		automate.shutdown();
	}
}
