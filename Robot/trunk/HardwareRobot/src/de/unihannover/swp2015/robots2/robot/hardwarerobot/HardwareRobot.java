package de.unihannover.swp2015.robots2.robot.hardwarerobot;

import java.awt.Color;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import de.unihannover.swp2015.robots2.controller.externalInterfaces.IHardwareRobot;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.robot.abstractrobot.AbstractRobot;
import de.unihannover.swp2015.robots2.robot.hardwarerobot.automate.HardwareAutomate;
import de.unihannover.swp2015.robots2.robot.hardwarerobot.pi2gocontroller.BlinkLEDAndServoController;
import de.unihannover.swp2015.robots2.robot.hardwarerobot.pi2gocontroller.LEDAndServoController;
import de.unihannover.swp2015.robots2.robot.hardwarerobot.pi2gocontroller.MotorController;
import de.unihannover.swp2015.robots2.robot.hardwarerobot.pi2gocontroller.Pi2GoGPIOController;
import de.unihannover.swp2015.robots2.robot.interfaces.AiEventObserver;

/**
 * The HardwareRobot extends {@link AbstractRobot} to control the Pi2Go.<br>
 * It uses an automate and hardware sensors on the Pi2Go to drive.
 * 
 * @author Lenard Spiecker
 * @author Philipp Rohde
 */
public class HardwareRobot extends AbstractRobot implements IHardwareRobot {

	/** LOGGER: */
	private static Logger LOGGER = LogManager.getLogger(HardwareRobot.class.getName());
	/**
	 * Initializes the sensor controllers, automate and AI.
	 */
	public HardwareRobot(String brokerIP) {
		super(true,brokerIP);
		// PreInitialize Controller Instances:
		BlinkLEDAndServoController.getInstance().setAccentColor(robotController.getMyself().getColor());;
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

		blink(robotController.getMyself().getColor());
	}
	

	@Override
	public void setSettings(String settings) {
		try {
			String setting[] = settings.split("=");
			switch (setting[0]) {
			case "dir":
				//TODO use own AI with eventhandling...
				if (!automate.nextOrientationEvent(Orientation.valueOf(setting[1]))) {
					automate.nextButOneOrientationEvent(Orientation.valueOf(setting[1]));
				}
				break;
			case "ai":
				if(!Boolean.parseBoolean(setting[1]))
				{
					ai.setAiEventObserver(new AiEventObserver() {			
						@Override
						public boolean nextOrientationEvent(Orientation orientation) {
							return false;
						}
						@Override
						public void nextButOneOrientationEvent(Orientation orientation) {
						}
					});
					//TODO load own AI, controlled via Settings
				}else
				{
					ai.setAiEventObserver(automate);
				}
				break;
			default:
				LOGGER.warn("Setting unknown!");
				break;
			}
		} catch (Exception e) {
			LOGGER.error("Setting not acccepted!", e);
		}
	}

	@Override
	public String getSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void blink(Color color) {
		if (LEDAndServoController.getInstance() instanceof BlinkLEDAndServoController) {
			((BlinkLEDAndServoController) LEDAndServoController.getInstance()).startBlinking(color);
			LOGGER.info("I'm blinking!");
		}else
		{
			LOGGER.warn("Wrong LEDAndServoController! Blink not possible!");
		}
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
					switch (robotController.getMyself().getState()) {
					case SETUPSTATE:
						System.out.println("Robot ready!");
						robotController.setRobotReady();
						break;
					case ENABLED:
						System.out.println("Robot disable!");
						robotController.disableMyself();
						break;	
					default:
						System.out.println("Robot delete!");
						robotController.deleteMyself();
						break;
					}
					
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

