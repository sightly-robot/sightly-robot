package de.unihannover.swp2015.robots2.model.interfaces;

import de.unihannover.swp2015.robots2.external.interfaces.IModelObserver;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IAbstractModel {
	
	/**
	 * 
	 * @param observer
	 */
	public abstract void observe( IModelObserver observer );
}