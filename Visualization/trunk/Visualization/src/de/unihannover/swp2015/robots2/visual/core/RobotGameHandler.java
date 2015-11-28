package de.unihannover.swp2015.robots2.visual.core;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;
import de.unihannover.swp2015.robots2.visual.entity.IEntity;
import de.unihannover.swp2015.robots2.visual.entity.Map;
import de.unihannover.swp2015.robots2.visual.entity.Robot;
import de.unihannover.swp2015.robots2.visual.entity.modifier.base.IEntityModifier;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.resource.ResourceHandler;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;

/**
 * It handles all entities, resources and update processes of the RobotGame.
 * 
 * @author Rico Schrage
 */
public class RobotGameHandler implements IGameHandler {

	/**
	 * Contains all modifiers, which are updated every tick.
	 */
	protected final List<IEntityModifier> modifierList;
	
	/**
	 * List of entities managed by this game handler.
	 */
	protected final List<IEntity> entityList;
	
	/**
	 * Root of the model.
	 */
	protected final IGame game;
	
	/**
	 * Contains multiple sets of resources for the game.
	 */
	protected IResourceHandler resourceHandler;
	
	/**
	 * SpriteBatch for rendering textures.
	 */
	protected SpriteBatch spriteBatch;
		
	/**
	 * Main camera
	 */
	protected OrthographicCamera cam;
	
	/**
	 * Settings received via MQTT
	 */
	protected IPreferences prefs;
	
	/**
	 * Construct a new RobotGameHandler and connects this handler (means it will directly observe the model) to the given model <code>game</code>
	 * @param game root of the model
	 * @param resourceHandler {@link IResourceHandler}
	 */
	public RobotGameHandler(final IGame game, final IResourceHandler resourceHandler, final OrthographicCamera cam, final IPreferences prefs) {
		this.modifierList = new ArrayList<>();
		this.entityList = new ArrayList<>();
		this.game = game;
		this.resourceHandler = resourceHandler;
		this.spriteBatch = new SpriteBatch();
		this.spriteBatch.setProjectionMatrix(cam.combined);
		this.cam = cam;
		this.prefs = prefs;
		this.game.observe(this);
		
		this.init();
	}
	
	/**
	 * Will create all entities based on the game object.
	 */
	private void init() {
		final IStage stage = game.getStage();

		//set preferences !have to happen before creating entities!
		this.prefs.putInt(PreferencesConstants.WALL_THICK_KEY, 10);
		this.prefs.putInt(PreferencesConstants.MAP_ROWS_KEY, stage.getWidth());
		this.prefs.putInt(PreferencesConstants.MAP_COLS_KEY, stage.getHeight());
		this.prefs.putFloat(PreferencesConstants.FIELD_WIDTH_KEY, ((float) Gdx.graphics.getWidth()) / stage.getWidth());
		this.prefs.putFloat(PreferencesConstants.FIELD_HEIGHT_KEY, ((float) Gdx.graphics.getHeight()) / stage.getHeight());

		//create entites
		for (final IRobot robot : game.getRobots().values()) {
			this.entityList.add(new Robot(robot, spriteBatch, this, prefs, resourceHandler));
		}
		this.entityList.add(new Map(stage, spriteBatch, this, prefs, resourceHandler));

	}
	
	@Override
	public void update() {
		//TODO modifier
	}
	
	@Override
	public void render() {
		for (int i = 0; i < entityList.size(); ++i) {
			this.entityList.get(i).render();
		}
	}

	@Override
	public void onModelUpdate(final IEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setResourceHandler(final ResourceHandler resourceHandler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispatchEvent(final IEvent event, final IEntity source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		this.spriteBatch.dispose();
		this.resourceHandler.dispose();
	}

	@Override
	public IPreferences getPreferences() {
		return prefs;
	}

	@Override
	public void resize(int width, int height) {
	}

}
