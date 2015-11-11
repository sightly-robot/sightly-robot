package de.unihannover.swp2015.robots2.external.interfaces;

import de.unihannover.swp2015.robots2.external.implementation.Position.Orientation;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IPosition {
	
	/**
	 * 
	 * @return
	 */
	public abstract int getX();
	
	/**
	 * 
	 * @return
	 */
	public abstract int getY();
	
	/**
	 * 
	 * @return
	 */
	public abstract Orientation getOrientation();
	
	/**
	 * 
	 * @return
	 */
	public abstract int getProgress();
}
