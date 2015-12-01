package de.unihannover.swp2015.robots2.hardwarerobot;

import java.awt.Color;

import de.unihannover.swp2015.robots2.abstractrobot.AbstractRobot;
import de.unihannover.swp2015.robots2.controller.main.RobotMainController;
import de.unihannover.swp2015.robots2.core.AI;
import de.unihannover.swp2015.robots2.hardwarerobot.automate.HardwareAutomate;
import de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller.BlinkLEDAndServoController;
import de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller.LEDAndServoController;
import de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller.MotorController;
import de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller.Pi2GoGPIOController;

/**
 * The HardwareRobot extends {@link AbstractRobot} to control the Pi2Go.<br>
 * It uses an automate and hardware sensors on the Pi2Go to drive.
 * 
 * @author Lenard Spiecker
 * @author Philipp Rohde
 */
public class HardwareRobot extends AbstractRobot {

	/**
	 * Initializes the sensor controllers, automate and AI.
	 */
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

	/**
	 * Blinks once using the color of the robot.
	 */
	private void blinkOnce() {
		LEDAndServoController.getInstance().setAllLEDs(robotController.getMyself().getColor());
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LEDAndServoController.getInstance().setAllLEDs(Color.BLACK);
	}
}
