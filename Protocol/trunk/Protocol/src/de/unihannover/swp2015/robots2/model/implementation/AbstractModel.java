package de.unihannover.swp2015.robots2.model.implementation;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
public abstract class AbstractModel implements IAbstractModel,
		IAbstractModelWriteable {
	/** List of observers of this model object. */
	private final Set<IModelObserver> observers;
	/** Lock Object to prevent change of observer list while iterating */
	private final ReadWriteLock observerlock;

	public AbstractModel() {
		this.observers = new HashSet<IModelObserver>();
		this.observerlock = new ReentrantReadWriteLock();
	}

	@Override
	public void observe(IModelObserver observer) {
		observerlock.writeLock().lock();
		this.observers.add(observer);
		observerlock.writeLock().unlock();
	}

	@Override
	public void unobserve(IModelObserver observer) {
		observerlock.writeLock().lock();
		this.observers.remove(observer);
		observerlock.writeLock().unlock();
	}

	@Override
	public void emitEvent(Event.UpdateType type) {
		this.emitEvent(type, this);
	}

	@Override
	public void emitEvent(Event.UpdateType type, Object object) {
		IEvent event = new Event(type, object);
		observerlock.readLock().lock();
		for (IModelObserver o : this.observers)
			o.onModelUpdate(event);
		observerlock.readLock().unlock();		
	}
}
