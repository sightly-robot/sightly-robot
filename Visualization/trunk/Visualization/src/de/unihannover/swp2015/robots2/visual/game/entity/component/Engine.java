package de.unihannover.swp2015.robots2.visual.game.entity.component;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.visual.core.PrefConst;
import de.unihannover.swp2015.robots2.visual.core.entity.Component;
import de.unihannover.swp2015.robots2.visual.core.entity.IEntity;
import de.unihannover.swp2015.robots2.visual.core.entity.IEntityModifier;
import de.unihannover.swp2015.robots2.visual.game.entity.modifier.MoveModifierX;
import de.unihannover.swp2015.robots2.visual.game.entity.modifier.MoveModifierY;
import de.unihannover.swp2015.robots2.visual.game.entity.modifier.RotationModifier;
import de.unihannover.swp2015.robots2.visual.util.ModelUtil;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;

public class Engine extends Component {
	
	/**
	 * Interval time used for the first calculation of the robot speed (in seconds).
	 */
	private static final float FIRST_INTERVAL_TIME = 0.1f;
	
	private float interval;
	private long lastEvent;
	private boolean firstEvent;

	private final IPreferences pref;
	
	public Engine(final IPreferences pref) {
		this.pref = pref;
	}
	
	@Override
	public void onRegister(final IEntity entity) {
		super.onRegister(entity);
		
		interval = FIRST_INTERVAL_TIME;
	}

	@Override
	public void onEvent(IEvent event) {
		switch(event.getType()) {

		case ROBOT_PROGRESS:
			this.calcInterval();			
			final IRobot robot = (IRobot) event.getObject();
			this.updateRobot(robot, robot.getPosition().getProgress());
			break;
			
		case ROBOT_POSITION:
			this.updateRobot((IRobot) event.getObject(), 1000);
			break;
			
		default:
			break;
		
		}
	}
	
	private void calcInterval() {
		if (!firstEvent) {
			firstEvent = true;
		}
		else {
			interval = (System.currentTimeMillis() - lastEvent) / 1000f;
		}
		this.lastEvent = System.currentTimeMillis();
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
		
		final float fieldWidth = pref.getFloat(PrefConst.FIELD_WIDTH_KEY);
		final float fieldHeight = pref.getFloat(PrefConst.FIELD_HEIGHT_KEY);
		
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
