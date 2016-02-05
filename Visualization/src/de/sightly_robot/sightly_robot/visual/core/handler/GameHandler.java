package de.sightly_robot.sightly_robot.visual.core.handler;

import com.badlogic.gdx.Gdx;

import de.sightly_robot.sightly_robot.model.interfaces.IEvent;
import de.sightly_robot.sightly_robot.model.interfaces.IGame;
import de.sightly_robot.sightly_robot.visual.core.PrefKey;
import de.sightly_robot.sightly_robot.visual.resource.IResourceHandler;
import de.sightly_robot.sightly_robot.visual.util.pref.IPreferences;

/**
 * Provides implementation of the trivial methods in {@link IGameHandler} and
 * implements {@link #onModelUpdate(IEvent)} to make
 * {@link #onManagedModelUpdate(IEvent)} run in the render thread.
 * 
 * @author Rico Schrage
 */
public abstract class GameHandler implements IGameHandler {

	/** contains multiple sets of resources for the game */
	protected IResourceHandler resHandler;

	/** settings received via MQTT and internal non-persistent settings */
	protected IPreferences<PrefKey> prefs;

	/** root of the game */
	protected IGame game;

	/**
	 * Constructs GameHandler.
	 * 
	 * @param game
	 *            root of the game
	 * @param resHandler
	 *            handler which manages <b>all</b> resources, the gameHandler
	 *            will use
	 * @param prefs
	 *            holds and manages preferences
	 */
	public GameHandler(final IGame game, final IResourceHandler resHandler,
			final IPreferences<PrefKey> prefs) {
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
