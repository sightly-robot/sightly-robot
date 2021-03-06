package de.sightly_robot.sightly_robot.visual.game.entity.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.sightly_robot.sightly_robot.model.interfaces.IEvent;
import de.sightly_robot.sightly_robot.model.interfaces.IRobot;
import de.sightly_robot.sightly_robot.model.interfaces.IPosition.Orientation;
import de.sightly_robot.sightly_robot.model.interfaces.IRobot.RobotState;
import de.sightly_robot.sightly_robot.visual.core.PrefKey;
import de.sightly_robot.sightly_robot.visual.core.entity.Component;
import de.sightly_robot.sightly_robot.visual.core.entity.IEntityModifier;
import de.sightly_robot.sightly_robot.visual.game.entity.Robot;
import de.sightly_robot.sightly_robot.visual.game.entity.modifier.AlphaModifier;
import de.sightly_robot.sightly_robot.visual.game.entity.modifier.MoveModifierX;
import de.sightly_robot.sightly_robot.visual.game.entity.modifier.MoveModifierY;
import de.sightly_robot.sightly_robot.visual.game.entity.modifier.RotationModifier;
import de.sightly_robot.sightly_robot.visual.util.ModelUtil;
import de.sightly_robot.sightly_robot.visual.util.TransformUtil;
import de.sightly_robot.sightly_robot.visual.util.pref.IPreferences;

/**
 * Component designed for {@link Robot} which handles the movement of the robot
 * using progress events. <br>
 * If progress events are not emitted, the engine will use robot position events
 * instead.
 * 
 * @author Rico Schrage
 */
public class RobotEngine extends Component<Robot> {

	/** logger (log4j) */
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * interval time used for the first calculation of the robot speed (in
	 * seconds)
	 */
	private static final float FIRST_INTERVAL_GUESS = 0.1f;

	/**
	 * number of intervals which will be used to calculate the current interval
	 */
	private static final int INTERVAL_COUNT = 10;

	/** history of intervals between the progress events */
	private float[] intervalHistory;
	/** index of the last interval in {@link #intervalHistory} */
	private int currentEnd;
	/** current interval based on the {@link #intervalHistory} */
	private float interval;
	/** time of the last progress event */
	private long lastEvent;
	/** True, if the component already handled a progress event */
	private boolean firstEvent;

	/** width of a field */
	private float fieldWidth;
	/** height of a field */
	private float fieldHeight;

	/**
	 * Constructs a RobotEngine using <code>pref</code>.
	 * 
	 * @param pref
	 */
	public RobotEngine(IPreferences<PrefKey> pref) {
		super(pref);

		this.fieldWidth = pref.getFloat(PrefKey.FIELD_WIDTH_KEY);
		this.fieldHeight = pref.getFloat(PrefKey.FIELD_HEIGHT_KEY);
	}

	@Override
	public void onRegister(final Robot entity) {
		super.onRegister(entity);

		intervalHistory = new float[INTERVAL_COUNT];
		interval = FIRST_INTERVAL_GUESS;
		firstEvent = false;
		lastEvent = 0;
		currentEnd = 0;
	}

	@Override
	public void onEvent(IEvent event) {
		switch (event.getType()) {

		case ROBOT_PROGRESS:
			calcInterval();
			final IRobot robot = (IRobot) event.getObject();
			if (robot.getState() != RobotState.SETUPSTATE) {
				updateRobot(robot, robot.getPosition().getProgress(), true);
			}
			break;

		case ROBOT_POSITION:
			final IRobot robotPos = (IRobot) event.getObject();
			if (robotPos.getState() == RobotState.SETUPSTATE) {
				updateRobot(robotPos, 0, false);
			} else {
				if (!firstEvent) {
					updateRobot((IRobot) event.getObject(), 0, true);
				}
			}
			break;

		default:
			break;

		}
	}

	/**
	 * Calculates the interval based on the {@link #intervalHistory}.
	 */
	private void calcInterval() {
		float realInterval = (System.nanoTime() - lastEvent) / 1000000000f;
		if (realInterval > interval * 2 || realInterval < interval / 2) {
			LOGGER.debug("Progress-event: time out of row {} (s)", realInterval);
		}

		if (!firstEvent) {
			firstEvent = true;
		} else {
			intervalHistory[currentEnd] = (System.nanoTime() - lastEvent) / 1000000000f;
			currentEnd = (currentEnd + 1) % INTERVAL_COUNT;
			for (final float value : intervalHistory) {
				interval += value;
			}
			interval /= INTERVAL_COUNT;
		}
		lastEvent = System.nanoTime();
	}

