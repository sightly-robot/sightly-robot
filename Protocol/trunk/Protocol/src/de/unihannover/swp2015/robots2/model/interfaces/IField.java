package de.unihannover.swp2015.robots2.model.interfaces;

import de.unihannover.swp2015.robots2.external.implementation.Position.Orientation;
import de.unihannover.swp2015.robots2.model.implementation.Field;

/**
 * Read-only interface of a Labyrinth-Field
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IField extends IAbstractModel {
	
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
	 * @param orientation
	 * @return 
	 */
	public abstract boolean isWall( Orientation orientation );
	
	/**
	 * 
	 * @return [0-10]
	 */
	public abstract int getFood();
	
	/**
	 * 
	 * @return 
	 */
	public abstract Field.State getState();
	
	/**
	 * Returns the id of the robot, who is locked the field.
	 * 
	 * @return robot-id
	 */
	public abstract String getLockedBy();
}