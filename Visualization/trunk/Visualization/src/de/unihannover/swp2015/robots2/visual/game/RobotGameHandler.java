package de.unihannover.swp2015.robots2.visual.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot.RobotState;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;
import de.unihannover.swp2015.robots2.visual.core.PrefKey;
import de.unihannover.swp2015.robots2.visual.core.entity.Entity;
import de.unihannover.swp2015.robots2.visual.core.entity.IEntity;
import de.unihannover.swp2015.robots2.visual.core.handler.GameHandler;
import de.unihannover.swp2015.robots2.visual.game.entity.Map;
import de.unihannover.swp2015.robots2.visual.game.entity.Robot;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.resource.ResourceHandler;
import de.unihannover.swp2015.robots2.visual.util.FlexibleFitViewport;
import de.unihannover.swp2015.robots2.visual.util.SortUtil;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;

/**
 * It handles all entities, resources and update processes of the RobotGame.
 * 
 * @author Rico Schrage
 */
public class RobotGameHandler extends GameHandler {
	
	/** List of entities managed by this game handler. */
	protected final List<IEntity> entityList;

	/** Viewport of the game. */
	protected FlexibleFitViewport view;

	/** SpriteBatch for rendering textures. */
	protected SpriteBatch batch;
	
	/** Constant scaleX of the current batch*/
	private float batchScaleX;
	/** Constant scaleY of the current batch*/
	private float batchScaleY;

	/** Overview of robots for ranking */
	private final List<IRobot> robots;

	/** Manages several post-processors. */
	private final PostProcessHandler pp;

	/** UI, which will be displayed if {@link IGame#isRunning()} == false */
	private UI ui;
	
	/** List of all available themes */
	private List<String> themes;
	
	/** Current theme */
	private int currentTheme;
	
	private boolean reseted = false;

	/**
	 * Construct a new RobotGameHandler and connects this handler (means it will
	 * directly observe the model) to the given model <code>game</code>
	 * 
	 * @param game
	 *            root of the model
	 * @param resourceHandler
	 *            {@link IResourceHandler}
	 */
	public RobotGameHandler(IGame robotGame, FlexibleFitViewport viewport, IResourceHandler resourceHandler,
			IPreferences<PrefKey> prefs) {
		super(robotGame, resourceHandler, prefs);

		this.robots = new ArrayList<>();
		this.entityList = new ArrayList<>();
		this.view = viewport;
		this.batch = new SpriteBatch();
		this.batch.setProjectionMatrix(view.getCamera().combined);
		this.pp = new PostProcessHandler((int) view.getWorldWidth(), (int) view.getWorldHeight());
		this.themes = ResourceHandler.themeKeys();
		this.currentTheme = ResourceHandler.getDefaultThemeIndex(themes);
		this.batchScaleX = batch.getProjectionMatrix().getScaleX();
		this.batchScaleY = batch.getProjectionMatrix().getScaleY();
		
		this.init();
	}

	/**
	 * Will create all entities based on the game object.
	 */
	private void init() {
		final IStage stage = game.getStage();

		prefs.putFloat(PrefKey.FIELD_WIDTH_KEY, view.getWorldWidth() / stage.getWidth());
		prefs.putFloat(PrefKey.FIELD_HEIGHT_KEY, view.getWorldWidth() / stage.getHeight());

		view.setOffsetX((int) prefs.getFloat(PrefKey.X_OFFSET));
		view.setOffsetY((int) prefs.getFloat(PrefKey.Y_OFFSET));
		
		entityList.add(new Map(stage, this));
		Entity.sortEntities(entityList);

		ui = new UI(robots, view, batch, resHandler);
	}
	
	/**
	 * Applies given x scale to the batch.
	 * 
	 * @param scaleX scaleX [0,X]
	 */
	private void applyScaleX(float scaleX) {
		Matrix4 proj = batch.getProjectionMatrix();
		float oldScale = proj.getScaleX();
		batch.getProjectionMatrix().scale(1f/oldScale, 1f, 1f);
		batch.getProjectionMatrix().scale(scaleX*batchScaleX, 1f, 1f);
	}

	/**
	 * Applies given y scale to the batch.
	 * 
	 * @param scaleY scaleY [0,X]
	 */
	private void applyScaleY(float scaleY) {
		Matrix4 proj = batch.getProjectionMatrix();
		float oldScale = proj.getScaleY();
		batch.getProjectionMatrix().scale(1f, 1f/oldScale, 1f);
		batch.getProjectionMatrix().scale(1f, batchScaleY*scaleY, 1f);
	}
	
	/**
	 * Convenience method, which basically just calls {@link #applyScaleX(float)} and {@link #applyScaleY(float)} 
	 * with the given value.
	 * 
	 * @param scaleX scale in x direction
	 * @param scaleY scale in y direction
	 */
	private void applyScale(float scaleX, float scaleY) {
		applyScaleX(scaleX);
		applyScaleY(scaleY);
	}
	
	/**
	 * Called when the  glViewport have to be updated.
	 *  
	 * @param viewWidth new width of the viewport
	 */
	private void updateViewWidth(float viewWidth) {
		view.setWorldWidth(viewWidth);
		onUpdateView();
	}

	/**
	 * Called when the  glViewport have to be updated.
	 *  
	 * @param viewHeight new height of the viewport
	 */
	private void updateViewHeight(float viewHeight) {
		view.setWorldHeight(viewHeight);
		onUpdateView();
	}
	
