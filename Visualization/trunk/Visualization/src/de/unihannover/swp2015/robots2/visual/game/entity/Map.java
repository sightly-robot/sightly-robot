package de.unihannover.swp2015.robots2.visual.game.entity;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.unihannover.swp2015.robots2.model.interfaces.IAbstractModel;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;
import de.unihannover.swp2015.robots2.visual.core.PrefKey;
import de.unihannover.swp2015.robots2.visual.core.entity.Entity;
import de.unihannover.swp2015.robots2.visual.game.GameConst;
import de.unihannover.swp2015.robots2.visual.game.RobotGameHandler;

/**
 * An entity, which is used for the construction of the map and that creates all
 * field entities
 * 
 * @version 1.0
 * @author Daphne Sch�ssow
 */
public class Map extends Entity {

	/** True if walls should get rendered */
	protected boolean renderWalls;
	/** True if resources should get rendered */
	protected boolean renderResources;

	/** List of field-entities, which will be rendered by this entity. */
	private final List<Field> fieldList;

	/** List of wall-entities, which will be rendered by this entity. */
	private final List<Resource> foodList;

	/** List of wall-entities, which will be rendered by this entity. */
	private final List<Wall> wallList;

	/**
	 * Constructs a map.
	 * 
	 * @param model
	 *            data model of the map
	 * @param batch
	 *            batch, which should be used to render the map
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
	 * Creates all field-entities.
	 * 
	 * @param model
	 *            data model of the map
	 */
	private void init(final IStage model) {
		for (int x = 0; x < model.getWidth(); ++x) {
			for (int y = 0; y < model.getHeight(); ++y) {
				final IField field = model.getField(x, y);
				fieldList.add(new Field(model, field, (RobotGameHandler) gameHandler));
			}
		}
		for (int x = 0; x < model.getWidth(); ++x) {
			for (int y = 0; y < model.getHeight(); ++y) {
				final IField field = model.getField(x, y);
				wallList.add(new Wall(model, field, (RobotGameHandler) gameHandler));
			}
		}
		for (int x = 0; x < model.getWidth(); ++x) {
			for (int y = 0; y < model.getHeight(); ++y) {
				final IField field = model.getField(x, y);
				foodList.add(new Resource(field, (RobotGameHandler) gameHandler));
			}
		}
	}

	private void resize() {
		final IStage stageModel = (IStage) model;

		for (int i = 0; i < fieldList.size(); ++i) {
			fieldList.get(i).clearReferences();
			wallList.get(i).clearReferences();
			foodList.get(i).clearReferences();
		}

		fieldList.clear();
		wallList.clear();
		foodList.clear();

		final float fieldSize = prefs.getFloat(PrefKey.DEVICE_HEIGHT) * GameConst.HEIGHT_SCALE / stageModel.getHeight();
		final float viewWidth = fieldSize * stageModel.getWidth();
		final float viewHeight = fieldSize * stageModel.getHeight();

		prefs.putFloat(PrefKey.FIELD_WIDTH_KEY, fieldSize);
		prefs.putFloat(PrefKey.FIELD_HEIGHT_KEY, fieldSize);
		prefs.putFloat(PrefKey.VIEW_WIDTH, viewWidth);
		prefs.putFloat(PrefKey.VIEW_HEIGHT, viewHeight);

		init(stageModel);
	}

	@Override
	public void draw(final Batch batch) {
		super.draw(batch);

		for (int i = 0; i < fieldList.size(); ++i) {
			fieldList.get(i).draw(batch);
		}

		if (renderResources) {
			for (int i = 0; i < wallList.size(); ++i) {
				foodList.get(i).draw(batch);
			}
		}

		if (renderWalls) {
			for (int i = 0; i < wallList.size(); ++i) {
				wallList.get(i).draw(batch);
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
	public IAbstractModel getModel() {
		return model;
	}

}
