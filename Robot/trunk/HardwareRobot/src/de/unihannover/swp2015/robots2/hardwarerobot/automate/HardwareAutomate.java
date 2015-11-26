package de.unihannover.swp2015.robots2.hardwarerobot.automate;

import de.unihannover.swp2015.robots2.abstractrobot.automate.Automate;
import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;

/**
 * The HardwareAutomate extends the {@link Automate} for controlling a hardware
 * robot (Pi2Go) using the {@link HardwareState}s.
 * 
 * @author Philipp Rohde
 */
public class HardwareAutomate extends Automate {

	public HardwareAutomate(IRobotController robotController) {
		super(robotController, HardwareState.WAIT);
	}

}
