package de.unihannover.swp2015.robots2.visual.entity;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.visual.core.IGameHandler;
import de.unihannover.swp2015.robots2.visual.core.PrefConst;
import de.unihannover.swp2015.robots2.visual.resource.ResConst;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesKey;
import de.unihannover.swp2015.robots2.visual.util.pref.observer.PreferencesObservable;

/**
 * An entity used for the visualization of robots
 * 
 * @version 0.2
 * @author Daphne Schössow
 */
public class Robot extends Entity {

	// private boolean isVirtual; //TODO Einbauen
	private final TextureRegion robo;
	private final Bubble bubble;
	
	private float width;
	private float height;
	private float direction=0;
		
	public Robot(final IRobot robModel, SpriteBatch batch, IGameHandler gameHandler) {
		super(robModel, batch, gameHandler);

		this.bubble = new Bubble(robModel, batch, gameHandler);
		this.robo = resHandler.getRegion(ResConst.DEFAULT_ROBO_NORTH);
		
		final float fieldWidth = prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 42);
		final float fieldHeight = prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 42);

		this.width = fieldWidth * EntityConst.ROBOT_SCALE;
		this.height = fieldHeight * EntityConst.ROBOT_SCALE;
		
		this.renderX = robModel.getPosition().getX() * fieldWidth + fieldWidth/2 - width/2;
		this.renderY = robModel.getPosition().getY() * fieldHeight + fieldHeight/2 - height/2;
	}

	private void updateDirection(final IRobot robo){
		switch (robo.getPosition().getOrientation()) {
		case SOUTH:
			direction=180;
			break;
		case NORTH:
			direction=0;
			break;
		case WEST:
			direction=90;
			break;
		case EAST:
			direction=-90;
			break;
		}
	}
	
	@Override
	public void render() {
		batch.draw(robo, renderX, renderY, width/2f, height/2f, width, height, 1f, 1f, direction);	
		bubble.render();
	}

	@Override
	public void onManagedModelUpdate(IEvent event) {
		IRobot robo = (IRobot) model;
		
		switch(event.getType()) {
			case ROBOT_ADD:
				break;
			case ROBOT_POSITION:
				final float fieldWidth = prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 42);
				final float fieldHeight = prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 42);
				this.renderX = robo.getPosition().getX() * fieldWidth + fieldWidth/2 - width/2;
				this.renderY = robo.getPosition().getY() * fieldHeight + fieldHeight/2 - height/2;
				updateDirection(robo);
				break;
			case ROBOT_SCORE:
				break;
			case ROBOT_STATE:
				break;
			default:
				break;
		}		
	}

	@Override
	public void onUpdatePreferences(PreferencesObservable o, IPreferencesKey updatedKey) {
		if (updatedKey.getEnum() == PrefConst.FIELD_HEIGHT_KEY || updatedKey.getEnum() == PrefConst.FIELD_WIDTH_KEY) {
			final float fieldWidth = prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 42);
			final float fieldHeight = prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 42);

			this.width = fieldWidth * EntityConst.ROBOT_SCALE;
			this.height = fieldHeight * EntityConst.ROBOT_SCALE;
			
			final IRobot r = (IRobot) model;
			this.renderX = r.getPosition().getX() * fieldWidth + fieldWidth/2 - width/2;
			this.renderY = r.getPosition().getY() * fieldHeight + fieldHeight/2 - height/2;
		}
	}

}
