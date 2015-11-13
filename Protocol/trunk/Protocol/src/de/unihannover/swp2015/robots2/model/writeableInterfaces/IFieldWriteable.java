package de.unihannover.swp2015.robots2.model.writeableInterfaces;

import de.unihannover.swp2015.robots2.model.interfaces.*;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IFieldWriteable extends IField, IAbstractModelWriteable {

	/**
	 * 
	 * @param orientation
	 * @param wall
	 */
	public void setWall(IPosition.Orientation orientation, boolean wall);

	/**
	 * 
	 * @param food
	 */
	public void setFood(int food);
	
	public void incrementFood();

	/**
	 * 
	 * @param state
	 */
	public void setState(State state);

	/**
	 * 
	 * @param lockedBy
	 */
	public void setLockedBy(String lockedBy);
	
	public void setGrowingRate(int growingRate);
}
