package de.unihannover.swp2015.robots2.visual.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
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
	// private final TextureRegion tex;
	private final TextureRegion[] roboTex;

	public Robot(final IRobot robModel, SpriteBatch batch, IGameHandler gameHandler, IPreferences prefs, IResourceHandler resHandler) {
		super(batch, gameHandler, prefs, resHandler);

		this.model = robModel;
		this.model.observe(this);

		this.roboTex = resHandler.getRegion(ResConst.DEFAULT_ROBO_SOUTH, ResConst.DEFAULT_ROBO_NORTH,
				ResConst.DEFAULT_ROBO_WEST, ResConst.DEFAULT_ROBO_EAST);

		this.renderX = model.getPosition().getX() * prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 42);
		this.renderY = model.getPosition().getY() * prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 42);
		System.out.println(model.getPosition().getX());
	}

	@Override
	public void render() {
		
		final Orientation orientation = model.getPosition().getOrientation();
		final float fieldWidth = prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 10);
		final float fieldHeight = prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 10);
		
		batch.begin();
		switch (orientation) {
		case SOUTH:
			batch.draw(roboTex[0], renderX, renderY, fieldWidth, fieldHeight);
			break;
		case NORTH:
			batch.draw(roboTex[1], renderX, renderY, fieldWidth, fieldHeight);
			break;
		case WEST:
			batch.draw(roboTex[2], renderX, renderY, fieldWidth, fieldHeight);
			break;
		case EAST:
			batch.draw(roboTex[3], renderX, renderY, fieldWidth, fieldHeight);
			break;
		}
		batch.end();
	}

	@Override
	public void onModelUpdate(IEvent event) {
		switch(event.getType()) {
		case ROBOT_ADD:
			break;
		case ROBOT_POSITION:
			this.renderX = model.getPosition().getX() * prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 42);
			this.renderY = model.getPosition().getY() * prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 42);
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
		if (updatedKey.getEnum() == PrefConst.FIELD_WIDTH_KEY || updatedKey.getEnum() == PrefConst.FIELD_HEIGHT_KEY) {
			this.renderX = model.getPosition().getX() * prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 42);
			this.renderY = model.getPosition().getY() * prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 42);
		}
	}

}
