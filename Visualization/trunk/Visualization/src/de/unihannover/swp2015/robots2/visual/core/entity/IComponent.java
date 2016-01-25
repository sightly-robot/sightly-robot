package de.unihannover.swp2015.robots2.visual.core.entity;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.visual.core.IDrawable;
import de.unihannover.swp2015.robots2.visual.core.IUpdateable;

/**
 * Describes a component of an entity. Components can handle update processes or
 * event render something. <br>
 * They should be used to extends the functionality of an entity.
 * 
 * @author Rico Schrage
 */
public interface IComponent<T extends IEntity> extends IUpdateable, IDrawable {

	/**
	 * Will be called when the component gets registered.
	 * 
	 * @param entity
	 *            entity, which registered the component
	 */
	void onRegister(T entity);

	/**
	 * Will be called when the parent receives an event.
	 * 
	 * @param event
	 *            type of the event
	 */
	void onEvent(IEvent event);

}
