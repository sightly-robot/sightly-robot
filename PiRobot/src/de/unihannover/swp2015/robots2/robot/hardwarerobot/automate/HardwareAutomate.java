package de.unihannover.swp2015.robots2.robot.hardwarerobot.automate;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.robot.abstractrobot.automate.AbstractAutomate;

/**
 * The HardwareAutomate extends the {@link AbstractAutomate} for controlling a
 * hardware robot (Pi2Go) using the {@link HardwareState}s.
 * 
 * @author Philipp Rohde
 */
public class HardwareAutomate extends AbstractAutomate implements GpioPinListenerDigital {

	/** LOGGER: */
	// private static Logger LOGGER =
	// LogManager.getLogger(HardwareAutomate.class.getName());

	/**
	 * Constructs a new automate for a hardware robot.
	 * 
	 * @param robotController
	 *            the controller of the robot
	 */
	public HardwareAutomate(IRobotController robotController) {
		super(robotController, HardwareStateV2.WAIT, HardwareStateV2.SETUP, HardwareStateV2.DISABLED,
				HardwareStateV2.CONNECTED);
	}

	@Override
	public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent evt) {
		synchronized (getAutomation()) {
			getAutomation().notify();
		}
	}
}
