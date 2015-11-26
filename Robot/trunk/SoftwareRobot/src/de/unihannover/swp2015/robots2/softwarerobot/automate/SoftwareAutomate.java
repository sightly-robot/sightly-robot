package de.unihannover.swp2015.robots2.softwarerobot.automate;

import de.unihannover.swp2015.robots2.abstractrobot.automate.Automate;
import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;

/**
 * Sample SoftwareAutomate as child of Automate. Not used yet.
 * @author Lenard Spiecker
 *
 */
public class SoftwareAutomate extends Automate {

	public SoftwareAutomate(IRobotController robotController) {
		super(robotController,SoftwareState.WAIT);
	}

}
