package de.unihannover.swp2015.robots2.visual.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.visual.core.IGameHandler;
import de.unihannover.swp2015.robots2.visual.core.PrefConst;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.resource.ResConst;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesKey;
import de.unihannover.swp2015.robots2.visual.util.pref.observer.PreferencesObservable;

/**
 * An entity used for the visualization of resources
 * 
 * @version 0.1
 * @author Daphne Schössow
 */
public class Resource extends Entity {

	private IField model;
	private final TextureRegion[] tex;

	public Resource(final IField model, SpriteBatch batch, IGameHandler gameHandler, IPreferences prefs, IResourceHandler resHandler) {
		super(batch, gameHandler, prefs, resHandler);
		
		this.model = model;
		this.model.observe(this);
		
		this.tex = resHandler.getRegion(ResConst.DEFAULT_RES_1,
				ResConst.DEFAULT_RES_2, ResConst.DEFAULT_RES_3, ResConst.DEFAULT_RES_4,
				ResConst.DEFAULT_RES_5, ResConst.DEFAULT_RES_6, ResConst.DEFAULT_RES_7,
				ResConst.DEFAULT_RES_8, ResConst.DEFAULT_RES_9, ResConst.DEFAULT_RES_10);

		final float fieldWidth = prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 50);
		final float fieldHeight = prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 50);

		this.renderX = model.getX() * fieldWidth;
		this.renderY = model.getY() * fieldHeight;
	}

	@Override
	public void render() {

		if (model.getFood() == 0)
			return;
		
		final float fieldWidth = prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 50);
		final float fieldHeight = prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 50);
		
		if (model.getFood() > 0)
			batch.draw(tex[model.getFood()-1], renderX, renderY, fieldWidth, fieldHeight);
	}

	@Override
	public void onModelUpdate(IEvent event) {
		if (event.getType() == IEvent.UpdateType.FIELD_FOOD) {
			// some animation stuff
		}
	}

	@Override
	public void onUpdatePreferences(PreferencesObservable o, IPreferencesKey updatedKey) {
		// TODO Auto-generated method stub

	}
}
