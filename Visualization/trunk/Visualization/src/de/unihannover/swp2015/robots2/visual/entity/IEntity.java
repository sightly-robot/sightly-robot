package de.unihannover.swp2015.robots2.visual.entity;

import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IAbstractModel;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.visual.core.base.IDrawable;
import de.unihannover.swp2015.robots2.visual.core.base.IUpdateable;
import de.unihannover.swp2015.robots2.visual.entity.modifier.base.IEntityModifier;
import de.unihannover.swp2015.robots2.visual.util.pref.observer.IPreferencesObserver;

public interface IEntity extends IDrawable, IModelObserver, Comparable<IEntity>, IPreferencesObserver, IUpdateable {

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
}
