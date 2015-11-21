package de.unihannover.swp2015.robots2.model.implementation;

import java.util.Vector;
import java.util.List;

import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IAbstractModel;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IAbstractModelWriteable;

/**
 * Basic implementation of the interface IAbstractModelWritable (including
 * IAbstractModel).
 * 
 * Implements the observable part of the custom observer pattern and is meant to
 * be used as parent class for any kind of observable model.
 * 
 * @version 0.2
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public abstract class AbstractModel implements IAbstractModel, IAbstractModelWriteable {
	/** List of observers of this model object. */
	private List<IModelObserver> observers;

	public AbstractModel() {
		this.observers = new Vector<IModelObserver>();
	}

	@Override
	public void observe(IModelObserver observer) {
		this.observers.add(observer);
	}

	@Override
	public void emitEvent(Event.UpdateType type) {
		IEvent event = new Event(type, this);
		for (IModelObserver o : this.observers)
			o.onModelUpdate(event);
	}
}
