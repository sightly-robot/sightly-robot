package de.unihannover.swp2015.robots2.visual.core.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.unihannover.swp2015.robots2.model.interfaces.IAbstractModel;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.visual.core.handler.IGameHandler;
import de.unihannover.swp2015.robots2.visual.game.RobotGameHandler;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.util.SortUtil;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;

/**
 * The base class of all objects displayed at the (beamer) visualization
 *
 * Every object has placement coordinates (3-dimensional, so they get rendered in the right order),
 * a rotation and a model that contains all information about the object.
 * 
 * @version 1.0
 * @author Daphne Schössow
 */
public abstract class Entity implements IEntity {

	/**
	 * List of all modifiers created for the entity.
	 */
	protected final List<IEntityModifier> modList;
	
	/**
	 * List of all components registered by the entity.
	 */
	protected final List<IComponent> componentList;
	
	/**
	 * Data model of the entity.
	 */
	protected final IAbstractModel model;

	/**
	 * X-coordinate of the entity on the (virtual) screen.
	 */
	protected float renderX; 

	/**
	 * Y-coordinate of the entity on the (virtual) screen.
	 */
	protected float renderY; 
	
	/**
	 * X-coordinate of the entity on the (virtual) screen.
	 */
	protected float width; 

	/**
	 * Y-coordinate of the entity on the (virtual) screen.
	 */
	protected float height; 

	/**
	 * Z-coordinate of the entity on the (virtual) screen.
	 */
	protected int zIndex = 0;
	
	/**
	 * Rotation of the entity in degrees.
	 */
    protected float rotation = 0;
    
    /**
     * ResourceHandler, which should be used to obtain resources.
     */
    protected final IResourceHandler resHandler;
    
    /**
     * Preference object, which contains all necessary preferences.
     */
    protected final IPreferences prefs;
    
    /**
     * GameHandler, which owns the entity.
     */
    protected final IGameHandler gameHandler;
        
    /**
     * Constructs an entity. The constructor will make the entity observe the <code>model</code> and the preference-object of the <code>gameHandler</code>
     * 
     * @param model data model of the entity
     * @param batch batch for drawing
     * @param gameHandler parent
     */
    public Entity(final IAbstractModel model, final IGameHandler gameHandler) {
    	this.modList = new ArrayList<>();
    	this.componentList = new ArrayList<>();
    	
    	this.gameHandler = gameHandler;
    	this.resHandler = gameHandler.getResourceHandler();

    	this.prefs = gameHandler.getPreferences();
    	this.prefs.addObserver(this);
    	
    	this.model = model;
    	this.model.observe(this);
    }
    
    @Override
    public void update() {
	    for (int i = modList.size()-1; i >= 0 ; --i) {
	    	final IEntityModifier mod = modList.get(i);
	    	if (mod.hasFinished()) {
	    		mod.onFinish();
	    		modList.remove(i);
	    		break;
	    	}
	    	mod.update();
	    }
    	for (int i = 0 ; i < componentList.size(); ++i) {
    		final IComponent comp = componentList.get(i);
    		comp.update();
    	}
    }
    
    @Override
    public void draw(final Batch batch) {
    	for (int i = 0 ; i < componentList.size(); ++i) {
    		final IComponent comp = componentList.get(i);
    		comp.draw(batch);
    	}
    }
    
    @Override
    public void registerModifier(final IEntityModifier mod) {
    	this.modList.add(mod);
    	mod.onInit();
    }
    
    @Override
    public void clearModifier() {
    	this.modList.clear();
    }
    
    @Override
    public void registerComponent(final IComponent component) {
    	this.componentList.add(component);
    	component.onRegister(this);
    }

    @Override
    public void unregisterComponent(final IComponent component) {
    	this.componentList.remove(component);
    }
    
    @Override 
    public void onModelUpdate(final IEvent event) {
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				Entity.this.onManagedModelUpdate(event);
				
				for (int i = 0; i < componentList.size(); ++i) {
					componentList.get(i).onEvent(event);
				}
			}
		});
    }
        
    @Override
    public void clearReferences() {
    	this.prefs.removeObserver(this);
    	this.model.unobserve(this);
    }
    
    @Override
    public int getZIndex() {
    	return zIndex;
    }
    
    @Override
    public void setZIndex(final int zIndex) {
    	this.zIndex = zIndex;
    }
    
	@Override
	public int compareTo(IEntity o) {
		return this.zIndex - o.getZIndex();
	}

	@Override
	public void setPosition(float x, float y) {
		this.renderX = x;
		this.renderY = y;
	}

	@Override
	public float getPositionX() {
		return renderX;
	}

	@Override
	public float getPositionY() {
		return renderY;
	}
	
	@Override
	public float getWidth() {
		return width;
	}

	@Override
	public float getHeight() {
		return height;
	}
	
	@Override
	public float getRotation() {
		return rotation;
	}
	
	@Override
	public void setRotation(final float rot) {
		this.rotation = rot;
	}
	
	@Override
	public IAbstractModel getModel() {
		return model;
	}
	
	/**
	 * Should be called, after you have added an entity to the {@link RobotGameHandler}. As alternative you can call {@link EntityUtil#addEntitySorted(IEntity, List)d} 
	 * to add a new entity. 
	 */
	public static void sortEntities(final List<IEntity> entityList) {
		Collections.sort(entityList);
	}
	
	/**
	 * Adds a new entity to <code>renderUnits</code>. The method uses insertion sort, so you don't have to call {@link SortUtil#sortEntities(List)}
	 * 
	 * @param entity new entity
	 * @param entityList target list
	 */
	public static void addEntitySorted(final IEntity entity, final List<IEntity> entityList) {
		for (int i = 0 ; i < entityList.size() + 1; ++i) {
			if (i == entityList.size()) {
				entityList.add(entity);
				break;
			}
			
			if (entity.getZIndex() < entityList.get(i).getZIndex()) {
				entityList.add(i, entity);
				break;
			}
		}
	}
    
}
