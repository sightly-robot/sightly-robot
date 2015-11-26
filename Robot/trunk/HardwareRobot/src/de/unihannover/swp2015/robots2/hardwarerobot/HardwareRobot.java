package de.unihannover.swp2015.robots2.hardwarerobot;

import java.awt.Color;

import de.unihannover.swp2015.robots2.abstractrobot.AbstractRobot;
import de.unihannover.swp2015.robots2.abstractrobot.automate.Automate;
import de.unihannover.swp2015.robots2.hardwarerobot.automate.HardwareAutomate;
import de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller.LEDAndServoController;
import de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller.MotorController;
import de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller.Pi2GoGPIOController;

/**
 * HardwareRobot is a child of {@link AbstractRobot}.<br>
 * It uses an automate and hardware sensors on the Pi2Go to drive on a real map
 * and on a virtual map.
 * 
 * @author Lenard Spiecker
 * @author Philipp Rohde
 */
public class HardwareRobot extends AbstractRobot {

	Automate automate;

	public HardwareRobot() {
		super();

		// PreInitialize Controller Instances:
		LEDAndServoController.getInstance();
		Pi2GoGPIOController.getInstance();
		MotorController.getInstance();
		// SoundController.getInstance();
		// ColorSensorController.getInstance();
		// CompassController.getInstance();

		LEDAndServoController.getInstance().setAllLEDs(Color.CYAN);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LEDAndServoController.getInstance().setAllLEDs(Color.BLACK);

		automate = new HardwareAutomate(robotController);
		automate.start();

		ai.setAiEventObserver(automate);
	}
}
