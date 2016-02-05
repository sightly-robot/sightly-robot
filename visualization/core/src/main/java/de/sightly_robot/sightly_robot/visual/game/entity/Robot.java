package de.sightly_robot.sightly_robot.visual.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.sightly_robot.sightly_robot.model.interfaces.IEvent;
import de.sightly_robot.sightly_robot.model.interfaces.IRobot;
import de.sightly_robot.sightly_robot.model.interfaces.IRobot.RobotState;
import de.sightly_robot.sightly_robot.visual.core.PrefKey;
import de.sightly_robot.sightly_robot.visual.core.entity.Entity;
import de.sightly_robot.sightly_robot.visual.game.GameConst;
import de.sightly_robot.sightly_robot.visual.game.RobotGameHandler;
import de.sightly_robot.sightly_robot.visual.game.entity.component.RobotEngine;
import de.sightly_robot.sightly_robot.visual.resource.ResConst;
import de.sightly_robot.sightly_robot.visual.resource.texture.RenderUnit;
import de.sightly_robot.sightly_robot.visual.util.ColorUtil;
import de.sightly_robot.sightly_robot.visual.util.DelayedTask;
import de.sightly_robot.sightly_robot.visual.util.ModelUtil;
import de.sightly_robot.sightly_robot.visual.util.Task;

/**
 * an entity used for the visualization of robots (both virtual and hardware)
 * 
 * @author Daphne Schössow
 */
public class Robot extends Entity<RobotGameHandler, IRobot> {

	/** visual representation of the entity */
	private final RenderUnit robo;

	/** renderUnit which contains a texture to draw the start position */
	private final RenderUnit startPositionTexture;

	/** true if the start position should be drawn */
	private boolean drawStartPosition = false;

	/** width of the field */
	private float fieldWidth;

	/** height of the field */
	private float fieldHeight;

	/** bubble of the robot, which displays information about the robot */
	private RobotBubble bubble;

	/** performs task to let the robot blink */
	private DelayedTask blinkTask;

	/** true if the robot should get rendered */
	private boolean renderRobot;

	/** color of the robot */
	private Color robotColor;

	/** true if the bubble should get rendered */
	private boolean renderBubble;

	/**
	 * Construction of a robot entity using given parameters.
	 * 
	 * @param robot
	 *            data model of the {@link Robot}
	 * @param gameHandler
	 *            parent
	 */
	public Robot(IRobot robot, RobotGameHandler gameHandler) {
		super(robot, gameHandler);

		this.robo = robot.isHardwareRobot() ? resHandler
				.createRenderUnit(ResConst.DEFAULT_ROBO) : resHandler
				.createRenderUnit(ResConst.DEFAULT_VIRTUAL_ROBO);
		this.startPositionTexture = resHandler
				.createRenderUnit(ResConst.DEFAULT_STARTPOS);
		this.drawStartPosition = robot.getState() == RobotState.SETUPSTATE;
		this.rotation = ModelUtil.calculateRotation(robot.getPosition()
				.getOrientation());
		this.robotColor = ColorUtil.fromAwtColor(robot.getColor());

		this.fieldWidth = prefs.getFloat(PrefKey.FIELD_WIDTH_KEY);
		this.fieldHeight = prefs.getFloat(PrefKey.FIELD_HEIGHT_KEY);

		this.renderRobot = robot.isHardwareRobot() ? prefs
				.getBoolean(PrefKey.RENDER_ROBOTS) : prefs
				.getBoolean(PrefKey.RENDER_VIRTUAL_ROBOTS);
		this.renderBubble = robot.isHardwareRobot() ? prefs
				.getBoolean(PrefKey.RENDER_HARDWARE_BUBBLE) : prefs
				.getBoolean(PrefKey.RENDER_VIRTUAL_BUBBLE);

		this.updateWidth();
		this.updateHeight();
		this.bubble = new RobotBubble(gameHandler, this);

		this.registerComponent(new RobotEngine(prefs));

		this.blinkTask = new DelayedTask(1f, new Task() {
			@Override
			public void work() {
				color.set(1f, 1f, 1f, color.a);
			}
		});
		this.blinkTask.kill();
	}

	/**
	 * Updates all values which depend on {@link fieldWidth}.
	 */
	private void updateWidth() {
		this.width = fieldWidth * GameConst.ROBOT_SCALE;
		this.renderX = model.getPosition().getX() * fieldWidth + fieldWidth / 2
				- width / 2;
	}

	/**
	 * Updates all values which depend on {@link fieldHeight}.
	 */
	private void updateHeight() {
		this.height = fieldHeight * GameConst.ROBOT_SCALE;
		this.renderY = model.getPosition().getY() * fieldHeight + fieldHeight
				/ 2 - height / 2;
	}

	@Override
	public void update() {
		super.update();

		blinkTask.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void draw(final Batch batch) {
		super.draw(batch);

		if (drawStartPosition) {
			startPositionTexture.draw(batch, fieldWidth
					* model.getPosition().getX(), fieldHeight
					* model.getPosition().getY(), fieldWidth, fieldHeight,
					fieldWidth / 2f, fieldHeight / 2f, 1f, 1f, rotation);
		} else {
			if (renderRobot) {
				robo.draw(batch, renderX, renderY, width, height, width / 2f,
						height / 2f, 1f, 1f, rotation);
			}
			if (renderBubble) {
				bubble.draw(batch);
			}
		}
	}

	@Override
	public void onManagedModelUpdate(IEvent event) {
		IRobot robotModel = (IRobot) model;

		switch (event.getType()) {

		case ROBOT_BLINK:
			color.set(robotColor.r, robotColor.g, robotColor.b, color.a);
			blinkTask.reset();
			break;

		case ROBOT_STATE:
			drawStartPosition = robotModel.getState() == RobotState.SETUPSTATE;
			break;

		default:
			break;
		}
	}

	@Override
	public void onUpdatePreferences(PrefKey updatedKey, Object value) {
		final IRobot robot = (IRobot) model;

		switch (updatedKey) {

		case FIELD_HEIGHT_KEY:
			modList.clear();
			fieldHeight = (float) value;
			updateHeight();
			break;

		case FIELD_WIDTH_KEY:
			modList.clear();
			fieldWidth = (float) value;
			updateWidth();
			break;

		case RENDER_ROBOTS:
			renderRobot = robot.isHardwareRobot() ? (boolean) value
					: renderRobot;
			break;

		case RENDER_HARDWARE_BUBBLE:
			renderBubble = robot.isHardwareRobot() ? (boolean) value
					: renderBubble;
			break;

		case RENDER_VIRTUAL_BUBBLE:
			renderBubble = robot.isHardwareRobot() ? renderBubble
					: (boolean) value;
			break;

		case RENDER_VIRTUAL_ROBOTS:
			renderRobot = robot.isHardwareRobot() ? renderRobot
					: (boolean) value;
			break;

		default:
			break;

		}
	}

}
