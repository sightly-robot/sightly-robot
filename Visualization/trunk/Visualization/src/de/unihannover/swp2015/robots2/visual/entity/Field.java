package de.unihannover.swp2015.robots2.visual.entity;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;
import de.unihannover.swp2015.robots2.visual.core.PrefConst;
import de.unihannover.swp2015.robots2.visual.core.RobotGameHandler;
import de.unihannover.swp2015.robots2.visual.resource.ResConst;
import de.unihannover.swp2015.robots2.visual.util.StageUtil;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesKey;
import de.unihannover.swp2015.robots2.visual.util.pref.observer.PreferencesObservable;

/**
 * An entity used for the visualization of the underground and walls
 * 
 * @version 1.0
 * @author Daphne Sch�ssow
 */
public class Field extends Entity {

	/**
	 * Lookup table for the ground texture, which depends on the placement of walls. 
	 */
	private static final ResConst[] FIELD_TEXTURE_LOOKUP = { ResConst.DEFAULT_FIELD_4, ResConst.DEFAULT_FIELD_3_E,
			ResConst.DEFAULT_FIELD_3_W, ResConst.DEFAULT_FIELD_2_E, ResConst.DEFAULT_FIELD_3_N,
			ResConst.DEFAULT_FIELD_C_NE, ResConst.DEFAULT_FIELD_C_WN, ResConst.DEFAULT_FIELD_1_S,
			ResConst.DEFAULT_FIELD_3_S, ResConst.DEFAULT_FIELD_C_ES, ResConst.DEFAULT_FIELD_C_SW,
			ResConst.DEFAULT_FIELD_1_N, ResConst.DEFAULT_FIELD_2_N, ResConst.DEFAULT_FIELD_1_W,
			ResConst.DEFAULT_FIELD_1_E, ResConst.DEFAULT_FIELD };

	/**
	 * Lookup table for the rotation.
	 */
	private static final int[] FIELD_ROTATION_LOOKUP = { 0, -90, 90, 90, 180, -90, 180, 180, 0, 0, 90, 0, 0, -90, 90, 0 };

	/**
	 * Stage, the field belongs to.
	 */
	private final IStage parent;
	
	/**
	 * Visual representations of the walls.
	 */
	private final TextureRegion[] texWall;
	
	/**
	 * Resource as entity, based on this <code>model</code>.
	 */
	private final Resource food;
	
	/**
	 * Visual representation of the ground.
	 */
	private TextureRegion field;
	
	private final TextureRegion startpos;
	
	private boolean startp = false;
	
	/**
	 * Constructs a field-entity, which belongs to <code>parent</code>.
	 * 
	 * @param parent stage, the <code>model</code> is part of
	 * @param model data model of the field
	 * @param renderer batch, which should be used to render the entity
	 * @param gameHandler {@link IGameHandler}, which should own this entity.
	 */
	public Field(final IStage parent, final IField model, final RobotGameHandler gameHandler){
		super(model, gameHandler);
		
		this.model = model;
		this.model.observe(this);
		this.parent = parent;
		
		this.food = new Resource(model, gameHandler);
		this.texWall = resHandler.getRegion(ResConst.DEFAULT_WALL_N, ResConst.DEFAULT_WALL_E, ResConst.DEFAULT_WALL_S, ResConst.DEFAULT_WALL_W); 
		this.field = resHandler.getRegion(ResConst.DEFAULT_FIELD); 
		this.startpos = resHandler.getRegion(ResConst.DEFAULT_STARTPOS);

		final float fieldWidth = prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 50);
		final float fieldHeight = prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 50);
		
		this.renderX = model.getX() * fieldWidth;
		this.renderY = model.getY() * fieldHeight;

		this.determineFieldTexture(model);
		this.drawStartPositions();
		
	}
	
	private void drawStartPositions(){
		final List<IPosition> startposition = parent.getStartPositions();
		for (IPosition pos : startposition) {
			if (pos.getX() == renderX && pos.getY() == renderY) {
				this.startp = true;
				switch(pos.getOrientation()){
				case SOUTH:
					this.rotation = 180;
					break;
				case NORTH:
					this.rotation = 0;
					break;
				case WEST:
					this.rotation = -90;
					break;
				case EAST:
					this.rotation = 90;
					break;
				default:
					break;
				}
				break;
			}
		}
	}
	
	/**
	 * Updates {@link #field} and {@link #rotation}.
	 * 
	 * @param model data model of the field.
	 */
	private void determineFieldTexture(final IField model) {

		//3:SOUTH  2:NORTH  1:WEST  0:EAST
		final boolean[] dir = new boolean[4];
		final Orientation[] enums = new Orientation[4];
		
		//have to ensure that the order is exactly like I'm expecting
		enums[0] = Orientation.EAST;
		enums[1] = Orientation.WEST;
		enums[2] = Orientation.NORTH;
		enums[3] = Orientation.SOUTH;
		
		//checks for every direction if there is a double wall.
		for (int i = 0; i < 4; ++i) {
			dir[i] = StageUtil.checkDriveDirectionAndNeighbours(model, parent, enums[i]);
		}
		
		//selects the correct texture + rotation depending on the booleans
		final int result = StageUtil.convertToInt(dir, 2);
		this.field = resHandler.getRegion(FIELD_TEXTURE_LOOKUP[result]);
		this.rotation = FIELD_ROTATION_LOOKUP[result];
	}
	
	@Override
	public void draw(final Batch batch) {
		
		
		
		final float fieldWidth = prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 50);
		final float fieldHeight = prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 50);
		
		if (rotation == -90)
			batch.draw(field, renderX-(fieldWidth-fieldHeight)/2, renderY-(fieldWidth-fieldHeight)/2, fieldWidth/2f, fieldHeight/2f, fieldHeight, fieldWidth, 1f, 1f, rotation);
		else if (rotation == 90)
			batch.draw(field, renderX+(fieldWidth-fieldHeight)/2, renderY+(fieldWidth-fieldHeight)/2, fieldWidth/2f, fieldHeight/2f, fieldHeight, fieldWidth, 1f, 1f, rotation);
		else
			batch.draw(field, renderX, renderY, fieldWidth/2f, fieldHeight/2f, fieldWidth, fieldHeight, 1f, 1f, rotation);

		food.draw(batch);
				
		final IField field = (IField) model;
		
		if(field.isWall(IPosition.Orientation.NORTH))
			batch.draw(texWall[0], renderX, renderY, fieldWidth, fieldHeight);
		
		if(field.isWall(IPosition.Orientation.EAST))
			batch.draw(texWall[1], renderX, renderY, fieldWidth, fieldHeight);
		
		if(field.isWall(IPosition.Orientation.SOUTH))
			batch.draw(texWall[2], renderX, renderY, fieldWidth, fieldHeight);
		
		if(field.isWall(IPosition.Orientation.WEST))
			batch.draw(texWall[3], renderX, renderY, fieldWidth, fieldHeight);
		
		if(startp) // TODO stop showing when game started or robot is positioned
			batch.draw(startpos, renderX, renderY, fieldWidth, fieldHeight);
	}
	
	@Override
	public void onManagedModelUpdate(IEvent event) {
		final IField field = (IField) model;
		
		switch(event.getType()) {
		case STAGE_WALL:
			determineFieldTexture(field);
			break;
		case ROBOT_ADD:
			drawStartPositions();
			break;
		default:
			break;
		}		
	}

	@Override
	public void onUpdatePreferences(PreferencesObservable o, IPreferencesKey updatedKey) {
		// TODO Auto-generated method stub
		
	}

}
