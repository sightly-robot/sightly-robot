package de.unihannover.swp2015.robots2.visual.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.visual.core.IGameHandler;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.resource.ResourceConstants;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;
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

	// TODO perhaps we have to remember the old position to choose the correct
	// animation
	private int oldX;
	private int oldY;

	public Robot(final IRobot robMode, SpriteBatch batch, IGameHandler gameHandler, IPreferences prefs, IResourceHandler resHandler) {
		super(batch, gameHandler, prefs, resHandler);

		this.model = robMode;

		this.roboTex = resHandler.getRegion(ResourceConstants.DEFAULT_ROBO_SOUTH, ResourceConstants.DEFAULT_ROBO_NORTH,
				ResourceConstants.DEFAULT_ROBO_WEST, ResourceConstants.DEFAULT_ROBO_EAST);

		this.renderX = model.getPosition().getX();
		this.renderY = model.getPosition().getY();
	}

	@Override
	public void render() {
		
		Orientation orientation = model.getPosition().getOrientation();
				
		batch.begin();
		if (orientation == IPosition.Orientation.NORTH)
			batch.draw(roboTex[1], renderX, renderY);
		else if (orientation == IPosition.Orientation.EAST)
			batch.draw(roboTex[3], renderX, renderY);
		else if (orientation == IPosition.Orientation.SOUTH)
			batch.draw(roboTex[0], renderX, renderY);
		else if (orientation == IPosition.Orientation.WEST)
			batch.draw(roboTex[2], renderX, renderY);
		batch.end();
	}

	@Override
	public void onModelUpdate(IEvent event) {
		if (event.getType() == IEvent.UpdateType.ROBOT_POSITION) { // wie zieh
																	// ich die
																	// Information
																	// da raus?
			// TODO animation stuff
		}
	}

	@Override
	public void onUpdatePreferences(PreferencesObservable o, String updatedKey) {
		// TODO Auto-generated method stub

	}

}
