package de.unihannover.swp2015.robots2.hardwarerobot;

import java.awt.Color;

import de.unihannover.swp2015.robots2.abstractrobot.AbstractRobot;
import de.unihannover.swp2015.robots2.abstractrobot.automate.AbstractAutomate;
import de.unihannover.swp2015.robots2.controller.main.RobotMainController;
import de.unihannover.swp2015.robots2.core.AI;
import de.unihannover.swp2015.robots2.hardwarerobot.automate.HardwareAutomate;
import de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller.BlinkLEDAndServoController;
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

	public HardwareRobot() {
		// PreInitialize Controller Instances:
		BlinkLEDAndServoController.getInstance();
		Pi2GoGPIOController.getInstance();
		MotorController.getInstance();
		// SoundController.getInstance();
		// ColorSensorController.getInstance();
		// CompassController.getInstance();
		
		robotController = new RobotMainController(true);
		robotController.startMqtt("localhost");
		
		blinkOnce();
		
		automate = new HardwareAutomate(robotController);
		automate.start();
		
		ai = new AI(robotController);
		ai.setAiEventObserver(automate);
	}
	
	private void blinkOnce()
	{
		LEDAndServoController.getInstance().setAllLEDs(robotController.getMyself().getColor());
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LEDAndServoController.getInstance().setAllLEDs(Color.BLACK);
	}
}
