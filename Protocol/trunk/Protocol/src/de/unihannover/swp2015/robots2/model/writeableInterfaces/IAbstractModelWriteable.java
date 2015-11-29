package de.unihannover.swp2015.robots2.model.writeableInterfaces;

import de.unihannover.swp2015.robots2.model.implementation.Event;
import de.unihannover.swp2015.robots2.model.interfaces.IAbstractModel;

/**
 * Abstract writable interface for every model class.
 * 
 * @version 0.2
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public interface IAbstractModelWriteable extends IAbstractModel {

	/**
	 * Emit an event on this model object.
	 * 
	 * This will notify all observers of this object about a change within. An
	 * Event object containing the given type and a reference to this object
	 * will be created and given to each observer.
	 * 
	 * @param type
	 *            The type of event - the data, its update should be announced.
	 */
	public void emitEvent(Event.UpdateType type);

	/**
	 * Emit an event from this model object with a reference to another object.
	 * 
	 * This will notify all observers of this object about a change within. An
	 * Event object containing the given type and a reference to another object,
	 * that is given via the object parameter, will be created and given to each
	 * observer.
	 * 
	 * @param type
	 *            The type of event - the data, its update should be announced.
	 * @param object
	 *            Object reference that will be used in the emitted Event.
	 */
	public void emitEvent(Event.UpdateType type, Object object);

}