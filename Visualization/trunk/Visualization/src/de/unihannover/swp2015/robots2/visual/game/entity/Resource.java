package de.unihannover.swp2015.robots2.visual.game.entity;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.visual.core.PrefKey;
import de.unihannover.swp2015.robots2.visual.core.entity.Entity;
import de.unihannover.swp2015.robots2.visual.game.RobotGameHandler;
import de.unihannover.swp2015.robots2.visual.resource.ResConst;
import de.unihannover.swp2015.robots2.visual.resource.texture.RenderUnit;

/**
 * An entity used for the visualization of resources/food
 * 
 * @version 1.0
 * @author Daphne Sch�ssow
 */
public class Resource extends Entity {

	/** Visual representation of the different stages of the food. */
	private final RenderUnit[] tex;

	/**
	 * Constructs a resource entity.
	 * 
	 * @param model
	 *            data model of the resource.
	 * @param batch
	 *            batch, which will be used to draw the entity.
	 * @param gameHandler
	 *            parent
	 */
	public Resource(final IField model, RobotGameHandler gameHandler) {
		super(model, gameHandler);

		this.tex = resHandler.createRenderUnit(ResConst.DEFAULT_RES_1, ResConst.DEFAULT_RES_2, ResConst.DEFAULT_RES_3,
				ResConst.DEFAULT_RES_4, ResConst.DEFAULT_RES_5, ResConst.DEFAULT_RES_6, ResConst.DEFAULT_RES_7,
				ResConst.DEFAULT_RES_8, ResConst.DEFAULT_RES_9, ResConst.DEFAULT_RES_10);

		final float fieldWidth = prefs.getFloat(PrefKey.FIELD_WIDTH_KEY, 50);
		final float fieldHeight = prefs.getFloat(PrefKey.FIELD_HEIGHT_KEY, 50);

		this.renderX = model.getX() * fieldWidth;
		this.renderY = model.getY() * fieldHeight;
	}

	@Override
	public void draw(final Batch batch) {
		super.draw(batch);

		final IField field = (IField) model;

		if (field.getFood() == 0)
			return;

		final float fieldWidth = prefs.getFloat(PrefKey.FIELD_WIDTH_KEY, 50);
		final float fieldHeight = prefs.getFloat(PrefKey.FIELD_HEIGHT_KEY, 50);

		tex[field.getFood() - 1].draw(batch, renderX + (fieldWidth * 0.15f) / 2, renderY + (fieldHeight * 0.15f) / 2,
				fieldWidth * 0.85f, fieldHeight * 0.85f);
	}

	@Override
	public void onManagedModelUpdate(IEvent event) {
		// trivial entity
	}

	@Override
	public void onUpdatePreferences(PrefKey updatedKey, Object value) {
		// trivial entity
	}

}
