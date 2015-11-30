package de.unihannover.swp2015.robots2.visual.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;
import de.unihannover.swp2015.robots2.visual.core.IGameHandler;
import de.unihannover.swp2015.robots2.visual.core.PrefConst;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.resource.ResConst;
import de.unihannover.swp2015.robots2.visual.util.StageUtil;
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
	
	private final IStage parent;
	private final IField model;
	private final TextureRegion[] texWall;
	private final Resource food;
	
	private TextureRegion field;
	private float fieldRotation = 0;
	
	public Field(final IStage parent, final IField model, final SpriteBatch renderer, final IGameHandler gameHandler, final IPreferences prefs, final IResourceHandler resHandler){
		super(renderer, gameHandler, prefs, resHandler);
		
		this.model = model;
		this.model.observe(this);
		this.parent = parent;
		this.observerNeighbours();
		
		this.food = new Resource(model, renderer, gameHandler, prefs, resHandler);
		this.texWall = resHandler.getRegion(ResConst.DEFAULT_WALL_N, ResConst.DEFAULT_WALL_E, ResConst.DEFAULT_WALL_S, ResConst.DEFAULT_WALL_W); 
		this.field = resHandler.getRegion(ResConst.DEFAULT_FIELD); 

		final float fieldWidth = prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 50);
		final float fieldHeight = prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 50);
		
		this.renderX = model.getX() * fieldWidth;
		this.renderY = model.getY() * fieldHeight;

		this.determineFieldTexture();
	}
	
	private void observerNeighbours() {
		if (model.getX() > 0)
			parent.getField(model.getX()-1, model.getY()).observe(this);
		if (model.getX()+1 < parent.getWidth())
			parent.getField(model.getX()+1, model.getY()).observe(this);

		if (model.getY() > 0)
			parent.getField(model.getX(), model.getY()-1).observe(this);
		if (model.getY()+1 < parent.getHeight())
			parent.getField(model.getX(), model.getY()+1).observe(this);
	
	}
	
	private void determineFieldTexture() {

		//3:SOUTH  2:NORTH  1:WEST  0:EAST
		final boolean[] dir = new boolean[4];
		final Orientation[] enums = new Orientation[4];
		
		enums[0] = Orientation.EAST;
		enums[1] = Orientation.WEST;
		enums[2] = Orientation.NORTH;
		enums[3] = Orientation.SOUTH;
		
		for (int i = 0; i < 4; ++i) {
			dir[i] = StageUtil.checkDriveDirectionPlusNeighbours(model, parent, enums[i]);
		}
			
		switch(StageUtil.convertToInt(dir)) {
			case 0:
				field = resHandler.getRegion(ResConst.DEFAULT_FIELD_4);
				fieldRotation = 0;
				break;
			case 1:
				field = resHandler.getRegion(ResConst.DEFAULT_FIELD_3_E);
				fieldRotation = -90;
				break;
			case 10:
				field = resHandler.getRegion(ResConst.DEFAULT_FIELD_3_W);
				fieldRotation = 90;
				break;
			case 11:
				field = resHandler.getRegion(ResConst.DEFAULT_FIELD_2_E);
				fieldRotation = 90;
				break;
			case 100:
				field = resHandler.getRegion(ResConst.DEFAULT_FIELD_3_N);
				fieldRotation = 180;
				break;
			case 101:
				field = resHandler.getRegion(ResConst.DEFAULT_FIELD_C_NE);
				fieldRotation = -90;
				break;
			case 110:
				field = resHandler.getRegion(ResConst.DEFAULT_FIELD_C_WN);
				fieldRotation = 180;
				break;
			case 111:
				field = resHandler.getRegion(ResConst.DEFAULT_FIELD_1_S);
				fieldRotation = 180;
				break;
			case 1000:
				field = resHandler.getRegion(ResConst.DEFAULT_FIELD_3_S);
				fieldRotation = 0;
				break;
			case 1001:
				field = resHandler.getRegion(ResConst.DEFAULT_FIELD_C_ES);
				fieldRotation = 0;
				break;
			case 1010:
				field = resHandler.getRegion(ResConst.DEFAULT_FIELD_C_SW);
				fieldRotation = 90;
				break;
			case 1011:
				field = resHandler.getRegion(ResConst.DEFAULT_FIELD_1_N);
				fieldRotation = 0;
				break;
			case 1100:
				field = resHandler.getRegion(ResConst.DEFAULT_FIELD_2_N);
				fieldRotation = 0;
				break;
			case 1101:
				field = resHandler.getRegion(ResConst.DEFAULT_FIELD_1_W);
				fieldRotation = -90;
				break;
			case 1110:
				field = resHandler.getRegion(ResConst.DEFAULT_FIELD_1_E);
				fieldRotation = 90;
				break;
			case 1111:
				//replace with no way
				field = resHandler.getRegion(ResConst.DEFAULT_FIELD);
				fieldRotation = 0;
				break;
		}
	}
	
	@Override
	public void render() {
		
		final float fieldWidth = prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 50);
		final float fieldHeight = prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 50);
				
		batch.draw(field, renderX, renderY, fieldWidth/2f, fieldHeight/2f, fieldWidth, fieldHeight, 1f, 1f, fieldRotation);
		
		food.render();
				
		if(model.isWall(IPosition.Orientation.NORTH))
			batch.draw(texWall[0], renderX, renderY, fieldWidth, fieldHeight);
		
		if(model.isWall(IPosition.Orientation.EAST))
			batch.draw(texWall[1], renderX, renderY, fieldWidth, fieldHeight);
		
		if(model.isWall(IPosition.Orientation.SOUTH))
			batch.draw(texWall[2], renderX, renderY, fieldWidth, fieldHeight);
		
		if(model.isWall(IPosition.Orientation.WEST))
			batch.draw(texWall[3], renderX, renderY, fieldWidth, fieldHeight);
	}

	@Override
	public void onModelUpdate(IEvent event) {
		determineFieldTexture();
	}

	@Override
	public void onUpdatePreferences(PreferencesObservable o, IPreferencesKey updatedKey) {
		// TODO Auto-generated method stub
		
	}

}
