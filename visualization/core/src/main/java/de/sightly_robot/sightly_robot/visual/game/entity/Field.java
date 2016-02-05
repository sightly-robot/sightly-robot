package de.sightly_robot.sightly_robot.visual.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.sightly_robot.sightly_robot.model.interfaces.IEvent;
import de.sightly_robot.sightly_robot.model.interfaces.IField;
import de.sightly_robot.sightly_robot.model.interfaces.IStage;
import de.sightly_robot.sightly_robot.model.interfaces.IField.State;
import de.sightly_robot.sightly_robot.model.interfaces.IPosition.Orientation;
import de.sightly_robot.sightly_robot.visual.core.PrefKey;
import de.sightly_robot.sightly_robot.visual.core.entity.Entity;
import de.sightly_robot.sightly_robot.visual.game.RobotGameHandler;
import de.sightly_robot.sightly_robot.visual.resource.ResConst;
import de.sightly_robot.sightly_robot.visual.resource.texture.RenderUnit;
import de.sightly_robot.sightly_robot.visual.util.ColorUtil;
import de.sightly_robot.sightly_robot.visual.util.StageUtil;

/**
 * an entity used for the visualization of the underground
 * 
 * The texture depends on the wall placement.
 * 
 * @author Daphne Schössow
 */
public class Field extends Entity<RobotGameHandler, IField> {

	/**
	 * lookup table for the ground texture, which depends on the placement of
	 * walls
	 */
	private static final ResConst[] FIELD_TEXTURE_LOOKUP = {
			ResConst.DEFAULT_FIELD_4, ResConst.DEFAULT_FIELD_3,
			ResConst.DEFAULT_FIELD_3, ResConst.DEFAULT_FIELD_2,
			ResConst.DEFAULT_FIELD_3, ResConst.DEFAULT_FIELD_2_CURVE,
			ResConst.DEFAULT_FIELD_2_CURVE, ResConst.DEFAULT_FIELD_1,
			ResConst.DEFAULT_FIELD_3, ResConst.DEFAULT_FIELD_2_CURVE,
			ResConst.DEFAULT_FIELD_2_CURVE, ResConst.DEFAULT_FIELD_1,
			ResConst.DEFAULT_FIELD_2, ResConst.DEFAULT_FIELD_1,
			ResConst.DEFAULT_FIELD_1, ResConst.DEFAULT_FIELD };

	/** lookup table for the rotation */
	private static final int[] FIELD_ROTATION_LOOKUP = { 0, -90, 90, 90, 180,
			-90, 180, 180, 0, 0, 90, 0, 0, -90, 90, 0 };

	/** the stage the field belongs to */
	private final IStage parent;

	/** visual representation of the ground */
	private RenderUnit fieldUnit;

	/** width of the field */
	private float fieldWidth;

	/** height of the field */
	private float fieldHeight;

	/** true if the lock state of the fields should be rendered */
	private boolean renderLock;

	/** color of the robot which has locked this field */
	private Color robotColor;

	/**
	 * Contains all orientations in a specific order.
	 * 
	 * (I do not use Enum.values() here to be sure that the order is like I'm
	 * expecting it.)
	 */
	private Orientation[] orientations = { Orientation.EAST, Orientation.WEST,
			Orientation.NORTH, Orientation.SOUTH };

	/**
	 * Constructs a field-entity, which belongs to <code>parent</code>.
	 * 
	 * @param parent
	 *            stage of which the <code>model</code> is part of
	 * @param model
	 *            data model of the field
	 * @param renderer
	 *            batch which should be used to render the entity
	 * @param gameHandler
	 *            {@link IGameHandler} which should own this entity
	 */
	public Field(final IStage parentStage, final IField model,
			final RobotGameHandler gameHandler) {
		super(model, gameHandler);

		this.parent = parentStage;
		this.fieldUnit = resHandler.createRenderUnit(ResConst.DEFAULT_FIELD);
		this.fieldWidth = prefs.getFloat(PrefKey.FIELD_WIDTH_KEY);
		this.fieldHeight = prefs.getFloat(PrefKey.FIELD_HEIGHT_KEY);
		this.renderLock = prefs.getBoolean(PrefKey.RENDER_LOCK);

		this.renderX = model.getX() * fieldWidth;
		this.renderY = model.getY() * fieldHeight;

		this.determineFieldTexture();
	}

	/**
	 * Updates {@link #fieldUnit} and {@link #rotation}.
	 * 
	 * By checking if there's a solid wall in all directions it chooses a
	 * fitting field texture with the right rotation.
	 */
	private void determineFieldTexture() {

		// 3:SOUTH 2:NORTH 1:WEST 0:EAST
		final boolean[] dir = new boolean[4];

		// checks for every direction if there is a double wall.
		for (int i = 0; i < orientations.length; ++i) {
			dir[i] = StageUtil.checkDriveDirectionAndNeighbours(model, parent,
					orientations[i]);
		}

		// selects the correct texture + rotation depending on the booleans
		final int result = StageUtil.convertToInt(dir, 2);
		fieldUnit = resHandler.createRenderUnit(FIELD_TEXTURE_LOOKUP[result]);
		rotation = FIELD_ROTATION_LOOKUP[result];
	}

	/**
	 * Updates the visualization of the field locking.
	 */
	private void updateLockState() {
		if (model.getState() == State.LOCKED
				|| model.getState() == State.OCCUPIED) {
			robotColor = ColorUtil.fromAwtColor(gameHandler.getRobot(
					model.getLockedBy()).getColor());
			robotColor.r = Math.min(robotColor.r + 0.3f, 1);
			robotColor.g = Math.min(robotColor.g + 0.3f, 1);
			robotColor.b = Math.min(robotColor.b + 0.3f, 1);
		} else if (model.getState() == State.FREE) {
			robotColor = null;
		}
	}

	@Override
	public void draw(final Batch batch) {
		super.draw(batch);

		if (renderLock && robotColor != null) {
			batch.setColor(robotColor);
		}

		// the following switch corrects the drawn bounds of the field when
		// rotation is 90° or -90°
		// it's just relevant if fieldWidth != fieldHeight
		switch ((int) rotation) {

		case -90:
			fieldUnit.draw(batch, renderX - (fieldWidth - fieldHeight) / 2,
					renderY - (fieldWidth - fieldHeight) / 2, fieldHeight,
					fieldWidth, fieldWidth / 2f, fieldHeight / 2f, 1f, 1f,
					rotation);
			break;

		case 90:
			fieldUnit.draw(batch, renderX + (fieldWidth - fieldHeight) / 2,
					renderY + (fieldWidth - fieldHeight) / 2, fieldHeight,
					fieldWidth, fieldWidth / 2f, fieldHeight / 2f, 1f, 1f,
					rotation);
			break;

		default:
			fieldUnit.draw(batch, renderX, renderY, fieldWidth, fieldHeight,
					fieldWidth / 2f, fieldHeight / 2f, 1f, 1f, rotation);
			break;
		}

		if (renderLock) {
			batch.setColor(Color.WHITE);
		}
	}

	@Override
	public void onManagedModelUpdate(IEvent event) {

		switch (event.getType()) {

		case STAGE_WALL:
			determineFieldTexture();
			break;

		case FIELD_STATE:
			updateLockState();
			break;

		default:
			break;
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

		case RENDER_LOCK:
			renderLock = (boolean) value;
			break;

		default:
			break;
		}
	}

}
