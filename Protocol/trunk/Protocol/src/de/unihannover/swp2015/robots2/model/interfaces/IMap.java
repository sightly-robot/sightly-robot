package de.unihannover.swp2015.robots2.model.interfaces;

import java.util.Vector;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IMap extends IAbstractModel {

	/**
	 * Returns the number of IFields on the x-axis
	 * 
	 * @return
	 */
	public abstract int getWidth();
	
	/**
	 * Returns the number of IFields on the y-axis
	 * 
	 * @return
	 */
	public abstract int getHeight();
	
	/**
	 * Returns all IFields of the Map
	 * 
	 * @return
	 */
	public abstract Vector<Vector<IField>> getFields();
}
