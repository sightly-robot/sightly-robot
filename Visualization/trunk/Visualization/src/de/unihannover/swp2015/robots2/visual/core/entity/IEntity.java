package de.unihannover.swp2015.robots2.visual.core.entity;

import com.badlogic.gdx.graphics.Color;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.visual.core.IDrawable;
import de.unihannover.swp2015.robots2.visual.core.IUpdateable;

/**
 * Describes a self updating/drawing entity, which will be based on a model
 * class defined by the protocol.
 * 
 * @author Rico Schrage
 */
public interface IEntity extends IDrawable, Comparable<IEntity>, IUpdateable {

	/**
	 * @return the position on the z axis
	 */
	int getZIndex();

	/**
	 * Sets the position on the z axis.
	 * 
	 * @param zIndex
	 *            new position on the z axis
	 */
	void setZIndex(final int zIndex);

	/**
	 * Sets the rendering position of the entity.
	 * 
	 * @param x
	 *            x position on the virtual screen
	 * @param y
	 *            y position on the virtual screen
	 */
	void setPosition(final float x, final float y);

	/**
	 * @return x position of the entity
	 */
	float getPositionX();

	/**
	 * @return y position of the entity
	 */
	float getPositionY();

	/**
	 * @return width of the entity
	 */
	float getWidth();

	/**
	 * @return height of the entity
	 */
	float getHeight();

	/**
	 * Sets the rotation of the entity.
	 * 
	 * @param rot
	 *            rotation in degrees.
	 */
	void setRotation(final float rot);

	/**
	 * @return rotation in degrees
	 */
	float getRotation();

	/**
	 * Will be called when an event happens in the model.
	 * 
	 * In opposite to {@link #onModelUpdate(IEvent)} this method will be called
	 * in the render thread.
	 * 
	 * @param event
	 *            {@link IEvent}
	 */
	void onManagedModelUpdate(final IEvent event);

	/**
	 * Registers an entity modifier, which updates the entity in a specific way.
	 * 
	 * @param mod
	 *            {@link IEntityModifier}
	 */
	void registerModifier(final IEntityModifier mod);

	/**
	 * Removes all modifiers.
	 */
	void clearModifier();

	/**
	 * Removes all modifiers of the given type.
	 * 
	 * @param type
	 *            class of the modifiers you want to remove
	 */
	void clearModifier(Class<? extends IEntityModifier> type);

	/**
	 * Clears all references of this entity, which were hold by observables.
	 */
	void clearReferences();

	/**
	 * Registers a component, which can update the entity or even act as
	 * autonomic unit and draw something by itself.
	 * 
	 * @param component
	 *            {@link IComponent}
	 */
	<T extends IEntity> void registerComponent(IComponent<T> component);

	/**
	 * Unregisters a component.
	 * 
	 * @param component
	 *            {@link IComponent}
	 */
	<T extends IEntity> void unregisterComponent(IComponent<T> component);

	/**
	 * Set the color for this entity.
	 * 
	 * @param color
	 *            color
	 */
	void setColor(Color color);

	/**
	 * @return current color of the entity
	 */
	Color getColor();
}
