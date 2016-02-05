package de.sightly_robot.sightly_robot.visual.core.entity;

import de.sightly_robot.sightly_robot.model.interfaces.IEvent;
import de.sightly_robot.sightly_robot.visual.core.IDrawable;
import de.sightly_robot.sightly_robot.visual.core.IUpdateable;

/**
 * Describes a component of an entity.
 * 
 * Components can handle update processes or event render something. <br>
 * They should be used to extend the functionality of an entity.
 * 
 * @author Rico Schrage
 */
public interface IComponent<T extends IEntity> extends IUpdateable, IDrawable {

	/**
	 * Will be called when the component gets registered.
	 * 
	 * @param entity
	 *            entity which registered the component
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
