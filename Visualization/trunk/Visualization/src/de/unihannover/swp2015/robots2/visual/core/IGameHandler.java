package de.unihannover.swp2015.robots2.visual.core;

import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.visual.entity.IEntity;
import de.unihannover.swp2015.robots2.visual.resource.ResourceHandler;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;

/**
 * A GameHandler manages/handles a specific group of entities. Therefore it owns the resources, updates all connected entities and manages necessary data.
 * Additionally it observes the root of the model.
 * 
 * @author Rico Schrage
 */
public interface IGameHandler extends IUpdateable, IModelObserver, IRenderable {

	/**
	 * Sets the connected {@link ResourceHandler}.
	 * 
	 * @param resourceHandler contains resources and manages the textures.
	 */
	void setResourceHandler(final ResourceHandler resourceHandler);
	
	/**
	 * Dispatches an event from the model.
	 * 
	 * @param event {@link de.unihannover.swp2015.robots2.model.interfaces.IEvent}
	 */
	void dispatchEvent(final IEvent event, final IEntity source);
	
	/**
	 * Deallocates all native resources.
	 * 
	 * {@link com.badlogic.gdx.ApplicationListener.dispose}
	 */
	void dispose();
	
	/**
	 * {@link IPreferences}
	 * 
	 * @return preferences object
	 */
	IPreferences getPreferences();
	
}
