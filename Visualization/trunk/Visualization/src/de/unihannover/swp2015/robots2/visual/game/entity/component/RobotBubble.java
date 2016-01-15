package de.unihannover.swp2015.robots2.visual.game.entity.component;

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
import de.unihannover.swp2015.robots2.visual.game.entity.Robot;
import de.unihannover.swp2015.robots2.visual.resource.ResConst;
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
		
	/**
	 * Constructs a bubble for a given robot.
	 * 
	 * @param handler game-handler of the robot
	 * @param parent robot
	 */
	public RobotBubble(RobotGameHandler handler, Robot parent) {
		super(parent.getModel(), handler);
	
		final IRobot model = (IRobot) parent.getModel();
		this.fontSmall = resHandler.getFont(ResConst.DEFAULT_FONT_SMALL);
		this.fontBig = resHandler.getFont(ResConst.DEFAULT_FONT_POINTS);
		this.points = model.getScore() + "(" + handler.getRanking(model) + ")";
		this.id = model.getId().substring(0, 8);
		this.color = ColorUtil.fromAwtColor(model.getColor());
		this.color.a = GameConst.BUBBLE_ALPHA;
		this.tex = resHandler.createRenderUnit(ResConst.DEFAULT_BUBBLE);
		this.parent = parent;
		
		
		this.fieldWidth = gameHandler.getPreferences().getFloat(PrefKey.FIELD_WIDTH_KEY);
		this.fieldHeight = gameHandler.getPreferences().getFloat(PrefKey.FIELD_HEIGHT_KEY);
		
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
	
	@Override
	public void draw(Batch batch) {
		final float finalX = parent.getPositionX() + x;
		final float finalY = parent.getPositionY() + y;
		
		batch.setColor(color);
		tex.draw(batch, finalX, finalY, width, height);
		fontSmall.draw(batch, id, finalX + width/8, finalY + height/8, width, Align.left, false);
		fontBig.draw(batch, points, finalX + width/8, finalY + height/3, width, Align.left, false);
		batch.setColor(Color.WHITE);
	}
	
	@Override
	public void onUpdatePreferences(PrefKey updatedKey, Object value) {
		switch(updatedKey) {
		
		case FIELD_HEIGHT_KEY:
			this.fieldHeight = (float) value;
			updateHeight((IRobot) parent.getModel());
			break;
			
		case FIELD_WIDTH_KEY:
			this.fieldWidth = (float) value;
			updateWidth((IRobot) parent.getModel());
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

		default:
			break;
		}			
	}

}
