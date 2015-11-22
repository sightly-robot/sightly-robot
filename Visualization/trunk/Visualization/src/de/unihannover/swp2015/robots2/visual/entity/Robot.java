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
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;
import de.unihannover.swp2015.robots2.visual.util.pref.observer.PreferencesObservable;

/**
 * An entity used for the visualization of robots
 * 
 * @version 0.2
 * @author Daphne Schössow
 */
public class Robot extends Entity {
		
	//private boolean isVirtual; //TODO Einbauen
	private final IRobot model;
	//private final TextureRegion tex;
	private final TextureRegion texRoboN;
	private final TextureRegion texRoboE;
	private final TextureRegion texRoboS;
	private final TextureRegion texRoboW;
	
	//TODO perhaps we have to remember the old position to choose the correct animation
	private int oldX;
	private int oldY;
	private Orientation orientation;
	
	public Robot(final IRobot robMode, SpriteBatch batch, IGameHandler gameHandler, IPreferences prefs, IResourceHandler resHandler) {
		super(batch, gameHandler, prefs, resHandler);
		
		this.model = robMode;
		//this.tex = resHandler.getRegion(""); //TODO key
		this.texRoboN = new TextureRegion(gameHandler.getTexture()).split(256, 256)[0][2]; 
		this.texRoboE = new TextureRegion(gameHandler.getTexture()).split(256, 256)[1][2]; 
		this.texRoboS = new TextureRegion(gameHandler.getTexture()).split(256, 256)[2][2]; 
		this.texRoboW = new TextureRegion(gameHandler.getTexture()).split(256, 256)[3][2];
		this.renderX = model.getPosition().getX();
		this.renderY = model.getPosition().getY();
		this.orientation = model.getPosition().getOrientation();
	}
			
	@Override
	public void render() { //Vorläufig möglicherweise statitisch???
		batch.begin();
		if(orientation == IPosition.Orientation.NORTH)
			batch.draw(texRoboN, renderX, renderY);
		if(orientation == IPosition.Orientation.EAST)
			batch.draw(texRoboE, renderX, renderY);
		if(orientation == IPosition.Orientation.SOUTH)
			batch.draw(texRoboS, renderX, renderY);
		if(orientation == IPosition.Orientation.WEST)
			batch.draw(texRoboW, renderX, renderY);
		batch.end();
	}

	@Override
	public void onModelUpdate(IEvent event) {
		if(event.getType() == IEvent.UpdateType.ROBOT_POSITION){ //wie zieh ich die Information da raus?
			//TODO animation stuff
		}
	}

	@Override
	public void onUpdatePreferences(PreferencesObservable o, String updatedKey) {
		// TODO Auto-generated method stub
		
	}

}
