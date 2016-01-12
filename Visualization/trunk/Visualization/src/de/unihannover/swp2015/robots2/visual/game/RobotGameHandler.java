package de.unihannover.swp2015.robots2.visual.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;
import de.unihannover.swp2015.robots2.visual.core.PrefKey;
import de.unihannover.swp2015.robots2.visual.core.entity.Entity;
import de.unihannover.swp2015.robots2.visual.core.entity.IEntity;
import de.unihannover.swp2015.robots2.visual.core.handler.GameHandler;
import de.unihannover.swp2015.robots2.visual.game.entity.Map;
import de.unihannover.swp2015.robots2.visual.game.entity.Robot;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.util.SortUtil;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;

/**
 * It handles all entities, resources and update processes of the RobotGame.
 * 
 * @author Rico Schrage
 */
public class RobotGameHandler extends GameHandler {
		
	/** List of entities managed by this game handler.*/
	protected final List<IEntity> entityList;
	
	/** Viewport of the game. */
	protected Viewport view;
	
	/** SpriteBatch for rendering textures. */
	protected SpriteBatch spriteBatch;

	/** UI, which will be displayed if {@link IGame#isRunning()} == false */
	protected UI ui;
			
	/** Overview of robots for ranking */
	private final List<IRobot> robots;
	
	/** Manages several post-processors. */
	private final PostProcessHandler pp;

	/**
	 * Construct a new RobotGameHandler and connects this handler (means it will directly observe the model) to the given model <code>game</code>
	 * 
	 * @param game root of the model
	 * @param resourceHandler {@link IResourceHandler}
	 */
	public RobotGameHandler(IGame robotGame, Viewport viewport, IResourceHandler resourceHandler, IPreferences<PrefKey> prefs) {
		super(robotGame, resourceHandler, prefs);
		
		this.robots = new ArrayList<>();
		this.entityList = new ArrayList<>();
		this.view = viewport;
		this.spriteBatch = new SpriteBatch();
		this.spriteBatch.setProjectionMatrix(view.getCamera().combined);
		this.pp = new PostProcessHandler((int)view.getWorldWidth(), (int)view.getWorldHeight());
		
		this.init();
	}
	
	/**
	 * Will create all entities based on the game object.
	 */
	private void init() {
		final IStage stage = game.getStage();
		
		prefs.putFloat(PrefKey.FIELD_WIDTH_KEY, view.getWorldWidth() / stage.getWidth());
		prefs.putFloat(PrefKey.FIELD_HEIGHT_KEY, view.getWorldWidth() / stage.getHeight());
		
		for (final IRobot roboModel : game.getRobots().values()) {
			final Robot robo = new Robot(roboModel, this);
			robo.setZIndex(1);
			entityList.add(robo);
			robots.add(roboModel);
		}
		entityList.add(new Map(stage, this));
		Entity.sortEntities(entityList);
		
		ui = new UI(robots, this);
	}
	
	/**
	 * Returns the current ranking of the given robot
	 * 
	 * @param robot you want the ranking for
	 */
	public int getRanking(final IRobot robo) {
		SortUtil.sortRobots(robots);
		
		for (int i = 0; i < robots.size(); ++i) {
			if (robots.get(i) == robo)
				return i+1;
		}
		return -1;
	}
	
	@Override
	public void update() {
		for (int i = 0; i < entityList.size(); ++i) {
			entityList.get(i).update();
		}
	}
			
	@Override
	public void render() {
		
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			resHandler.loadTexturePack("default");
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			resHandler.loadTexturePack("earth");
		if (Gdx.input.isKeyPressed(Keys.UP))
			resHandler.loadTexturePack("home");
		
		if (!game.isRunning()) {

			if (pp.isBloomEnabled()) {
				//approx. 50% slower than the other branch.
				pp.captureBloom();
				
				spriteBatch.begin();
				for (int i = 0; i < entityList.size(); ++i) {
					entityList.get(i).draw(spriteBatch);
				}
				spriteBatch.end();
				
				pp.renderBloomToInternalBuffer();
				pp.captureFxaa();
				
				spriteBatch.begin();
				spriteBatch.draw(pp.getBufferTexture(), 0, 0);
				ui.draw(spriteBatch);
				spriteBatch.end();
				
				pp.renderFxaa();
			}
			else {
				pp.captureFxaa();
				
				spriteBatch.begin();
				for (int i = 0; i < entityList.size(); ++i) {
					entityList.get(i).draw(spriteBatch);
				}

				ui.draw(spriteBatch);
				spriteBatch.end();

				pp.renderFxaa();
			}
		}
		else {
			pp.captureFxaa();
			spriteBatch.begin();
			for (int i = 0; i < entityList.size(); ++i) {
				entityList.get(i).draw(spriteBatch);
			}
			spriteBatch.end();
			pp.renderFxaa();
		}
	}

	@Override
	public void onManagedModelUpdate(IEvent event) {

		switch(event.getType()) {
		case GAME_PARAMETER:
			break;
			
		case GAME_STATE:
			pp.setBloomEnabled(!game.isRunning());
			ui.setDisplay(true);
			break;
			
		case ROBOT_ADD:
			if (event.getObject() instanceof IRobot) {
				final IRobot robot = (IRobot) event.getObject();
				final Robot roboEntity = new Robot(robot, this);
				roboEntity.setZIndex(1);
				Entity.addEntitySorted(roboEntity, entityList);
				SortUtil.addRobotSorted(robot, robots);
			}
			break;
			
		case ROBOT_DELETE:
			robots.remove(event.getObject());
			for (int i = entityList.size()-1; i >= 0 ; i--) {
				if (entityList.get(i).getModel() == event.getObject()) {
					entityList.remove(i).clearReferences();
				}
			}
			break;
			
		default:
			break;
		
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		
		spriteBatch.dispose();
		pp.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		view.update(width, height);
		
		pp.onResize(view);
	}
	
	@Override
	public void onUpdatePreferences(PrefKey updatedKey, Object value) {
		switch (updatedKey) {
		
		case VIEW_WIDTH:
			view.setWorldWidth((float) value);
			view.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
			spriteBatch.setProjectionMatrix(view.getCamera().combined);
			pp.onViewUpdate(view);
			break;
			
		case VIEW_HEIGHT:
			view.setWorldHeight((float) value);
			view.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
			spriteBatch.setProjectionMatrix(view.getCamera().combined);
			pp.onViewUpdate(view);			
			break;
			
		default:
			break;
		
		}
	}

}