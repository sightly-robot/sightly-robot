package de.unihannover.swp2015.robots2.visual.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.visual.core.IGameHandler;
import de.unihannover.swp2015.robots2.visual.core.PrefConst;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.resource.ResConst;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesKey;
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
	private final TextureRegion[] field;
	
	public Field(final IField model, final SpriteBatch renderer, final IGameHandler gameHandler, final IPreferences prefs, final IResourceHandler resHandler){
		super(renderer, gameHandler, prefs, resHandler);
		
		this.model = model;
		this.model.observe(this);
		
		this.texWall = resHandler.getRegion(ResConst.DEFAULT_WALL_N, ResConst.DEFAULT_WALL_E, ResConst.DEFAULT_WALL_S, ResConst.DEFAULT_WALL_W); 
		this.field = resHandler.getRegion(ResConst.DEFAULT_FIELD, ResConst.DEFAULT_FIELD_1_N, ResConst.DEFAULT_FIELD_1_E, ResConst.DEFAULT_FIELD_1_S, ResConst.DEFAULT_FIELD_1_W,
				ResConst.DEFAULT_FIELD_2_N, ResConst.DEFAULT_FIELD_2_E,
				ResConst.DEFAULT_FIELD_3_N, ResConst.DEFAULT_FIELD_3_E, ResConst.DEFAULT_FIELD_3_S, ResConst.DEFAULT_FIELD_3_W,
				ResConst.DEFAULT_FIELD_4,
				ResConst.DEFAULT_FIELD_C_NE, ResConst.DEFAULT_FIELD_C_ES, ResConst.DEFAULT_FIELD_C_SW, ResConst.DEFAULT_FIELD_C_WN); 

		final float fieldWidth = prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 50);
		final float fieldHeight = prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 50);
		
		this.renderX = model.getX() * fieldWidth;
		this.renderY = model.getY() * fieldHeight;
	}

	@Override
	public void render() {
		
		final float fieldWidth = prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 50);
		final float fieldHeight = prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 50);
		final float wallThickness = prefs.getInt(PrefConst.WALL_THICK_KEY, 50);

		batch.draw(field[1], renderX, renderY, fieldWidth, fieldHeight);
		
		//TODO render field
		if(model.isWall(IPosition.Orientation.NORTH))
			batch.draw(texWall[0], renderX, renderY, fieldWidth, fieldHeight);
		
		if(model.isWall(IPosition.Orientation.EAST))
			batch.draw(texWall[1], renderX + fieldWidth - wallThickness, renderY, fieldWidth, fieldHeight);
		
		if(model.isWall(IPosition.Orientation.SOUTH))
			batch.draw(texWall[0], renderX, renderY + fieldHeight - wallThickness, fieldWidth, fieldHeight);
		
		if(model.isWall(IPosition.Orientation.WEST))
			batch.draw(texWall[1], renderX, renderY, fieldWidth, fieldHeight);
	}

	@Override
	public void onModelUpdate(IEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUpdatePreferences(PreferencesObservable o, IPreferencesKey updatedKey) {
		// TODO Auto-generated method stub
		
	}

}
