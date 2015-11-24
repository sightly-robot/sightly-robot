package de.unihannover.swp2015.robots2.visual.entity;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;
import de.unihannover.swp2015.robots2.visual.core.IGameHandler;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;
import de.unihannover.swp2015.robots2.visual.util.pref.observer.PreferencesObservable;

/**
 * An entity used for the visualization of 
 * 
 * @version 0.0
 * @author Daphne Schössow
 */
public class Map extends Entity {

	private final IStage model;
	private final List<Resource> resourceList;
	private final List<Field> fieldList;
	
	public Map(IStage model, SpriteBatch batch, IGameHandler gameHandler, IPreferences prefs, IResourceHandler resHandler) {
		super(batch, gameHandler, prefs, resHandler);
	
		this.resourceList = new ArrayList<>(model.getWidth() * model.getHeight());
		this.fieldList = new ArrayList<>(model.getWidth() * model.getHeight());
		
		this.model = model;
		this.init();
	}
	
	private void init() {
		for (int x = 0; x < model.getWidth(); ++x) {
			for (int y = 0; y < model.getHeight(); ++y) {
				final IField field = model.getField(x, y);
				this.resourceList.add(new Resource(field, batch, gameHandler, prefs, resHandler));
				this.fieldList.add(new Field(field, batch, gameHandler, prefs, resHandler));
			}
		}
	}
	
	@Override
	public void render() {
		for (int i = 0 ; i < fieldList.size() ; ++i) {
			fieldList.get(i).render();
			resourceList.get(i).render();
		}
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
