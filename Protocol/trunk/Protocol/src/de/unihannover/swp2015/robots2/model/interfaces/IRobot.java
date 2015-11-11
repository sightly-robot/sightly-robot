package de.unihannover.swp2015.robots2.model.interfaces;

import java.awt.Color;

import de.unihannover.swp2015.robots2.external.interfaces.IPosition;

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
	public abstract String getId();
	
	/**
	 * 
	 * @return
	 */
	public abstract String getName();
	
	/**
	 * 
	 * @return
	 */
	public abstract boolean isHardwareRobot();
	
	/**
	 * 
	 * @return
	 */
	public abstract IPosition getPosition();
	
	/**
	 * 
	 * @return
	 */
	public abstract int getScore();
	
	/**
	 * 
	 * @return
	 */
	public abstract boolean isSetupState();
	
	/**
	 * 
	 * @return
	 */
	public abstract boolean isMyself();
	
	/**
	 * 
	 * @return
	 */
	public abstract Color getColor();

}
