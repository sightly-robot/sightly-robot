package de.unihannover.swp2015.robots2.visual.game.entity;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent.UpdateType;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;
import de.unihannover.swp2015.robots2.visual.core.PrefKey;
import de.unihannover.swp2015.robots2.visual.core.entity.Entity;
import de.unihannover.swp2015.robots2.visual.game.RobotGameHandler;
import de.unihannover.swp2015.robots2.visual.resource.ResConst;
import de.unihannover.swp2015.robots2.visual.resource.texture.RenderUnit;
import de.unihannover.swp2015.robots2.visual.util.StageUtil;

/**
 * An entity used for the visualization of the underground and walls
 * 
 * @version 1.0
 * @author Daphne Schössow
 */
public class Field extends Entity {

	/** Lookup table for the ground texture, which depends on the placement of walls. */
	private static final ResConst[] FIELD_TEXTURE_LOOKUP = { ResConst.DEFAULT_FIELD_4, ResConst.DEFAULT_FIELD_3,
			ResConst.DEFAULT_FIELD_3, ResConst.DEFAULT_FIELD_2, ResConst.DEFAULT_FIELD_3,
			ResConst.DEFAULT_FIELD_2_CURVE, ResConst.DEFAULT_FIELD_2_CURVE, ResConst.DEFAULT_FIELD_1,
			ResConst.DEFAULT_FIELD_3, ResConst.DEFAULT_FIELD_2_CURVE, ResConst.DEFAULT_FIELD_2_CURVE,
			ResConst.DEFAULT_FIELD_1, ResConst.DEFAULT_FIELD_2, ResConst.DEFAULT_FIELD_1,
			ResConst.DEFAULT_FIELD_1, ResConst.DEFAULT_FIELD };

	/** Lookup table for the rotation. */
	private static final int[] FIELD_ROTATION_LOOKUP = { 0, -90, 90, 90, 180, -90, 180, 180, 0, 0, 90, 0, 0, -90, 90, 0 };

	/** Stage, the field belongs to. */
	private final IStage parent;
	
	/** Visual representation of the ground. */
	private RenderUnit fieldUnit;
	
	/** width of the field */
	private float fieldWidth;
	
	/** height of the field */
	private float fieldHeight;
		
	/** Contains all orientation in a specific order (I do not use Enum.values() to be sure that the order is like I'm expecting) */
	private Orientation[] orientations = {Orientation.EAST, Orientation.WEST, Orientation.NORTH, Orientation.SOUTH};
	
	/**
	 * Constructs a field-entity, which belongs to <code>parent</code>.
	 * 
	 * @param parent stage, the <code>model</code> is part of
	 * @param model data model of the field
	 * @param renderer batch, which should be used to render the entity
	 * @param gameHandler {@link IGameHandler}, which should own this entity.
	 */
	public Field(final IStage parentStage, final IField model, final RobotGameHandler gameHandler) {
		super(model, gameHandler);

		this.parent = parentStage;
		this.fieldUnit = resHandler.createRenderUnit(ResConst.DEFAULT_FIELD);
		this.fieldWidth = prefs.getFloat(PrefKey.FIELD_WIDTH_KEY, 50);
		this.fieldHeight = prefs.getFloat(PrefKey.FIELD_HEIGHT_KEY, 50);
		
		this.renderX = model.getX() * fieldWidth;
		this.renderY = model.getY() * fieldHeight;

		this.determineFieldTexture(model);
	}
	
	/**
	 * Updates {@link #fieldUnit} and {@link #rotation}.
	 * 
	 * @param model data model of the field.
	 */
	private void determineFieldTexture(IField model) {

		//3:SOUTH  2:NORTH  1:WEST  0:EAST
		final boolean[] dir = new boolean[4];
		
		//checks for every direction if there is a double wall.
		for (int i = 0; i < orientations.length; ++i) {
			dir[i] = StageUtil.checkDriveDirectionAndNeighbours(model, parent, orientations[i]);
		}
		
		//selects the correct texture + rotation depending on the booleans
		final int result = StageUtil.convertToInt(dir, 2);
		fieldUnit = resHandler.createRenderUnit(FIELD_TEXTURE_LOOKUP[result]);
		rotation = FIELD_ROTATION_LOOKUP[result];
	}
	
	@Override
	public void draw(final Batch batch) {
		super.draw(batch);
				
		// the following switch corrects the drawn bounds of the field when rotation is 90° or -90°
		// it's just relevant if fieldWidth != fieldHeight
		switch((int) rotation) {
		
		case -90:
			fieldUnit.draw(batch, renderX-(fieldWidth-fieldHeight)/2, renderY-(fieldWidth-fieldHeight)/2, 
					fieldHeight, fieldWidth, fieldWidth/2f, fieldHeight/2f, 1f, 1f, rotation);
			break;
			
		case 90:
			fieldUnit.draw(batch, renderX+(fieldWidth-fieldHeight)/2, renderY+(fieldWidth-fieldHeight)/2, 
					fieldHeight, fieldWidth, fieldWidth/2f, fieldHeight/2f, 1f, 1f, rotation);
			break;
			
		default:
			fieldUnit.draw(batch, renderX, renderY, fieldWidth, fieldHeight, fieldWidth/2f, fieldHeight/2f, 1f, 1f, rotation);
			break;
		}
	}
	
	@Override
	public void onManagedModelUpdate(IEvent event) {
		final IField field = (IField) model;
		
		if (event.getType() == UpdateType.STAGE_WALL) {
			determineFieldTexture(field);
		}		
	}

	@Override
	public void onUpdatePreferences(PrefKey updatedKey, Object value) {
		switch(updatedKey) {
		
		case FIELD_WIDTH_KEY:
			fieldWidth = (float) value;
			break;
			
		case FIELD_HEIGHT_KEY:
			fieldHeight = (float) value;
			break;
			
		default:
			break;
		}
	}

}
