package de.unihannover.swp2015.robots2.hardwarerobot.automate;

import java.awt.Color;

import de.unihannover.swp2015.robots2.abstractrobot.automate.AbstractAutomate;
import de.unihannover.swp2015.robots2.controller.externalInterfaces.IHardwareRobot;
import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller.BlinkLEDAndServoController;
import de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller.LEDAndServoController;

/**
 * The HardwareAutomate extends the {@link AbstractAutomate} for controlling a hardware
 * robot (Pi2Go) using the {@link HardwareState}s.
 * 
 * @author Philipp Rohde
 */
public class HardwareAutomate extends AbstractAutomate implements IHardwareRobot{

	public HardwareAutomate(IRobotController robotController) {
		super(robotController, HardwareState.WAIT);
	}

	@Override
	public void setSettings(String settings) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void blink(Color color) {
		if(LEDAndServoController.getInstance() instanceof BlinkLEDAndServoController)
		{
			((BlinkLEDAndServoController)LEDAndServoController.getInstance()).startBlinking(robot.getColor());
		}
	}

}
