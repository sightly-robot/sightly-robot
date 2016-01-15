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
import de.unihannover.swp2015.robots2.visual.resource.texture.TransformedRenderUnit;
import de.unihannover.swp2015.robots2.visual.util.StageUtil;

/**
 * Describes the walls of one field.
 * 
 * @author Rico Schrage
 */
public class Wall extends Entity {

	/** Stage, the field belongs to. */
	private final IStage parent;
	
	/** Visual representations of the walls. */
	private final TransformedRenderUnit[] texWall;
	
	/** width of the field */
	private float fieldWidth;
	
	/** height of the field */
	private float fieldHeight;
	
	/** Can mark walls as one-way road (0=EAST, 1=WEST, 2=NORTH, 3=SOUTH) */
	private boolean[] isOneway = {false, false, false, false};
	
	/** Contains all orientation in a specific order (I do not use Enum.values() to be sure that the order is like I'm expecting) */
	private Orientation[] orientations = {Orientation.EAST, Orientation.WEST, Orientation.NORTH, Orientation.SOUTH};
	
	/**
	 * Constructs a wall-entity, which belongs to <code>parent</code>.
	 * 
	 * @param parent stage, the <code>model</code> is part of
	 * @param model data model of the field
	 * @param renderer batch, which should be used to render the entity
	 * @param gameHandler {@link IGameHandler}, which should own this entity.
	 */
	public Wall(final IStage parentStage, final IField model, final RobotGameHandler gameHandler) {
		super(model, gameHandler);

		this.parent = parentStage;
		
		this.texWall = new TransformedRenderUnit[4];
		this.fieldWidth = prefs.getFloat(PrefKey.FIELD_WIDTH_KEY, 50);
		this.fieldHeight = prefs.getFloat(PrefKey.FIELD_HEIGHT_KEY, 50);
		
		this.renderX = model.getX() * fieldWidth;
		this.renderY = model.getY() * fieldHeight;

		this.determineOneway(model);
		this.createRenderUnits(model);
	}
	
	/**
	 * Creates all transformed render units.
	 * 
	 * @param model field model of this entity
	 */
	private void createRenderUnits(IField model) {
		final RenderUnit wallTexture = resHandler.createRenderUnit(ResConst.DEFAULT_WALL);
		final RenderUnit onewayTexture = resHandler.createRenderUnit(ResConst.DEFAULT_ONEWAY);
		
		final float wallWidth = wallTexture.getWidth()*fieldHeight/wallTexture.getHeight(); 
		final float wallHeight = fieldHeight;
		
		texWall[0] = new TransformedRenderUnit(isOneway[0] ? onewayTexture : wallTexture, renderX + fieldWidth - wallWidth/2, renderY, 
				wallWidth, wallHeight);
		texWall[1] = new TransformedRenderUnit(isOneway[1] ? onewayTexture : wallTexture, renderX - wallWidth/2, renderY, 
				wallWidth, wallHeight);
		texWall[2] = new TransformedRenderUnit(isOneway[2] ? onewayTexture : wallTexture, renderX, renderY + wallWidth/2, 
				wallWidth, wallHeight, 0, 0, -90);
		texWall[3] = new TransformedRenderUnit(isOneway[3] ? onewayTexture : wallTexture, renderX, renderY + fieldHeight + wallWidth/2, 
				wallWidth, wallHeight, 0, 0, -90);
	}
	
	private void determineOneway(IField model) {
		// updates informations about the type of the walls.
		for (int i = 0; i < orientations.length; ++i) {
			isOneway[i] = StageUtil.checkDriveDirectionAndNotNeighbours(model, parent, orientations[i]);
		}
	}
	
	@Override
	public void draw(final Batch batch) {
		super.draw(batch);
				
		final IField field = (IField) model;

		for (int i = 0; i < orientations.length; ++i) {
			if (field.isWall(orientations[i])) {
				texWall[i].draw(batch);
			}
		}
	}
	
	@Override
	public void onManagedModelUpdate(IEvent event) {
		final IField field = (IField) model;
		
		if (event.getType() == UpdateType.STAGE_WALL) {
			determineOneway(field);
			createRenderUnits(field);
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
