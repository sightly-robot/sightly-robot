package de.unihannover.swp2015.robots2.visual.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.visual.core.IGameHandler;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;
import de.unihannover.swp2015.robots2.visual.util.pref.observer.PreferencesObservable;

/**
 * An entity used for the visualization of robots
 * 
 * @version 0.1
 * @author Daphne Schössow
 */
public class Robot extends Entity {
		
	//private boolean isVirtual; //TODO Einbauen
	private final IRobot model;
	private final TextureRegion tex;
	
	//TODO perhaps we have to remember the old position to choose the correct animation
	private int oldX;
	private int oldY;
	
	public Robot(final IRobot robMode, SpriteBatch batch, IGameHandler gameHandler, IPreferences prefs, IResourceHandler resHandler) {
		super(batch, gameHandler, prefs, resHandler);
		
		this.model = robMode;
		this.tex = resHandler.getRegion(""); //TODO key
	}
			
	@Override
	public void render() {
		batch.begin();
		
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
