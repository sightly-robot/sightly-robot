package de.unihannover.swp2015.robots2.visual.core;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.effects.Bloom;
import com.bitfire.postprocessing.effects.Fxaa;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;
import de.unihannover.swp2015.robots2.visual.core.base.GameHandler;
import de.unihannover.swp2015.robots2.visual.entity.IEntity;
import de.unihannover.swp2015.robots2.visual.entity.Map;
import de.unihannover.swp2015.robots2.visual.entity.Robot;
import de.unihannover.swp2015.robots2.visual.entity.modifier.base.IEntityModifier;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.ui.UI;
import de.unihannover.swp2015.robots2.visual.util.SortUtil;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;

/**
 * It handles all entities, resources and update processes of the RobotGame.
 * 
 * @author Rico Schrage
 */
public class RobotGameHandler extends GameHandler {

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
	 * Viewport of the game.
	 */
	protected final Viewport view;
	
	/**
	 * SpriteBatch for rendering textures.
	 */
	protected SpriteBatch spriteBatch;
		
	/**
	 * Capable to render post-processing effects. For the map.
	 */
	protected PostProcessor pp;
	/**
	 * Capable to render post-processing effects. For the map.
	 */
	protected PostProcessor pp2;
	
	/**
	 * Effect, which blurs the screen when game is stopped.
	 */
	protected Bloom bloom;
	
	/**
	 * UI, which will be displayed if {@link IGame#isRunning()} == false
	 */
	protected UI ui;
			
	/**
	 * Overview of robots for ranking
	 */
	private final List<IRobot> robots;
	
	/**
	 * FBO, part of a workaround for a bug in the PP-lib.
	 */
	private final FrameBuffer fbo;
	
	/**
	 * TextureRegion, part of a workaround for a bug in the PP-lib.
	 * Describes the resulting texture of the FBO as texture region.
	 */
	private final TextureRegion reg;
	
	/**
	 * Construct a new RobotGameHandler and connects this handler (means it will directly observe the model) to the given model <code>game</code>
	 * 
	 * @param game root of the model
	 * @param resourceHandler {@link IResourceHandler}
	 */
	public RobotGameHandler(final IGame game, final Viewport view, final IResourceHandler resourceHandler, final IPreferences prefs) {
		super(resourceHandler, prefs);
		
		this.robots = new ArrayList<>();
		this.modifierList = new ArrayList<>();
		this.entityList = new ArrayList<>();
		this.game = game;
		this.view = view;
		this.spriteBatch = new SpriteBatch();
		this.spriteBatch.setProjectionMatrix(view.getCamera().combined);
		this.game.observe(this);
		
		this.pp = new PostProcessor(false, true, true);
		this.pp2 = new PostProcessor(false, true, true);
		this.bloom = new Bloom((int) (view.getWorldWidth()/2f), (int)(view.getWorldHeight()/2f));
		this.bloom.setBaseIntesity(0);
		this.bloom.setThreshold(0);
		this.bloom.setBloomSaturation(0.3f);
		this.bloom.setEnabled(!game.isRunning());
		this.pp.addEffect(bloom);
		this.pp2.addEffect(new Fxaa((int) (view.getWorldWidth() * 2), (int) (view.getWorldHeight() * 2)));
		this.fbo = new FrameBuffer(Pixmap.Format.RGBA4444, 800, 800, false);
		this.reg = new TextureRegion(fbo.getColorBufferTexture());

		this.init();
	}
	
	/**
	 * Will create all entities based on the game object.
	 */
	private void init() {
		final IStage stage = game.getStage();
		
		//set preferences !have to happen before creating entities!
		this.prefs.putInt(PrefConst.MAP_ROWS_KEY, stage.getWidth());
		this.prefs.putInt(PrefConst.MAP_COLS_KEY, stage.getHeight());
		this.prefs.putFloat(PrefConst.FIELD_WIDTH_KEY, view.getWorldWidth() / stage.getWidth());
		this.prefs.putFloat(PrefConst.FIELD_HEIGHT_KEY, view.getWorldWidth() / stage.getHeight());
		
		//create entities
		for (final IRobot roboModel : game.getRobots().values()) {
			final Robot robo = new Robot(roboModel, this);
			robo.setZIndex(1);
			this.entityList.add(robo);
			this.robots.add(roboModel);
		}
		this.entityList.add(new Map(stage, this));
		SortUtil.sortEntities(entityList);
		
		//create ui
		this.ui = new UI(robots, spriteBatch, this);
	}
	
	/**
	 * Returns the current ranking of the given robot
	 * 
	 * @param robot you want the ranking for
	 */
	public int getRanking(final IRobot robo) {
		for (int i = 0; i < robots.size(); ++i) {
			if (robots.get(i) == robo)
				return i+1;
		}
		return -1;
	}
	
	@Override
	public void update() {
		for (int i = 0; i < entityList.size(); ++i) {
			this.entityList.get(i).update();
		}
	}
			
	@Override
	public void render() {
		
		if (!game.isRunning()) {
			//TODO check whether the performance drop in this branch is acceptable.
			//approx. 50% slower than the other branch.
			pp.capture();
			
			spriteBatch.begin();
			for (int i = 0; i < entityList.size(); ++i) {
				this.entityList.get(i).draw(spriteBatch);
			}
			spriteBatch.end();
			
			pp.render(fbo);
			pp2.capture();
			
			spriteBatch.begin();
			spriteBatch.draw(reg, 0, 0);
			spriteBatch.end();
			
			ui.render();
			
			pp2.render();
		}
		else {
			pp2.capture();
			spriteBatch.begin();
			for (int i = 0; i < entityList.size(); ++i) {
				this.entityList.get(i).draw(spriteBatch);
			}
			spriteBatch.end();
			pp2.render();
		}
	}

	@Override
	public void onManagedModelUpdate(IEvent event) {
		switch(event.getType()) {
		
		case GAME_PARAMETER:
			break;
			
		case GAME_STATE:
			bloom.setEnabled(!game.isRunning());
			ui.setDisplay(true);
			break;
			
		case ROBOT_ADD:
			if (event.getObject() instanceof IRobot) {
				final IRobot robot = (IRobot) event.getObject();
				final Robot roboEntity = new Robot(robot, this);
				roboEntity.setZIndex(1);
				SortUtil.addEntitySorted(roboEntity, entityList);
				SortUtil.addRobotSorted(robot, robots);
			}
			break;
			
		case ROBOT_DELETE:
			this.robots.remove(event.getObject());
			for (int i = entityList.size()-1; i >= 0 ; i--) {
				if (entityList.get(i).getModel() == event.getObject()) {
					this.entityList.remove(i);
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
		
		this.spriteBatch.dispose();
		this.pp.dispose();
		this.pp2.dispose();
		this.fbo.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		this.pp.setViewport(new Rectangle(view.getScreenX(), view.getScreenY(),
				view.getScreenWidth(), view.getScreenHeight()));
		this.pp2.setViewport(new Rectangle(view.getScreenX(), view.getScreenY(),
				view.getScreenWidth(), view.getScreenHeight()));
	}
	
	/**
	 * 
	 * @return list of robots in game
	 */
	
	public List<IRobot> getRobots(){
		return robots;
	}
	
}
