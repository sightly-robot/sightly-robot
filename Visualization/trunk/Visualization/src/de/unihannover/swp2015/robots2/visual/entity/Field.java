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
	private final TextureRegion[] texWall;
	private final TextureRegion field;
	
	public Field(final IField model, final SpriteBatch renderer, final IGameHandler gameHandler, final IPreferences prefs, final IResourceHandler resHandler){
		super(renderer, gameHandler, prefs, resHandler);
		
		this.model = model;
		this.model.observe(this);
		
		this.texWall = resHandler.getRegion(ResourceConstants.DEFAULT_WALL_H, ResourceConstants.DEFAULT_WALL_V); 
		this.field = resHandler.getRegion(ResourceConstants.DEFAULT_FIELD); 

		final float fieldWidth = prefs.getInt(PreferencesConstants.FIELD_WIDTH_KEY, 10);
		final float fieldHeight = prefs.getInt(PreferencesConstants.FIELD_HEIGHT_KEY, 10);
		
		this.renderX = model.getX() * fieldWidth;
		this.renderY = model.getY() * fieldHeight;
	}

	@Override
	public void render() {
		
		final float fieldWidth = prefs.getInt(PreferencesConstants.FIELD_WIDTH_KEY, 10);
		final float fieldHeight = prefs.getInt(PreferencesConstants.FIELD_HEIGHT_KEY, 10);
		final float wallThickness = prefs.getInt(PreferencesConstants.WALL_THICK_KEY, 10);

		batch.begin();
		//TODO render field
		if(model.isWall(IPosition.Orientation.NORTH))
			batch.draw(texWall[0], renderX, renderY, fieldWidth, wallThickness);
		
		if(model.isWall(IPosition.Orientation.EAST))
			batch.draw(texWall[1], renderX + fieldWidth, renderY, wallThickness, fieldHeight);
		
		if(model.isWall(IPosition.Orientation.SOUTH))
			batch.draw(texWall[0], renderX, renderY + fieldHeight, fieldWidth, wallThickness);
		
		if(model.isWall(IPosition.Orientation.WEST))
			batch.draw(texWall[1], renderX, renderY, wallThickness, fieldHeight);
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
