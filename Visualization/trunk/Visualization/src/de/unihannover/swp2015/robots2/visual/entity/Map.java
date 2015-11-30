package de.unihannover.swp2015.robots2.visual.entity;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;
import de.unihannover.swp2015.robots2.visual.core.IGameHandler;
import de.unihannover.swp2015.robots2.visual.core.PrefConst;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesKey;
import de.unihannover.swp2015.robots2.visual.util.pref.observer.PreferencesObservable;

/**
 * An entity used for the visualization of 
 * 
 * @version 0.0
 * @author Daphne Schössow
 */
public class Map extends Entity {

	private final IStage model;
	private final List<Field> fieldList;
	private final ShapeRenderer shapeRenderer;
	
	public Map(IStage model, SpriteBatch batch, IGameHandler gameHandler, IPreferences prefs, IResourceHandler resHandler) {
		super(batch, gameHandler, prefs, resHandler);
	
		this.fieldList = new ArrayList<>(model.getWidth() * model.getHeight());
		this.shapeRenderer = new ShapeRenderer();
		this.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		
		this.model = model;
		this.model.observe(this);
		
		this.init();
	}
	
	private void init() {
		for (int x = 0; x < model.getWidth(); ++x) {
			for (int y = 0; y < model.getHeight(); ++y) {
				final IField field = model.getField(x, y);
				this.fieldList.add(new Field(model, field, batch, gameHandler, prefs, resHandler));
			}
		}
	}
	
	@Override
	public void render() {
		
		final float fieldWidth = prefs.getFloat(PrefConst.FIELD_WIDTH_KEY, 50);
		final float fieldHeight = prefs.getFloat(PrefConst.FIELD_HEIGHT_KEY, 50);
		
		for (int i = 0 ; i < fieldList.size() ; ++i) {
			fieldList.get(i).render();
		}

		batch.end();
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(100f/255f, 100f/255f, 100f/255f, 100f/255f);
		for (int y = 1; y < model.getHeight(); ++y) {
			shapeRenderer.rect(0, y*fieldWidth, Gdx.graphics.getWidth(), 2);
		}
		for (int x = 1; x < model.getWidth(); ++x) {
			shapeRenderer.rect(x*fieldHeight, 0, 2, Gdx.graphics.getHeight());
		}
		shapeRenderer.end();
		
		batch.begin();
	}

	@Override
	public void onModelUpdate(IEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdatePreferences(PreferencesObservable o, IPreferencesKey updatedKey) {
		// TODO Auto-generated method stub
		
	}

}
