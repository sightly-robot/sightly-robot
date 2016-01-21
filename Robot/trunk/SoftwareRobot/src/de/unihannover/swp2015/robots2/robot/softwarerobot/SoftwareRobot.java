package de.unihannover.swp2015.robots2.robot.softwarerobot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.robot.abstractrobot.AbstractRobot;
import de.unihannover.swp2015.robots2.robot.abstractrobot.automate.AbstractAutomate;
import de.unihannover.swp2015.robots2.robot.softwarerobot.automate.SoftwareAutomate;

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
	 * @param brokerIP
	 */
	public SoftwareRobot(String brokerIP,boolean useYetAnotherAi) {
		super(false, brokerIP,useYetAnotherAi);

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
