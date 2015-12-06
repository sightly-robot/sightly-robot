package de.unihannover.swp2015.robots2.visual.entity;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.unihannover.swp2015.robots2.model.interfaces.IAbstractModel;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;
import de.unihannover.swp2015.robots2.visual.core.IGameHandler;
import de.unihannover.swp2015.robots2.visual.core.PrefConst;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesKey;
import de.unihannover.swp2015.robots2.visual.util.pref.observer.PreferencesObservable;

/**
 * An entity used for the visualization of 
 * 
 * @version 0.0
 * @author Daphne Schössow
 */
public class Map extends Entity {

	/**
	 * List of field-entities, which will be rendered by this entity.
	 */
	private final List<Field> fieldList;
	
	/**
	 * Constructs a map.
	 * 
	 * @param model data model of the map
	 * @param batch batch, which should be used to render the map
	 * @param gameHandler parent
	 */
	public Map(IStage model, IGameHandler gameHandler) {
		super(model, gameHandler);
	
		this.fieldList = new ArrayList<>(model.getWidth() * model.getHeight());
		
		this.init(model);
	}
	
	/**
	 * Creates all field-entities.
	 * 
	 * @param model data model of the map
	 */
	private void init(final IStage model) {
		for (int x = 0; x < model.getWidth(); ++x) {
			for (int y = 0; y < model.getHeight(); ++y) {
				final IField field = model.getField(x, y);
				this.fieldList.add(new Field(model, field, gameHandler));
			}
		}
	}
	
	@Override
	public void draw(final Batch batch) {
		for (int i = 0 ; i < fieldList.size() ; ++i) {
			fieldList.get(i).draw(batch);
		}
	}
	
	@Override
	public void onManagedModelUpdate(IEvent event) {
		final IStage model = (IStage) this.model;
		
		switch(event.getType()) {
		
		case STAGE_SIZE:
			//TODO dynamic world-size
			this.fieldList.clear();
			this.prefs.putInt(PrefConst.MAP_ROWS_KEY, model.getWidth());
			this.prefs.putInt(PrefConst.MAP_COLS_KEY, model.getHeight());
			this.prefs.putFloat(PrefConst.FIELD_WIDTH_KEY, prefs.getFloat(PrefConst.VIEW_WIDTH, 1) / model.getWidth());
			this.prefs.putFloat(PrefConst.FIELD_HEIGHT_KEY, prefs.getFloat(PrefConst.VIEW_HEIGHT, 1) / model.getHeight());
			this.init(model);
			break;
			
		case STAGE_WALL:
			for (final Field f : this.fieldList)
				f.onModelUpdate(event);
			break;
			
		default:
			break;
		
		}		
	}

	@Override
	public void onUpdatePreferences(PreferencesObservable o, IPreferencesKey updatedKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IAbstractModel getModel() {
		return model;
	}

}
