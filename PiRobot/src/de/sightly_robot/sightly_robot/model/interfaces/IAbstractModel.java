package de.sightly_robot.sightly_robot.model.interfaces;

import de.sightly_robot.sightly_robot.model.externalInterfaces.IModelObserver;

/**
 * Abstract interface for every model class.
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public interface IAbstractModel {

	/**
	 * Register the passed IModelObserver in an internal observer set, so it
	 * will be notified via {@link IModelObserver#onModelUpdate(IEvent)} about
	 * updates of this model. An already registered observer will be ignored.
	 * 
	 * @param observer
	 *            The new observer of the model
	 */
	public void observe(IModelObserver observer);

	/**
	 * Unregister an IModelObserver from this model object. This method will
	 * remove the observer from the Set of observers. If it was added before.
	 * 
	 * @param observer
	 *            The observer to remove from this model object
	 */
	public void unobserve(IModelObserver observer);
}