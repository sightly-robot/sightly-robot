package de.unihannover.swp2015.robots2.model.writeableInterfaces;

import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.external.implementation.Position.Orientation;
import de.unihannover.swp2015.robots2.model.implementation.Field;

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
	public abstract void setWall( Orientation orientation, boolean wall );
	
	/**
	 * 
	 * @param food
	 */
	public abstract void setFood( int food );
	
	/**
	 * 
	 * @param state
	 */
	public abstract void setState( Field.State state );
	
	/**
	 * 
	 * @param lockedBy
	 */
	public abstract void setLockedBy( String lockedBy );
}
