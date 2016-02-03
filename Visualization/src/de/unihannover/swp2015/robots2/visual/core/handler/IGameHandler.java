package de.unihannover.swp2015.robots2.visual.core.handler;

import com.badlogic.gdx.utils.Disposable;

import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.visual.core.IRenderable;
import de.unihannover.swp2015.robots2.visual.core.IUpdateable;
import de.unihannover.swp2015.robots2.visual.core.PrefKey;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesObserver;

/**
 * A GameHandler manages/handles a specific group of entities.
 * 
 * Therefore it owns the resources, updates all connected entities and manages
 * necessary data. Additionally it observes the root of the model.
 * 
 * @author Rico Schrage
 */
public interface IGameHandler extends IUpdateable, IRenderable, Disposable,
		IModelObserver, IPreferencesObserver<PrefKey> {

	/**
	 * Sets the connected {@link IResourceHandler}.
	 * 
	 * @param resourceHandler
	 *            contains resources and manages the textures
	 */
	void setResourceHandler(final IResourceHandler resourceHandler);

	/**
	 * Gets the connected {@link IResourceHandler}
	 * 
	 * @return resourceHandler contains resources and manages the textures
	 */
	IResourceHandler getResourceHandler();

	/**
	 * {@link IPreferences}
	 * 
	 * @return preferences object
	 */
	IPreferences<PrefKey> getPreferences();

	/**
	 * Defines what happens on resize.
	 * 
	 * Will be called in the {@link com.badlogic.gdx.ApplicationListener}/
	 * {@link com.badlogic.gdx.ApplicationAdapter} or in a subclass of them.
	 * 
	 * @param width
	 *            new width of the window
	 * @param height
	 *            new height of the window
	 */
	void resize(final int width, final int height);

	/**
	 * Will be called from {@link #onModelUpdate(IEvent)}, but runs safely in
	 * the render thread.
	 * 
	 * @param event
	 *            emitted event
	 */
	void onManagedModelUpdate(final IEvent event);

}
