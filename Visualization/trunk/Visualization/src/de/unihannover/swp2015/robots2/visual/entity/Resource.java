package de.unihannover.swp2015.robots2.visual.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.visual.core.IGameHandler;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;
import de.unihannover.swp2015.robots2.visual.util.pref.observer.PreferencesObservable;

/**
 * An entity used for the visualization of resources
 * 
 * @version 0.1
 * @author Daphne Schössow
 */
public class Resource extends Entity {

	private IField model;
	private final TextureRegion texRes00;
	private final TextureRegion texRes01;
	private final TextureRegion texRes02;
	private final TextureRegion texRes03;
	private final TextureRegion texRes04;
	private final TextureRegion texRes05;
	private final TextureRegion texRes06;
	private final TextureRegion texRes07;
	private final TextureRegion texRes08;
	private final TextureRegion texRes09;
	private final TextureRegion texRes10;
	
	private int stage;
	
	public Resource(final IField model, SpriteBatch batch, IGameHandler gameHandler, IPreferences prefs, IResourceHandler resHandler) {
		super(batch, gameHandler, prefs, resHandler);
		this.model = model;
		this.texRes00 = new TextureRegion(gameHandler.getTexture()).split(256, 256)[4][2]; 
		this.texRes01 = new TextureRegion(gameHandler.getTexture()).split(256, 256)[0][0]; 
		this.texRes02 = new TextureRegion(gameHandler.getTexture()).split(256, 256)[1][0]; 
		this.texRes03 = new TextureRegion(gameHandler.getTexture()).split(256, 256)[2][0];
		this.texRes04 = new TextureRegion(gameHandler.getTexture()).split(256, 256)[3][0]; 
		this.texRes05 = new TextureRegion(gameHandler.getTexture()).split(256, 256)[4][0]; 
		this.texRes06 = new TextureRegion(gameHandler.getTexture()).split(256, 256)[0][1]; 
		this.texRes07 = new TextureRegion(gameHandler.getTexture()).split(256, 256)[1][1];
		this.texRes08 = new TextureRegion(gameHandler.getTexture()).split(256, 256)[2][1]; 
		this.texRes09 = new TextureRegion(gameHandler.getTexture()).split(256, 256)[3][1]; 
		this.texRes10 = new TextureRegion(gameHandler.getTexture()).split(256, 256)[4][1]; 

		this.renderX = model.getX();
		this.renderY = model.getY();
		this.stage = model.getFood();
	}

	@Override
	public void render() {
		batch.begin();
		switch(stage){
		case 1:
			batch.draw(texRes01, renderX, renderY);
			break;
		case 2:
			batch.draw(texRes02, renderX, renderY);
			break;
		case 3:
			batch.draw(texRes03, renderX, renderY);
			break;
		case 4:
			batch.draw(texRes04, renderX, renderY);
			break;
		case 5:
			batch.draw(texRes05, renderX, renderY);
			break;
		case 6:
			batch.draw(texRes06, renderX, renderY);
			break;
		case 7:
			batch.draw(texRes07, renderX, renderY);
			break;
		case 8:
			batch.draw(texRes08, renderX, renderY);
			break;
		case 9:
			batch.draw(texRes09, renderX, renderY);
			break;
		case 10:
			batch.draw(texRes10, renderX, renderY);
			break;
		default:
			batch.draw(texRes01, renderX, renderY);
			break;
		}
		batch.end();
	}

	@Override
	public void onModelUpdate(IEvent event) {
		if(event.getType() == IEvent.UpdateType.FIELD_FOOD){
			// some animation stuff
		}
	}

	@Override
	public void onUpdatePreferences(PreferencesObservable o, String updatedKey) {
		// TODO Auto-generated method stub
		
	}
}
