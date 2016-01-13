package de.unihannover.swp2015.robots2.visual.game.entity.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.visual.core.PrefKey;
import de.unihannover.swp2015.robots2.visual.core.entity.Component;
import de.unihannover.swp2015.robots2.visual.core.entity.IEntity;
import de.unihannover.swp2015.robots2.visual.core.entity.IEntityModifier;
import de.unihannover.swp2015.robots2.visual.game.entity.modifier.MoveModifierX;
import de.unihannover.swp2015.robots2.visual.game.entity.modifier.MoveModifierY;
import de.unihannover.swp2015.robots2.visual.game.entity.modifier.RotationModifier;
import de.unihannover.swp2015.robots2.visual.util.ModelUtil;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;

/**
 * Component designed for {@link Robot}. It handles the movement of the robot using progress events. <br>
 * If progress events are not fired, the engine will use robot position events instead.
 * 
 * @author Rico Schrage
 */
public class RobotEngine extends Component {

	private static final Logger log = LogManager.getLogger();
	
	/** Interval time used for the first calculation of the robot speed (in seconds). */
	private static final float FIRST_INTERVAL_GUESS = 0.1f;
	
	/** Number of intervals which will be used to calculate the current interval */
	private static final int INTERVAL_COUNT = 10;
	
	/** History of intervals between the progress events */
	private float[] intervalHistory;
	/** Index of the last interval in {@link #intervalHistory} */
	private int currentEnd;
	/** Current interval based on the {@link #intervalHistory}*/
	private float interval;
	/** Time of the last progress event */
	private long lastEvent;
	/** True, if the component already handled an event */
	private boolean firstEvent;

	/**
	 * Constructs a RobotEngine using <code>pref</code>.
	 * @param pref
	 */
	public RobotEngine(IPreferences<PrefKey> pref) {
		super(pref);
	}

	@Override
	public void onRegister(final IEntity entity) {
		super.onRegister(entity);
		
		intervalHistory = new float[INTERVAL_COUNT];
		interval = FIRST_INTERVAL_GUESS;
		firstEvent = false;
		lastEvent = 0;
		currentEnd = 0;
	}
	
	@Override
	public void onEvent(IEvent event) {
		switch(event.getType()) {

		case ROBOT_PROGRESS:
			float realInterval = (System.nanoTime() - lastEvent) / 1000000000f;
			if (realInterval > 0.2 || realInterval < 0.05) {
				log.info("event time out of row {}" , realInterval);
			}
			calcInterval();			
			final IRobot robot = (IRobot) event.getObject();
			updateRobot(robot, robot.getPosition().getProgress());
			break;
			
		case ROBOT_POSITION:
			updateRobot((IRobot) event.getObject(), 0);
			break;
			
		default:
			break;
		
		}
	}
	
	/**
	 * Calculates the interval based on the {@link #intervalHistory}
	 */
	private void calcInterval() {
		if (!firstEvent) {
			firstEvent = true;
		}
		else {
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
	 * Updates x/y-coordinate/rotation of the robot. This method uses entityModifier for a smooth transition.
	 * 
	 * @see IEntityModifier
	 * @param robo data model of the robot
	 */
	private void updateRobot(final IRobot robo, final float rawProgress) {

		entity.clearModifier();
		
		final Orientation orientation = robo.getPosition().getOrientation();
		final IEntityModifier rotationModifier = new RotationModifier(entity, interval, entity.getRotation(), ModelUtil.calculateRotation(orientation));
		
		entity.registerModifier(rotationModifier);
		
		final float fieldWidth = pref.getFloat(PrefKey.FIELD_WIDTH_KEY);
		final float fieldHeight = pref.getFloat(PrefKey.FIELD_HEIGHT_KEY);
		
		final float factorX = calcFactorX(orientation);
		final float factorY = calcFactorY(orientation);
		final float progress = rawProgress / 1000f;
		
		final float offsetX = fieldWidth/2 - entity.getWidth()/2;
		final float fieldXOld = robo.getPosition().getX() * fieldWidth + offsetX;
		final float fieldXNew = (robo.getPosition().getX() + factorX) * fieldWidth + offsetX;
		final float newRenderX = fieldXOld + calcFactorX(orientation) * Math.abs(fieldXOld - fieldXNew) * progress;
		
		final float offsetY = fieldHeight/2 - entity.getHeight()/2;
		final float fieldYOld = robo.getPosition().getY() * fieldHeight + offsetY;
		final float fieldYNew = (robo.getPosition().getY() + factorY) * fieldHeight + offsetY;
		final float newRenderY = fieldYOld + calcFactorY(orientation) * Math.abs(fieldYOld - fieldYNew) * progress;

		entity.registerModifier(new MoveModifierX(entity, interval, entity.getPositionX(), newRenderX));
		entity.registerModifier(new MoveModifierY(entity, interval, entity.getPositionY(), newRenderY));
	}
		
	/**
	 * Helper, which calculates the factor of an value, which should be added to the x position of an entity.
	 * 
	 * @param orientation orientation
	 * @return 0(ns),1(e),-1(w)
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
	 * Helper, which calculates the factor of an value, which should be added to the y position of an entity.
	 * 
	 * @param orientation orientation
	 * @return 0(ew),-1(n),1(s)
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
	public void update() {
		// event only component
	}

	@Override
	public void draw(final Batch batch) {
		// event only component
	}

}
