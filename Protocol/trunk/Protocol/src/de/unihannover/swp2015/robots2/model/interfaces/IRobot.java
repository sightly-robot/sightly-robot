package de.unihannover.swp2015.robots2.model.interfaces;

import java.awt.Color;

import de.unihannover.swp2015.robots2.model.interfaces.IPosition;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IRobot extends IAbstractModel {
	
	/**
	 * 
	 * @return
	 */
	public String getId();
	
	/**
	 * 
	 * @return
	 */
	public String getName();
	
	/**
	 * 
	 * @return
	 */
	public boolean isHardwareRobot();
	
	/**
	 * 
	 * @return
	 */
	public IPosition getPosition();
	
	/**
	 * 
	 * @return
	 */
	public int getScore();
	
	/**
	 * 
	 * @return
	 */
	public boolean isSetupState();
	
	/**
	 * 
	 * @return
	 */
	public boolean isMyself();
	
	/**
	 * 
	 * @return
	 */
	public Color getColor();
	
	public boolean isErrorState();

}
