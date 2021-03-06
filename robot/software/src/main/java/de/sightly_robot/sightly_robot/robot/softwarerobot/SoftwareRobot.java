package de.sightly_robot.sightly_robot.robot.softwarerobot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.sightly_robot.sightly_robot.model.externalInterfaces.IModelObserver;
import de.sightly_robot.sightly_robot.model.interfaces.IEvent;
import de.sightly_robot.sightly_robot.robot.abstractrobot.AbstractRobot;
import de.sightly_robot.sightly_robot.robot.abstractrobot.automate.AbstractAutomate;
import de.sightly_robot.sightly_robot.robot.softwarerobot.automate.SoftwareAutomate;

/**
 * SoftwareRobot is a child of {@link AbstractRobot}.<br>
 * It uses an automate without hardware sensors to drive at the virtual map.
 * 
 * @author Lenard Spiecker
 */
public class SoftwareRobot extends AbstractRobot {

	/** LOGGER: */
	private static Logger LOGGER = LogManager.getLogger(SoftwareRobot.class.getName());

	AbstractAutomate automate;

	/**
	 * Software Robot Constructor.
	 * 
	 * Creates a {@link SoftwareAutomate} Instance and Handles SETUP_STATE
	 * Events.
	 * 
	 * @param brokerURL
	 */
	public SoftwareRobot(String brokerURL,boolean useYetAnotherAi) {
		super(false, brokerURL,useYetAnotherAi);

		robotController.getMyself().observe(new IModelObserver() {

			@Override
			public void onModelUpdate(IEvent event) {
				switch (event.getType()) {
				case ROBOT_STATE:
					switch (robotController.getMyself().getState()) {
					case SETUPSTATE:
						LOGGER.info("Robot - READY!");
						robotController.setRobotReady();
						break;
					default:
						break;
					}
					break;
				default:
					break;
				}
			}
		});

		automate = new SoftwareAutomate(robotController);
		automate.start();

		ai.setAiEventObserver(automate);
	}
}