	/**
	 * Updates x/y-coordinate/rotation of the robot.
	 * 
	 * This method uses entityModifier for a smooth transition.
	 * 
	 * @see IEntityModifier
	 * @param robo
	 *            data model of the robot
	 * @param rawProgress
	 *            current progress emitted by the robot
	 * @param smooth
	 *            used for smooth drawing, set false if the robot is set in
	 *            setup state again
	 */
	private void updateRobot(final IRobot robo, final float rawProgress,
			boolean smooth) {

		entity.clearModifier(MoveModifierX.class);
		entity.clearModifier(MoveModifierY.class);
		entity.clearModifier(RotationModifier.class);

		Orientation orientation = robo.getPosition().getOrientation();

		float progress = rawProgress / 1000f;
		float newRenderX = calculateNewX(orientation, progress, robo);
		float newRenderY = calculateNewY(orientation, progress, robo);

		if (entity.getPositionX() <= -1 || entity.getPositionY() <= -1) {
			spawn(newRenderX, newRenderY, orientation);
			return;
		}

		if (smooth) {
			IEntityModifier rotationModifier = new RotationModifier(entity,
					interval, entity.getRotation(),
					TransformUtil.calculateShortestRotation(
							entity.getRotation(),
							ModelUtil.calculateRotation(orientation)));
			entity.registerModifier(rotationModifier);
			entity.registerModifier(new MoveModifierX(entity, interval, entity
					.getPositionX(), newRenderX));
			entity.registerModifier(new MoveModifierY(entity, interval, entity
					.getPositionY(), newRenderY));
		} else {
			entity.setPosition(newRenderX, newRenderY);
			entity.setRotation(ModelUtil.calculateRotation(orientation));
		}
	}

	/**
	 * Called when an update occurs and the robot isn't visible.
	 * 
	 * @param newRenderX
	 *            new x position
	 * @param newRenderY
	 *            new y position
	 * @param orientation
	 *            new orientation
	 */
	private void spawn(float newRenderX, float newRenderY,
			Orientation orientation) {
		entity.setRotation(ModelUtil.calculateRotation(orientation));
		entity.setPosition(newRenderX, newRenderY);
		entity.registerModifier(new AlphaModifier(entity, 1, 0f, 1f));
	}

	/**
	 * Calculates new x position based on the progress.
	 * 
	 * @param orientation
	 *            new orientation
	 * @param progress
	 *            current progress in the range of [0,1]
	 * @param robo
	 *            robot to be updated
	 * @return x as virtual screen coordinate
	 */
	private float calculateNewX(Orientation orientation, float progress,
			IRobot robo) {
		final float factorX = calcFactorX(orientation);

		final float offsetX = fieldWidth / 2 - entity.getWidth() / 2;
		final float fieldXOld = robo.getPosition().getX() * fieldWidth
				+ offsetX;
		final float fieldXNew = (robo.getPosition().getX() + factorX)
				* fieldWidth + offsetX;
		return fieldXOld + factorX
				* Math.abs(fieldXOld - fieldXNew) * progress;
	}

	/**
	 * Calculates new y position based on the progress.
	 * 
	 * @param orientation
	 *            new orientation
	 * @param progress
	 *            current progress in the range of [0,1]
	 * @param robo
	 *            robot to be updated
	 * @return x as virtual screen coordinate
	 */
	private float calculateNewY(Orientation orientation, float progress,
			IRobot robo) {
		final float factorY = calcFactorY(orientation);

		final float offsetY = fieldHeight / 2 - entity.getHeight() / 2;
		final float fieldYOld = robo.getPosition().getY() * fieldHeight
				+ offsetY;
		final float fieldYNew = (robo.getPosition().getY() + factorY)
				* fieldHeight + offsetY;
		return fieldYOld + factorY
				* Math.abs(fieldYOld - fieldYNew) * progress;
	}

	/**
	 * Helper which calculates the factor of a value, which should be added to
	 * the x position of an entity.
	 * 
	 * @param orientation
	 *            orientation
	 * @return 0 for north and south, 1 for east, -1 for west
	 */
	private float calcFactorX(final Orientation orientation) {
		switch (orientation) {

		case NORTH:
		case SOUTH:
			return 0;

		case EAST:
			return 1;

		case WEST:
			return -1;

		default:
			return 0;
		}
	}

	/**
	 * Helper which calculates the factor of a value, which should be added to
	 * the y position of an entity.
	 * 
	 * @param orientation
	 *            orientation
	 * @return 0 for east and west, -1 for north, 1 for south
	 */
	private float calcFactorY(final Orientation orientation) {
		switch (orientation) {

		case EAST:
		case WEST:
			return 0;

		case NORTH:
			return -1;

		case SOUTH:
			return 1;

		default:
			return 0;
		}
	}

	@Override
	public void onUpdatePreferences(PrefKey updatedKey, Object value) {
		switch (updatedKey) {

		case FIELD_HEIGHT_KEY:
			this.fieldHeight = (float) value;
			break;

		case FIELD_WIDTH_KEY:
			this.fieldWidth = (float) value;
			break;

		default:
			break;

		}
	}

	@Override
	public void update() {
		// event only component
	}

	@Override
	public void draw(final Batch batch) {
		// event only component
	}

}
