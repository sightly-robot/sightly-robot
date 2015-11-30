package de.unihannover.swp2015.robots2.visual.entity;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.visual.core.IGameHandler;
import de.unihannover.swp2015.robots2.visual.core.PrefConst;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.resource.ResConst;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;
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
	private final IRobot model;
	private final TextureRegion robo;
	private final Bubble bubble;
	
	private float width;
	private float height;
	private float direction=0;
		
	public Robot(final IRobot robModel, SpriteBatch batch, IGameHandler gameHandler, IPreferences prefs, IResourceHandler resHandler) {
		super(batch, gameHandler, prefs, resHandler);

		this.model = robModel;
		this.model.observe(this);

		this.bubble = new Bubble(model, batch, gameHandler, prefs, resHandler);
		this.robo = resHandler.getRegion(ResConst.DEFAULT_ROBO_NORTH);
		
		final float fieldWidth = prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 42);
		final float fieldHeight = prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 42);

		this.width = fieldWidth * EntityConst.ROBOT_SCALE;
		this.height = fieldHeight * EntityConst.ROBOT_SCALE;
		
		this.renderX = model.getPosition().getX() * fieldWidth + fieldWidth/2 - width/2;
		this.renderY = model.getPosition().getY() * fieldHeight + fieldHeight/2 - height/2;
	}

	private void changeDirection(){
		switch (model.getPosition().getOrientation()) {
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
	public void onModelUpdate(IEvent event) {
		switch(event.getType()) {
			case ROBOT_ADD:
				break;
			case ROBOT_POSITION:
				final float fieldWidth = prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 42);
				final float fieldHeight = prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 42);
				this.renderX = model.getPosition().getX() * fieldWidth + fieldWidth/2 - width/2;
				this.renderY = model.getPosition().getY() * fieldHeight + fieldHeight/2 - height/2;
				changeDirection();
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
	}

}
