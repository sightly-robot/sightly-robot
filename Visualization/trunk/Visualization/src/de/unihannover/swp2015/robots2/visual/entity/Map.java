package de.unihannover.swp2015.robots2.visual.entity;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.unihannover.swp2015.robots2.model.interfaces.IAbstractModel;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;
import de.unihannover.swp2015.robots2.visual.core.GameConst;
import de.unihannover.swp2015.robots2.visual.core.PrefConst;
import de.unihannover.swp2015.robots2.visual.core.RobotGameHandler;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesKey;
import de.unihannover.swp2015.robots2.visual.util.pref.observer.PreferencesObservable;

/**
 * An entity, which is used for the construction of the map and that creates all field entities
 * 
 * @version 1.0
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
	public Map(IStage model, RobotGameHandler gameHandler) {
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
				this.fieldList.add(new Field(model, field, (RobotGameHandler) gameHandler));
			}
		}
	}

	private void resize() {
		final IStage model = (IStage) this.model;

		for (int i = 0; i < fieldList.size(); ++i) {
			fieldList.get(i).clearReferences();
		}
		this.fieldList.clear();
		
		final float fieldSize = prefs.getFloat(PrefConst.DEVICE_HEIGHT) * GameConst.HEIGHT_SCALE / model.getHeight();
		final float viewWidth = fieldSize * model.getWidth();
		final float viewHeight = fieldSize * model.getHeight();
		
		this.prefs.putFloat(PrefConst.FIELD_WIDTH_KEY, fieldSize);
		this.prefs.putFloat(PrefConst.FIELD_HEIGHT_KEY, fieldSize);
		this.prefs.putFloat(PrefConst.VIEW_WIDTH, viewWidth);
		this.prefs.putFloat(PrefConst.VIEW_HEIGHT, viewHeight);
		this.init(model);
	}
	
	@Override
	public void draw(final Batch batch) {
		for (int i = 0 ; i < fieldList.size() ; ++i) {
			fieldList.get(i).draw(batch);
		}
	}
	
	@Override
	public void onManagedModelUpdate(IEvent event) {		
		switch(event.getType()) {
		
		case STAGE_SIZE:
			this.resize();
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
