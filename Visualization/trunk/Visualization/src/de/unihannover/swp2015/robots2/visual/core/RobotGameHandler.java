package de.unihannover.swp2015.robots2.visual.core;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.visual.entity.IEntity;
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
	 * List of renderUnits managed by this game handler.
	 */
	protected final List<IEntity> renderUnits;
	
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
	
	protected IPreferences prefer;
	
	/**
	 * Construct a new RobotGameHandler and connects this handler (means it will directly observe the model) to the given mode <code>game</code>
	 * @param game root of the model
	 * @param resourceHandler {@link IResourceHandler}
	 */
	public RobotGameHandler(final IGame game, final IResourceHandler resourceHandler, final OrthographicCamera cam) {
		this.modifierList = new ArrayList<>();
		this.renderUnits = new ArrayList<>();
		this.game = game;
		this.resourceHandler = resourceHandler;
		this.spriteBatch = new SpriteBatch();
		this.spriteBatch.setProjectionMatrix(cam.combined);
		this.cam = cam;

		//this.game.observe(this);
	}
	
	@Override
	public void update() {
		//TODO modifier
	}
	
	@Override
	public void render() {
		for (int i = 0; i < renderUnits.size(); ++i) {
			this.renderUnits.get(i).render();
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public IPreferences getPreferences() {
		return prefer;
	}

}
