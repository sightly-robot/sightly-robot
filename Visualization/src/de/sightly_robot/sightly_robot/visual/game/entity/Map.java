package de.sightly_robot.sightly_robot.visual.game.entity;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.sightly_robot.sightly_robot.model.interfaces.IEvent;
import de.sightly_robot.sightly_robot.model.interfaces.IField;
import de.sightly_robot.sightly_robot.model.interfaces.IStage;
import de.sightly_robot.sightly_robot.visual.core.PrefKey;
import de.sightly_robot.sightly_robot.visual.core.entity.Entity;
import de.sightly_robot.sightly_robot.visual.game.GameConst;
import de.sightly_robot.sightly_robot.visual.game.RobotGameHandler;

/**
 * An entity which is used for the construction of the map.
 * 
 * This creates all field entities.
 * 
 * @author Daphne Schössow
 */
public class Map extends Entity<RobotGameHandler, IStage> {

	/** true if walls should get rendered */
	protected boolean renderWalls;
	/** true if resources should get rendered */
	protected boolean renderResources;

	/** list of field entities which will be rendered by this entity */
	private final List<Field> fieldList;

	/** list of resource entities which will be rendered by this entity */
	private final List<Resource> foodList;

	/** list of wall entities which will be rendered by this entity */
	private final List<Wall> wallList;

	/**
	 * Construction of the map.
	 * 
	 * @param model
	 *            data model of the map
	 * @param gameHandler
	 *            parent
	 */
	public Map(IStage model, RobotGameHandler gameHandler) {
		super(model, gameHandler);

		this.fieldList = new ArrayList<>(model.getWidth() * model.getHeight());
		this.wallList = new ArrayList<>(model.getWidth() * model.getHeight());
		this.foodList = new ArrayList<>(model.getWidth() * model.getHeight());

		this.renderWalls = prefs.getBoolean(PrefKey.RENDER_WALL);
		this.renderResources = prefs.getBoolean(PrefKey.RENDER_RESOURCES);

		if (model.getHeight() != 0 && model.getWidth() != 0)
			this.resize();
	}

	/**
	 * Creation of all field-entities.
	 */
	private void init() {
		for (int x = 0; x < model.getWidth(); ++x) {
			for (int y = 0; y < model.getHeight(); ++y) {
				final IField field = model.getField(x, y);
				fieldList.add(new Field(model, field,
						(RobotGameHandler) gameHandler));
				wallList.add(new Wall(model, field,
						(RobotGameHandler) gameHandler));
				foodList.add(new Resource(field, (RobotGameHandler) gameHandler));
			}
		}
	}

	/**
	 * If the stage gets resized (because a different map has been loaded), this
	 * manages the redrawing of the stage and all components.
	 */
	private void resize() {
		for (int i = 0; i < fieldList.size(); ++i) {
			fieldList.get(i).clearReferences();
			wallList.get(i).clearReferences();
			foodList.get(i).clearReferences();
		}

		fieldList.clear();
		wallList.clear();
		foodList.clear();

		final float fieldSize = prefs.getFloat(PrefKey.DEVICE_HEIGHT)
				* GameConst.HEIGHT_SCALE / model.getHeight();
		final float viewWidth = fieldSize * model.getWidth();
		final float viewHeight = fieldSize * model.getHeight();

		prefs.putFloat(PrefKey.FIELD_WIDTH_KEY, fieldSize);
		prefs.putFloat(PrefKey.FIELD_HEIGHT_KEY, fieldSize);
		prefs.putFloat(PrefKey.VIEW_WIDTH, viewWidth);
		prefs.putFloat(PrefKey.VIEW_HEIGHT, viewHeight);

		init();
	}

	@Override
	public void draw(final Batch batch) {
		super.draw(batch);

		for (int i = 0; i < fieldList.size(); ++i) {
			fieldList.get(i).draw(batch);
		}

		if (renderWalls) {
			for (int i = 0; i < wallList.size(); ++i) {
				wallList.get(i).draw(batch);
			}
		}

		if (renderResources) {
			for (int i = 0; i < wallList.size(); ++i) {
				foodList.get(i).draw(batch);
			}
		}

	}

	@Override
	public void onManagedModelUpdate(IEvent event) {
		switch (event.getType()) {

		case STAGE_SIZE:
			resize();
			break;

		case STAGE_WALL:
			for (final Field f : fieldList) {
				f.onModelUpdate(event);
			}
			for (final Wall w : wallList) {
				w.onModelUpdate(event);
			}
			break;

		default:
			break;

		}
	}

	@Override
	public void onUpdatePreferences(PrefKey updatedKey, Object value) {
		switch (updatedKey) {

		case RENDER_WALL:
			renderWalls = (boolean) value;
			break;

		case RENDER_RESOURCES:
			renderResources = (boolean) value;
			break;

		default:
			break;
		}
	}

	@Override
	public IStage getModel() {
		return model;
	}

}
