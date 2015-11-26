package de.unihannover.swp2015.robots2.softwarerobot;

import de.unihannover.swp2015.robots2.abstractrobot.AbstractRobot;
import de.unihannover.swp2015.robots2.abstractrobot.automate.Automate;
import de.unihannover.swp2015.robots2.softwarerobot.automate.SoftwareState;

/**
 * HardwareRobot is a child of {@link AbstractRobot}.
 * It uses an Automate without hardware sensors to drive at the virtual map.
 * @author Lenard Spiecker
 *
 */
public class SoftwareRobot extends AbstractRobot{
	
	Automate automate;
	
	public SoftwareRobot() {
		
		super();
		
		automate = new Automate(robotController,SoftwareState.WAIT);
		automate.start();
		
		ai.setAiEventObserver(automate);
	}
}
