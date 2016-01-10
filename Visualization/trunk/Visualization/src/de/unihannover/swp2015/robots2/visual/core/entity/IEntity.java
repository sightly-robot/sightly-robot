package de.unihannover.swp2015.robots2.visual.core.entity;

import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IAbstractModel;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.visual.core.IDrawable;
import de.unihannover.swp2015.robots2.visual.core.IUpdateable;
import de.unihannover.swp2015.robots2.visual.core.PrefKey;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesObserver;

public interface IEntity extends IDrawable, IModelObserver, Comparable<IEntity>, IPreferencesObserver<PrefKey>, IUpdateable {

	/**
	 * @return the position on the z-axis.
	 */
	int getZIndex();
	
	/**
	 * Sets the position on the z-axis.
	 * 
	 * @param zIndex new position on the z-axis
	 */
	void setZIndex(final int zIndex);
	
	/**
	 * Sets the position, where the entity should be rendered.
	 * 
	 * @param x x position on the virtual screen
	 * @param y y position on the virtual screen
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
	 * @param rot rotation in degrees.
	 */
	void setRotation(final float rot);
	
	/**
	 * @return rotation in degrees.
	 */
	float getRotation();
	
	/**
	 * @return the model, which will be renderer by the entity.
	 */
	IAbstractModel getModel();
	
	/**
	 * Will be called when an event happens in the model. In opposite to {@link #onModelUpdate(IEvent)} this
	 * method will be called in the render-thread.
	 * 
	 * @param event {@link IEvent}
	 */
	void onManagedModelUpdate(final IEvent event);
	
	/**
	 * Registers an entity modifier, which updates the entity in a specific way.
	 * 
	 * @param mod {@link IEntityModifier}
	 */
	void registerModifier(final IEntityModifier mod);
	
	/**
	 * Removes all modifiers.
	 */
	void clearModifier();
	
	/**
	 * Clears all references, hold by observables, to this entity.
	 */
	void clearReferences();

	/**
	 * Registers a component, which can update the entity or even act as autonomic unit and draw something by itself.
	 * 
	 * @param component {@link IComponent}
	 */
	void registerComponent(IComponent component);

	/**
	 * Unregisters a component.
	 * 
	 * @param component {@link IComponent}
	 */
	void unregisterComponent(IComponent component);
}
