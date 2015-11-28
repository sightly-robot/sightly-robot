package de.unihannover.swp2015.robots2.visual.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.visual.core.IGameHandler;
import de.unihannover.swp2015.robots2.visual.core.PreferencesConstants;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.resource.ResourceConstants;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;
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
		
		this.tex = resHandler.getRegion(ResourceConstants.DEFAULT_RES_0, ResourceConstants.DEFAULT_RES_1,
				ResourceConstants.DEFAULT_RES_2, ResourceConstants.DEFAULT_RES_3, ResourceConstants.DEFAULT_RES_4,
				ResourceConstants.DEFAULT_RES_5, ResourceConstants.DEFAULT_RES_6, ResourceConstants.DEFAULT_RES_7,
				ResourceConstants.DEFAULT_RES_8, ResourceConstants.DEFAULT_RES_9, ResourceConstants.DEFAULT_RES_10);

		final float fieldWidth = prefs.getFloat(PreferencesConstants.FIELD_WIDTH_KEY, 10);
		final float fieldHeight = prefs.getFloat(PreferencesConstants.FIELD_HEIGHT_KEY, 10);

		this.renderX = model.getX() * fieldWidth;
		this.renderY = model.getY() * fieldHeight;
	}

	@Override
	public void render() {

		if (model.getFood() == 0)
			return;
		
		final float fieldWidth = prefs.getFloat(PreferencesConstants.FIELD_WIDTH_KEY, 10);
		final float fieldHeight = prefs.getFloat(PreferencesConstants.FIELD_HEIGHT_KEY, 10);
		
		batch.begin();
		batch.draw(tex[model.getFood()], renderX, renderY, fieldWidth/2, fieldHeight/2);
		batch.end();
	}

	@Override
	public void onModelUpdate(IEvent event) {
		if (event.getType() == IEvent.UpdateType.FIELD_FOOD) {
			// some animation stuff
		}
	}

	@Override
	public void onUpdatePreferences(PreferencesObservable o, String updatedKey) {
		// TODO Auto-generated method stub

	}
}
