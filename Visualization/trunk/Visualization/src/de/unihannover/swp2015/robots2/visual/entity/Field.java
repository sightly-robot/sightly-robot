package de.unihannover.swp2015.robots2.visual.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.visual.core.IGameHandler;
import de.unihannover.swp2015.robots2.visual.core.PreferencesConstants;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.resource.ResourceConstants;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;
import de.unihannover.swp2015.robots2.visual.util.pref.observer.PreferencesObservable;

/**
 * An entity used for the visualization of the underground and walls
 * 
 * @version 0.2
 * @author Daphne Schössow
 */
public class Field extends Entity {
	
	private final IField model;
	private final TextureRegion texWallN;
	private final TextureRegion texWallE;
	private final TextureRegion texWallS;
	private final TextureRegion texWallW;
	private final TextureRegion field;
	
	public Field(final IField model, final SpriteBatch renderer, final IGameHandler gameHandler, final IPreferences prefs, final IResourceHandler resHandler){
		super(renderer, gameHandler, prefs, resHandler);
		
		this.model = model;
		this.model.observe(this);
		
		//this.texWallN = resHandler.getRegion(ResourceConstants.DEFAULT_WALL_H); 
		//this.texWallE = resHandler.getRegion(ResourceConstants.DEFAULT_WALL_V); 
		this.texWallN = new TextureRegion(gameHandler.getTexture()).split(256, 256)[0][3]; 
		this.texWallE = new TextureRegion(gameHandler.getTexture()).split(256, 256)[1][3]; 
		this.texWallS = new TextureRegion(gameHandler.getTexture()).split(256, 256)[2][3]; 
		this.texWallW = new TextureRegion(gameHandler.getTexture()).split(256, 256)[3][3]; 
		this.field = new TextureRegion(gameHandler.getTexture()).split(256, 256)[4][2]; 
		//this.renderX = model.getX() * prefs.getInt(PreferencesConstants.FIELD_WIDTH_KEY, 100);
		//this.renderY = model.getY() * prefs.getInt(PreferencesConstants.FIELD_HEIGHT_KEY, 100);
		this.renderX = model.getX();
		this.renderY = model.getY();
	}

	@Override
	public void render() {
		batch.begin();
		batch.draw(field, renderX, renderY);
		if(model.isWall(IPosition.Orientation.NORTH))
			batch.draw(texWallN, renderX, renderY);
		if(model.isWall(IPosition.Orientation.EAST))
			batch.draw(texWallE, renderX, renderY);
		if(model.isWall(IPosition.Orientation.SOUTH))
			batch.draw(texWallS, renderX, renderY);
		if(model.isWall(IPosition.Orientation.WEST))
			batch.draw(texWallW, renderX, renderY);
		batch.end();
	}

	@Override
	public void onModelUpdate(IEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUpdatePreferences(PreferencesObservable o, String updatedKey) {
		// TODO Auto-generated method stub
		
	}

}