	private void onUpdateView() {
		view.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		batch.setProjectionMatrix(view.getCamera().combined);
		pp.onViewUpdate(view);
		ui.onResize(view);
		batchScaleY = batch.getProjectionMatrix().getScaleY();
		batchScaleX = batch.getProjectionMatrix().getScaleX();
		applyScale(prefs.getFloat(PrefKey.X_SCALE), prefs.getFloat(PrefKey.Y_SCALE));
		onStateChange();
	}
	
	/**
	 * Called when a robot has been added.
	 * 
	 * @param robot robot which has been added
	 */
	private void addRobot(IRobot robot) {
		robot.observe(this);
		final Robot roboEntity = new Robot(robot, this);
		roboEntity.setZIndex(1);
		Entity.addEntitySorted(roboEntity, entityList);
		SortUtil.addRobotSorted(robot, robots);
		ui.onRobotChange();
	}

	/**
	 * Called when a robot has been removed.
	 * 
	 * @param robot robot which has been removed
	 */
	private void removeRobot(IRobot robot) {
		robot.unobserve(this);
		robots.remove(robot);
		ui.onRobotChange();
		for (int i = entityList.size() - 1; i >= 0; i--) {
			if (entityList.get(i) instanceof Robot) {
				Robot robotEntity = (Robot) entityList.get(i);
				if (robotEntity.getModel() == robot) {
					entityList.remove(i).clearReferences();
				}
			}
		}
	}
	
	/**
	 * Called when the state of one of the robot changes.
	 */
	private void onStateChange() {
		boolean gameSetup = false;
		for (int i = 0; i < robots.size(); ++i) {
			if (robots.get(i).getState() == RobotState.SETUPSTATE) {
				gameSetup = true;
			}
		}
		boolean condition = (!gameSetup && (!game.isRunning() && !reseted));
		pp.setBloomEnabled(condition);
		ui.setVisible(condition);
	}
	
	/**
	 * Returns the current ranking of the given robot
	 * 
	 * @param robot
	 *            you want the ranking for
	 */
	public int getRanking(final IRobot robo) {
		SortUtil.sortRobots(robots);

		for (int i = 0; i < robots.size(); ++i) {
			if (robots.get(i) == robo)
				return i + 1;
		}
		return -1;
	}
	
	/**
	 * Returns robot by id
	 * 
	 * @param id id of the robot
	 * @return robot
	 */
	public IRobot getRobot(String id) {
		return game.getRobots().get(id);
	}
	
	@Override
	public void update() {

		for (int i = 0; i < entityList.size(); ++i) {
			entityList.get(i).update();
		}
		ui.update();
	}

	@Override
	public void render() {
		
		if (!game.isRunning()) {

			if (pp.isBloomEnabled()) {
				// approx. 50% slower than the other branch.
				pp.captureBloom();

				batch.begin();
				for (int i = 0; i < entityList.size(); ++i) {
					entityList.get(i).draw(batch);
				}
				batch.end();

				pp.renderBloomToInternalBuffer();
				pp.captureFxaa();

				batch.begin();
				batch.draw(pp.getBufferTexture(), 0, 0, 0, 0, pp.getBufferTexture().getRegionWidth(), pp.getBufferTexture().getRegionHeight(), 
						1f/prefs.getFloat(PrefKey.X_SCALE), 1f/prefs.getFloat(PrefKey.Y_SCALE), 0);
				batch.end();
				
				ui.render();

				pp.renderFxaa();

			} else {
				pp.captureFxaa();

				batch.begin();
				for (int i = 0; i < entityList.size(); ++i) {
					entityList.get(i).draw(batch);
				}
				batch.end();
				
				ui.render();

				pp.renderFxaa();
			}
		} else {
			pp.captureFxaa();
			batch.begin();
			for (int i = 0; i < entityList.size(); ++i) {
				entityList.get(i).draw(batch);
			}
			batch.end();
			pp.renderFxaa();
		}
	}

	@Override
	public void onManagedModelUpdate(IEvent event) {

		switch (event.getType()) {

		case GAME_RESET:
			pp.setBloomEnabled(false);
			ui.setVisible(false);
			reseted = true;
			break;

		case ROBOT_STATE:
		case GAME_STATE:
			if (game.isRunning()) {
				reseted = false;
			}
			onStateChange();
			break;

		case ROBOT_ADD:
			if (event.getObject() instanceof IRobot) {
				addRobot((IRobot) event.getObject());
			}
			break;

		case ROBOT_DELETE:
			removeRobot((IRobot) event.getObject());
			break;

		default:
			break;

		}
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
			updateViewWidth((float) value);
			break;

		case VIEW_HEIGHT:
			updateViewHeight((float) value);
			break;

		case X_OFFSET:
			view.setOffsetX((int) ((float) value));
			resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			break;
			
		case Y_OFFSET:
			view.setOffsetY((int) ((float) value));
			resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			break;
			
		case X_SCALE:
			applyScaleX((float) value);
			break;
			
		case Y_SCALE:
			applyScaleY((float) value);
			break;
			
		case CYCLE_TEXTURE_PACK:
			currentTheme++;
			resHandler.loadTexturePack(themes.get(currentTheme % themes.size()));
			break;
			
		default:
			break;

		}
	}

	@Override
	public void dispose() {
		super.dispose();

		batch.dispose();
		pp.dispose();
		ui.dispose();
	}

}
