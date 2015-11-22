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
 * An entity used for the visualization of 
 * 
 * @version 0.1
 * @author Daphne Schössow
 */
public class Field extends Entity {
	
	private final IField model;
	private final TextureRegion texH;
	private final TextureRegion texV;
	
	public Field(final IField model, final SpriteBatch renderer, final IGameHandler gameHandler, final IPreferences prefs, final IResourceHandler resHandler){
		super(renderer, gameHandler, prefs, resHandler);
		
		this.model = model;
		this.model.observe(this);
		
		this.texH = resHandler.getRegion(ResourceConstants.DEFAULT_WALL_H); 
		this.texV = resHandler.getRegion(ResourceConstants.DEFAULT_WALL_V); 
		this.renderX = model.getX() * prefs.getInt(PreferencesConstants.FIELD_WIDTH_KEY, 100);
		this.renderY = model.getY() * prefs.getInt(PreferencesConstants.FIELD_HEIGHT_KEY, 100);
	}

	@Override
	public void render() {
		batch.begin();
		batch.end();
	}

	@Override
	public void onModelUpdate(IEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUpdatePreferences(PreferencesObservable o, String updatedKey) {
		// TODO Auto-generated method stub
		
	}

}
