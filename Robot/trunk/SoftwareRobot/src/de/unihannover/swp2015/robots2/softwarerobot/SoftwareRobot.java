package de.unihannover.swp2015.robots2.softwarerobot;

import de.unihannover.swp2015.robots2.abstractrobot.AbstractRobot;
import de.unihannover.swp2015.robots2.abstractrobot.automate.AbstractAutomate;
import de.unihannover.swp2015.robots2.controller.main.RobotMainController;
import de.unihannover.swp2015.robots2.core.AI;
import de.unihannover.swp2015.robots2.softwarerobot.automate.SoftwareAutomate;

/**
 * SoftwareRobot is a child of {@link AbstractRobot}.<br>
 * It uses an automate without hardware sensors to drive at the virtual map.
 * 
 * @author Lenard Spiecker
 */
public class SoftwareRobot extends AbstractRobot{
	
	AbstractAutomate automate;
	
	public SoftwareRobot() {

			robotController = new RobotMainController(false);
			robotController.startMqtt("localhost");
			
			automate = new SoftwareAutomate(robotController);
			automate.start();
			
			ai = new AI(robotController);
			ai.setAiEventObserver(automate);
		}
}
