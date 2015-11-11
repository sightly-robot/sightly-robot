package de.unihannover.swp2015.robots2.model.writeableInterfaces;

import java.awt.Color;

import de.unihannover.swp2015.robots2.external.implementation.Position.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 *
 */
public interface IRobotWriteable extends IRobot, IAbstractModelWriteable {
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param orientation
	 */
	public abstract void setPosition(int x, int y, Orientation orientation);
	
	/**
	 * 
	 * @param name
	 */
	public abstract void setName(String name);
	
	/**
	 * 
	 * @param score
	 */
	public abstract void setScore(int score);
	
	/**
	 * 
	 * @param setupState
	 */
	public abstract void setSetupState(boolean setupState);
	
	/**
	 * 
	 * @param color
	 */
	public abstract void setColor(Color color);
	
}
