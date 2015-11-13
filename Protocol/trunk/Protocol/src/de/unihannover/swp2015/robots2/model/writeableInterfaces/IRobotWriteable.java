package de.unihannover.swp2015.robots2.model.writeableInterfaces;

import de.unihannover.swp2015.robots2.model.interfaces.*;

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
	public void setPosition(int x, int y, IPosition.Orientation orientation);

	/**
	 * 
	 * @param name
	 */
	public void setName(String name);

	/**
	 * 
	 * @param score
	 */
	public int addScore(int score);
	
	public void setScore(int score);

	/**
	 * 
	 * @param setupState
	 */
	public void setSetupState(boolean setupState);

}
