package de.unihannover.swp2015.robots2.softwarerobot.automate;

import de.unihannover.swp2015.robots2.abstractrobot.automate.AbstractAutomate;
import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;

/**
 * Sample SoftwareAutomate as child of {@link AbstractAutomate}.
 * 
 * @author Lenard Spiecker
 */
public class SoftwareAutomate extends AbstractAutomate {

	public SoftwareAutomate(IRobotController robotController) {
		super(robotController, SoftwareState.WAIT);
	}

}
