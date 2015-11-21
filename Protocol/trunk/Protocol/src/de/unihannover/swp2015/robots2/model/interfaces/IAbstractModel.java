package de.unihannover.swp2015.robots2.model.interfaces;

import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;

/**
 * Abstract interface for every model class.
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public interface IAbstractModel {

	/**
	 * Register the passed IModelObserver into an internal observer list
	 * 
	 * @param observer
	 */
	public void observe(IModelObserver observer);
}