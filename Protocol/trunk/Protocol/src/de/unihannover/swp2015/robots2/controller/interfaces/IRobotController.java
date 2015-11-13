package de.unihannover.swp2015.robots2.controller.interfaces;

import de.unihannover.swp2015.robots2.model.interfaces.*;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IRobotController extends IController {

	/**
	 * 
	 * @param x
	 * @param y
	 * @param orientation
	 */
	public void updatePosition(int x, int y, IPosition.Orientation orientation);
	
	/**
	 * 
	 * @param progress
	 */
	public void updatePositionProgress(int progress);
	
	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void requestField(int x, int y);
	
	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void releaseField(int x, int y);
}
