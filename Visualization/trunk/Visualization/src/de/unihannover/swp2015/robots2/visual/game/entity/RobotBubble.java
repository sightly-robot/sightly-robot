package de.unihannover.swp2015.robots2.visual.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.visual.core.PrefKey;
import de.unihannover.swp2015.robots2.visual.core.entity.Entity;
import de.unihannover.swp2015.robots2.visual.game.GameConst;
import de.unihannover.swp2015.robots2.visual.game.RobotGameHandler;
import de.unihannover.swp2015.robots2.visual.resource.ResConst;
import de.unihannover.swp2015.robots2.visual.resource.ResourceHandler;
import de.unihannover.swp2015.robots2.visual.resource.texture.RenderUnit;
import de.unihannover.swp2015.robots2.visual.util.ColorUtil;

/**
 * Bubble of a robot. Display points and the id of the robot.
 * 
 * @author Rico Schrage
 */
public class RobotBubble extends Entity {
	
	/** Texture of the bubble. */
	protected RenderUnit tex;
	
	/** Width of the bubble. */
	protected float width;
	
	/** Height of the bubble. */
	protected float height;
	
	/** Color of the bubble. This value is based on the model. */
	protected Color color;
	
	/** Font of the name. */
	protected BitmapFont fontSmall;
	
	/** Font of the score/rank. */
	protected BitmapFont fontBig;

	/** id as string. */
	protected CharSequence id;
	
	/** Points as string. */
	protected CharSequence points;
	
	/** X-position on (virtual) screen relative to the robot. */
	protected float x;

	/** Y-position on (virtual) screen relative to the robot. */
	protected float y;
	
	/** Width of a single field  */
	private float fieldWidth;
	
	/** Height of a single field*/
	private float fieldHeight;
	
	/** Parent of the bubble */
	private Robot parent;
	
	/** Connection lost icon */
	private RenderUnit connection;
	
	/** Warning icon */
	private RenderUnit warning;
	
	/** References the current icon, which should be rendered*/
	private RenderUnit currentIcon;
	
	/** True if an icon have to be rendered */
	boolean renderIcon = false;
		
	/**
	 * Constructs a bubble for a given robot.
	 * 
	 * @param handler game-handler of the robot
	 * @param parent robot
	 */
	public RobotBubble(RobotGameHandler handler, Robot parent) {
		super(parent.getModel(), handler);

		this.fieldWidth = gameHandler.getPreferences().getFloat(PrefKey.FIELD_WIDTH_KEY);
		this.fieldHeight = gameHandler.getPreferences().getFloat(PrefKey.FIELD_HEIGHT_KEY);
		
		final IRobot model = (IRobot) parent.getModel();
		this.fontSmall = resHandler.createFont((int) fieldWidth/12, ResourceHandler.NECESSARY_CHARS, true, 1, Color.BLACK);
		this.fontBig = resHandler.createFont((int) fieldWidth/9, ResourceHandler.NECESSARY_CHARS, true, 1, Color.BLACK);
		this.points = model.getScore() + "(" + handler.getRanking(model) + ")";
		this.id = model.getName();
		this.color = ColorUtil.fromAwtColor(model.getColor());
		this.color.a = GameConst.BUBBLE_ALPHA;
		this.tex = resHandler.createRenderUnit(ResConst.DEFAULT_BUBBLE);
		this.parent = parent;
		this.connection = resHandler.createRenderUnit(ResConst.DEFAULT_CONNECTION);
		this.warning = resHandler.createRenderUnit(ResConst.DEFAULT_WARNING);
		
		this.updateWidth(model);
		this.updateHeight(model);
	}

	/**
	 * Updates all values, which depend on {@link fieldWidth}
	 * 
	 * @param robot robot model
	 */
	private void updateWidth(final IRobot robot) {
		this.x = robot.getPosition().getX() * fieldWidth - parent.getPositionX();
		this.width = fieldWidth * 0.75f;
	}
	
	/**
	 * Updates all values, which depend on {@link fieldHeight}
	 * 
	 * @param robot robot model
	 */
	private void updateHeight(final IRobot robot) {
		this.height = fieldHeight * 0.5f;
		this.y = robot.getPosition().getY() * fieldHeight - parent.getPositionY() - fieldHeight * 0.1f;
	}

	/**
	 * Updates the icon.
	 */
	private void updateState() {
		IRobot robot = (IRobot) model;
		
		switch(robot.getState()) {
		
		case DISCONNECTED:
			renderIcon = true;
			currentIcon = connection;
			break;
		
		case CONNECTED:
		case MANUAL_DISABLED_GUI:
		case MANUAL_DISABLED_ROBOT:
		case ROBOTICS_ERROR:
			renderIcon = true;
			currentIcon = warning;
			break;
			
		case ENABLED:
		case SETUPSTATE:
			renderIcon = false;
			break;
			
		default:
			break;
		
		}
	}
	
	/**
	 * Recreates the fonts.
	 * 
	 * @param value new size of the fields.
	 */
	private void updateFonts(float value) {
		fontSmall = resHandler.createFont((int) value/12, ResourceHandler.NECESSARY_CHARS, true, 1, Color.BLACK);
		fontBig = resHandler.createFont((int) value/9, ResourceHandler.NECESSARY_CHARS, true, 1, Color.BLACK);
	}

	@Override
	public void draw(Batch batch) {
		final float finalX = parent.getPositionX() + x;
		final float finalY = parent.getPositionY() + y;
		
		batch.setColor(color);
		tex.draw(batch, finalX, finalY, width, height);
		batch.setColor(Color.WHITE);
		
		fontSmall.draw(batch, id, finalX + width/8, finalY + height/8, width, Align.left, false);
		fontBig.draw(batch, points, finalX + width/8, finalY + height/3, width, Align.left, false);
		
		if (renderIcon) {
			currentIcon.draw(batch, finalX + width/1.6f, finalY + height/8, width/4, width/4);
		}
	}
	
	@Override
	public void onUpdatePreferences(PrefKey updatedKey, Object value) {
		switch(updatedKey) {
		
		case FIELD_HEIGHT_KEY:
			this.fieldHeight = (float) value;
			updateHeight((IRobot) parent.getModel());
			updateFonts((float) value);
			break;
			
		case FIELD_WIDTH_KEY:
			this.fieldWidth = (float) value;
			updateWidth((IRobot) parent.getModel());
			updateFonts((float) value);
			break;
		
		default:
			break;
		
		}
	}
	
	@Override
	public void onManagedModelUpdate(IEvent event) {
		switch(event.getType()) {
		
		case ROBOT_SCORE:
			final RobotGameHandler handler = (RobotGameHandler) gameHandler;
			final IRobot robot = (IRobot) event.getObject();
			points = robot.getScore() + "(" + handler.getRanking(robot) + ")";
			break;

		case ROBOT_STATE:
			updateState();
			break;
			
		default:
			break;
		}			
	}

}
