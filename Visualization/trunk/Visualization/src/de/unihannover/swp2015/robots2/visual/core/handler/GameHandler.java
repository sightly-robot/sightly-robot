package de.unihannover.swp2015.robots2.visual.core.handler;

import com.badlogic.gdx.Gdx;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.visual.core.PrefKey;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;

/**
 * Provides implementation of the trivial method in {@link IGameHandler} and implements {@link #onModelUpdate(IEvent)} to make 
 * {@link #onManagedModelUpdate(IEvent)} run in render thread.
 * 
 * @author Rico Schrage
 */
public abstract class GameHandler implements IGameHandler {
	
	/** Contains multiple sets of resources for the game. */
	protected IResourceHandler resHandler;
	
	/** Settings received via MQTT plus internal non-persistent settings. */
	protected IPreferences<PrefKey> prefs;
	
	/** Root of the game. */
	protected IGame game;
	
	/**
	 * Constructs GameHandler.
	 * 
	 * @param resHandler handler, which managed <b>all</b> resources the gameHandler will use
	 * @param prefs hold and manages preferences
	 */
	public GameHandler(final IGame game, final IResourceHandler resHandler, final IPreferences<PrefKey> prefs) {
		this.resHandler = resHandler;
		this.game = game;
		this.game.observe(this);
		this.prefs = prefs;
		this.prefs.addObserver(this);
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
		resHandler = resourceHandler;
	}

	@Override
	public IResourceHandler getResourceHandler() {
		return resHandler;
	}

	@Override
	public IPreferences<PrefKey> getPreferences() {
		return prefs;
	}
	
	@Override
	public void dispose() {
		resHandler.dispose();
	}

}
