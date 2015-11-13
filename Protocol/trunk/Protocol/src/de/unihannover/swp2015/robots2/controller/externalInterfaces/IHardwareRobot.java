package de.unihannover.swp2015.robots2.controller.externalInterfaces;

import java.awt.Color;

public interface IHardwareRobot {

	public void setSettings(String settings);
	
	public String getSettings();
	
	public void blink(Color color);
	
}