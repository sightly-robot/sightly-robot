package de.unihannover.swp2015.robots2.visual.core;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;
import de.unihannover.swp2015.robots2.visual.entity.IEntity;
import de.unihannover.swp2015.robots2.visual.entity.Map;
import de.unihannover.swp2015.robots2.visual.entity.Robot;
import de.unihannover.swp2015.robots2.visual.entity.modifier.base.IEntityModifier;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.util.EntityUtil;
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
	 * SpriteBatch for rendering textures.
	 */
	protected SpriteBatch spriteBatch;
		
	/**
	 * Main camera
	 */
	protected Viewport view;
	
	/**
	 * Overview of robots for ranking
	 */
	private IRobot[] robots;
	private int robotsIndex;
	
	/**
	 * Construct a new RobotGameHandler and connects this handler (means it will directly observe the model) to the given model <code>game</code>
	 * @param game root of the model
	 * @param resourceHandler {@link IResourceHandler}
	 */
	public RobotGameHandler(final IGame game, final IResourceHandler resourceHandler, final Viewport view, final IPreferences prefs) {
		super(resourceHandler, prefs);
		
		this.modifierList = new ArrayList<>();
		this.entityList = new ArrayList<>();
		this.game = game;
		this.spriteBatch = new SpriteBatch();
		this.view = view;
		this.spriteBatch.setProjectionMatrix(this.view.getCamera().combined);
		this.game.observe(this);
		
		this.init();
	}
	
	/**
	 * Will create all entities based on the game object.
	 */
	private void init() {
		final IStage stage = game.getStage();

		this.robots = new IRobot[game.getRobots().size()];
		this.robotsIndex = 0;
		
		//set preferences !have to happen before creating entities!
		this.prefs.putInt(PrefConst.MAP_ROWS_KEY, stage.getWidth());
		this.prefs.putInt(PrefConst.MAP_COLS_KEY, stage.getHeight());
		this.prefs.putFloat(PrefConst.FIELD_WIDTH_KEY, ((float) Gdx.graphics.getWidth()) / stage.getWidth());
		this.prefs.putFloat(PrefConst.FIELD_HEIGHT_KEY, ((float) Gdx.graphics.getHeight()) / stage.getHeight());
		
		//create entites
		for (final IRobot robot : game.getRobots().values()) {
			final Robot robo = new Robot(robot, spriteBatch, this);
			robo.setZIndex(1);
			this.entityList.add(robo);
			this.robots[robotsIndex]=robot;
			robotsIndex++;
		}
		this.entityList.add(new Map(stage, spriteBatch, this));
		
		EntityUtil.sortEntities(entityList);
	}
	
	@Override
	public void update() {
		//TODO modifier
	}
	
	@Override
	public void render() {
		spriteBatch.begin();
		for (int i = 0; i < entityList.size(); ++i) {
			this.entityList.get(i).render();
		}
		spriteBatch.end();
	}

	@Override
	public void onManagedModelUpdate(IEvent event) {
		switch(event.getType()) {
		
		case GAME_PARAMETER:
			break;
			
		case GAME_STATE:
			break;
			
		case ROBOT_ADD:
			if (event.getObject() instanceof IRobot) {
				final IRobot robot = (IRobot) event.getObject();
				final Robot roboEntity = new Robot(robot, spriteBatch, this);
				roboEntity.setZIndex(1);
				EntityUtil.addEntitySorted(roboEntity, entityList);
			}
			break;
			
		case ROBOT_DELETE:
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
	public void dispatchEvent(final IEvent event, final IEntity source) {
		
	}

	@Override
	public void dispose() {
		super.dispose();
		
		this.spriteBatch.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
	}
	
	@Override
	public int getRanking(final IRobot robo) {
		IRobot temp;
		
		for(int h=0; h<robots.length-1; h++)
		{
			for(int i=0; i<robots.length-h; i++)
			{
				System.out.println(robots[i].getScore());
				if(robots[i].getScore()>robots[i+1].getScore())
				{
					temp = robots[i];
					robots[i] = robots[i+1];
					robots[i+1]=temp;
				}
			}
		}
		for(int i=0; i<robots.length; i++)
		{
			if(robots[i]==robo)
				return i+1;
		}
		return 1;//TODO fix this
	}
	

}
