package de.unihannover.swp2015.robots2.hardwarerobot.automate;

import de.unihannover.swp2015.robots2.abstractrobot.automate.Automate;
import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;

public class HardwareAutomate extends Automate {

	public HardwareAutomate(IRobotController robotController) {
		super(robotController, HardwareState.WAIT);
	}

}
