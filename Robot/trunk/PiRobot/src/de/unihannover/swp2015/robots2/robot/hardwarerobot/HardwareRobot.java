package de.unihannover.swp2015.robots2.robot.hardwarerobot;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import de.unihannover.swp2015.robots2.controller.externalInterfaces.IHardwareRobot;
import de.unihannover.swp2015.robots2.robot.abstractrobot.AbstractRobot;
import de.unihannover.swp2015.robots2.robot.hardwarerobot.automate.HardwareAutomate;
import de.unihannover.swp2015.robots2.robot.hardwarerobot.pi2gocontroller.BlinkLEDAndServoController;
import de.unihannover.swp2015.robots2.robot.hardwarerobot.pi2gocontroller.MotorController;
import de.unihannover.swp2015.robots2.robot.hardwarerobot.pi2gocontroller.Pi2GoGPIOController;

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
		super(true);
		// PreInitialize Controller Instances:
		BlinkLEDAndServoController.getInstance();
		Pi2GoGPIOController.getInstance();
		MotorController.getInstance();
		// SoundController.getInstance();
		// ColorSensorController.getInstance();
		// CompassController.getInstance();
		
		automate = new HardwareAutomate(robotController);
		automate.start();
		robotController.registerHardwareRobot((IHardwareRobot)automate);
		ai.setAiEventObserver(automate);
		
		initializeGPIOs();

		((HardwareAutomate)automate).blink(robotController.getMyself().getColor());
	}
	
	/**
	 * Initializes GPIO Event-handling.
	 */
	private void initializeGPIOs()
	{
		Pi2GoGPIOController.getInstance().getButton().addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				if(event.getState().isHigh())
				{
					robotController.setRobotReady();
				}
			}
		});
		GpioPinListenerDigital gChangeEvent = new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				((HardwareAutomate)automate).getAutomation().notify();
			}
		};
		Pi2GoGPIOController.getInstance().getLineLeft().addListener(gChangeEvent);
		Pi2GoGPIOController.getInstance().getLineRight().addListener(gChangeEvent);
	}
}

