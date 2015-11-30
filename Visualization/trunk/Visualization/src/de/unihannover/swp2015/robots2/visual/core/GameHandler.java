package de.unihannover.swp2015.robots2.visual.core;

import com.badlogic.gdx.Gdx;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;

public abstract class GameHandler implements IGameHandler {
	
	/**
	 * Contains multiple sets of resources for the game.
	 */
	protected IResourceHandler resHandler;
	
	/**
	 * Settings received via MQTT plus internal non-persistent settings.
	 */
	protected IPreferences prefs;
	
	public GameHandler(final IResourceHandler resHandler, final IPreferences prefs) {
		this.resHandler = resHandler;
		this.prefs = prefs;
	}
	
	@Override
	public void onModelUpdate(final IEvent event) {
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				GameHandler.this.onManagedModelUpdate(event);
			}
		});
	}
	
	@Override
	public void setResourceHandler(IResourceHandler resourceHandler) {
		this.resHandler = resourceHandler;
	}

	@Override
	public IResourceHandler getResourceHandler() {
		return resHandler;
	}

	@Override
	public IPreferences getPreferences() {
		return prefs;
	}
	
	@Override
	public void dispose() {
		this.resHandler.dispose();
	}

}
