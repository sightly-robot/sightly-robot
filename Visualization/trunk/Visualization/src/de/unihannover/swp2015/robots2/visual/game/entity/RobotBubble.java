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
 * Bubble of a robot which displays the name of the robot, its points, rank, and
 * connection/error states.
 * 
 * @author Daphne Schössow
 */
public class RobotBubble extends Entity<RobotGameHandler, IRobot> {

	/** texture of the bubble */
	protected RenderUnit tex;

	/** width of the bubble */
	protected float width;

	/** height of the bubble */
	protected float height;

	/** color of the bubble */
	protected Color color;

	/** font for the name */
	protected BitmapFont fontSmall;

	/** font for the score/rank. */
	protected BitmapFont fontBig;

	/** id as string */
	protected CharSequence id;

	/** points as string */
	protected CharSequence points;

	/** x position on screen, relative to the robot */
	protected float x;

	/** y position on screen, relative to the robot */
	protected float y;

	/** width of a field */
	private float fieldWidth;

	/** height of a field */
	private float fieldHeight;

	/** robot which the bubble belongs to */
	private Robot parent;

	/** connection lost icon */
	private RenderUnit connection;

	/** warning icon */
	private RenderUnit warning;

	/** the current icon which should be rendered */
	private RenderUnit currentIcon;

	/** true if an icon has to be rendered */
	boolean renderIcon = false;

	/**
	 * Constructs a bubble for a given robot.
	 * 
	 * @param handler
	 *            game handler of the robot
	 * @param parent
	 *            robot
	 */
	public RobotBubble(RobotGameHandler handler, Robot parent) {
		super(parent.getModel(), handler);

		this.fieldWidth = gameHandler.getPreferences().getFloat(
				PrefKey.FIELD_WIDTH_KEY);
		this.fieldHeight = gameHandler.getPreferences().getFloat(
				PrefKey.FIELD_HEIGHT_KEY);

		final IRobot model = (IRobot) parent.getModel();

		this.connection = resHandler
				.createRenderUnit(ResConst.DEFAULT_CONNECTION);
		this.warning = resHandler.createRenderUnit(ResConst.DEFAULT_WARNING);
		this.tex = resHandler.createRenderUnit(ResConst.DEFAULT_BUBBLE);

		this.points = model.getScore() + "(" + handler.getRanking(model) + ")";
		this.id = model.getName();
		this.color = ColorUtil.fromAwtColor(model.getColor());
		this.color.a = GameConst.BUBBLE_ALPHA;
		this.parent = parent;

		this.updateWidth(model);
		this.updateHeight(model);
		this.updateFonts(fieldWidth);
		this.updateState();
	}

	/**
	 * Updates all values which depend on {@link fieldWidth}.
	 * 
	 * @param robot
	 *            robot model
	 */
	private void updateWidth(final IRobot robot) {
		this.x = robot.getPosition().getX() * fieldWidth
				- parent.getPositionX();
		this.width = fieldWidth * GameConst.BUBBLE_WIDTH_REL;
	}

	/**
	 * Updates all values which depend on {@link fieldHeight}.
	 * 
	 * @param robot
	 *            robot model
	 */
	private void updateHeight(final IRobot robot) {
		this.height = fieldHeight * GameConst.BUBBLE_HEIGHT_REL;
		this.y = robot.getPosition().getY() * fieldHeight
				- parent.getPositionY() - fieldHeight
				* GameConst.BUBBLE_Y_OFFSET_REL;
	}

	/**
	 * Updates the icon.
	 */
	private void updateState() {
		IRobot robot = (IRobot) model;

		switch (robot.getState()) {

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
	 * Recreates the fonts when the map has been resized.
	 * 
	 * @param value
	 *            new size of the fields
	 */
	private void updateFonts(float value) {
		fontSmall = resHandler.createFont(
				(int) (value * GameConst.BUBBLE_NAME_FONT_SIZE_REL),
				ResourceHandler.NECESSARY_CHARS, true, 1, Color.BLACK,
				Color.WHITE);
		fontBig = resHandler.createFont(
				(int) (value * GameConst.BUBBLE_NAME_POINTS_SIZE_REL),
				ResourceHandler.NECESSARY_CHARS, true, 1, Color.BLACK,
				Color.WHITE);
	}

	@Override
	public void draw(Batch batch) {
		final float finalX = parent.getPositionX() + x;
		final float finalY = parent.getPositionY() + y;

		batch.setColor(color.r, color.g, color.b, color.a * parent.getColor().a);
		tex.draw(batch, finalX, finalY, width, height);
		batch.setColor(Color.WHITE);

		if (renderIcon) {
			currentIcon.draw(batch, finalX + width
					* GameConst.BUBBLE_ICON_X_REL, finalY + height
					* GameConst.BUBBLE_ICON_Y_REL, width
					* GameConst.BUBBLE_ICON_WIDTH_REL, width
					* GameConst.BUBBLE_ICON_HEIGHT_REL);
		}

		final float fontX = finalX + width * GameConst.BUBBLE_PADDING_LEFT_REL;

		fontSmall.draw(batch, id, fontX, finalY + height
				* GameConst.BUBBLE_FONT_NAME_Y_REL, width, Align.left, false);
		fontBig.draw(batch, points, fontX, finalY + height
				* GameConst.BUBBLE_FONT_POINTS_Y_REL, width, Align.left, false);
	}

	@Override
	public void onUpdatePreferences(PrefKey updatedKey, Object value) {
		switch (updatedKey) {

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
		switch (event.getType()) {

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
