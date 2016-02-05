package de.sightly_robot.sightly_robot.visual.game.entity;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.sightly_robot.sightly_robot.model.interfaces.IEvent;
import de.sightly_robot.sightly_robot.model.interfaces.IField;
import de.sightly_robot.sightly_robot.model.interfaces.IStage;
import de.sightly_robot.sightly_robot.model.interfaces.IEvent.UpdateType;
import de.sightly_robot.sightly_robot.model.interfaces.IPosition.Orientation;
import de.sightly_robot.sightly_robot.visual.core.PrefKey;
import de.sightly_robot.sightly_robot.visual.core.entity.Entity;
import de.sightly_robot.sightly_robot.visual.game.RobotGameHandler;
import de.sightly_robot.sightly_robot.visual.resource.ResConst;
import de.sightly_robot.sightly_robot.visual.resource.texture.RenderUnit;
import de.sightly_robot.sightly_robot.visual.resource.texture.TransformedRenderUnit;
import de.sightly_robot.sightly_robot.visual.util.StageUtil;

/**
 * an entity for the visualization of walls of one field
 * 
 * @author Daphne Schössow
 */
public class Wall extends Entity<RobotGameHandler, IField> {

	/** stage to which the field belongs */
	private final IStage parent;

	/** visual representations of the walls */
	private final TransformedRenderUnit[] texWall;

	/** width of the field */
	private float fieldWidth;

	/** height of the field */
	private float fieldHeight;

	/** marks walls as one-way road (0=EAST, 1=WEST, 2=NORTH, 3=SOUTH) */
	private boolean[] isOneway = { false, false, false, false };

	/**
	 * contains all orientation in a specific order
	 * 
	 * (I do not use Enum.values() here to be sure that the order is like I'm
	 * expecting)
	 */
	private Orientation[] orientations = { Orientation.EAST, Orientation.WEST,
			Orientation.NORTH, Orientation.SOUTH };

	/**
	 * Construction of a wall-entity which belongs to <code>parent</code>.
	 * 
	 * @param parent
	 *            stage of which the <code>model</code> is a part of
	 * @param model
	 *            data model of the field
	 * @param gameHandler
	 *            {@link IGameHandler} which owns this entity
	 */
	public Wall(final IStage parentStage, final IField model,
			final RobotGameHandler gameHandler) {
		super(model, gameHandler);

		this.parent = parentStage;

		this.texWall = new TransformedRenderUnit[4];
		this.fieldWidth = prefs.getFloat(PrefKey.FIELD_WIDTH_KEY);
		this.fieldHeight = prefs.getFloat(PrefKey.FIELD_HEIGHT_KEY);

		this.renderX = model.getX() * fieldWidth;
		this.renderY = model.getY() * fieldHeight;

		this.determineOneway();
		this.createRenderUnits();
	}

	/**
	 * Creation of all transformed render units.
	 */
	private void createRenderUnits() {
		final RenderUnit wallTexture = resHandler
				.createRenderUnit(ResConst.DEFAULT_WALL);
		final RenderUnit onewayTexture = resHandler
				.createRenderUnit(ResConst.DEFAULT_ONEWAY);

		texWall[0] = new TransformedRenderUnit(isOneway[0] ? onewayTexture
				: wallTexture, renderX + fieldWidth / 2f, renderY - fieldHeight
				/ 2f, fieldWidth, fieldHeight * 2f, fieldWidth / 2f,
				fieldHeight, 180f);

		texWall[1] = new TransformedRenderUnit(isOneway[1] ? onewayTexture
				: wallTexture, renderX - fieldWidth / 2f, renderY - fieldHeight
				/ 2f, fieldWidth, fieldHeight * 2f, fieldWidth / 2f,
				fieldHeight, 0f);

		texWall[2] = new TransformedRenderUnit(isOneway[2] ? onewayTexture
				: wallTexture, renderX, renderY - fieldHeight, fieldWidth,
				fieldHeight * 2, fieldWidth / 2f, fieldHeight, 90f);

		texWall[3] = new TransformedRenderUnit(isOneway[3] ? onewayTexture
				: wallTexture, renderX, renderY, fieldWidth, fieldHeight * 2,
				fieldWidth / 2f, fieldHeight, -90f);
	}

	/**
	 * Checks all walls of the field if they're one-way roads.
	 */
	private void determineOneway() {
		for (int i = 0; i < orientations.length; ++i) {
			isOneway[i] = StageUtil.checkDriveDirectionAndNotNeighbours(model,
					parent, orientations[i]);
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
		if (event.getType() == UpdateType.STAGE_WALL) {
			determineOneway();
			createRenderUnits();
		}
	}

	@Override
	public void onUpdatePreferences(PrefKey updatedKey, Object value) {
		switch (updatedKey) {

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
