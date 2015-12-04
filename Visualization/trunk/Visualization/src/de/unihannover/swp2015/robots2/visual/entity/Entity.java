package de.unihannover.swp2015.robots2.visual.entity;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.unihannover.swp2015.robots2.model.interfaces.IAbstractModel;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.visual.core.IGameHandler;
import de.unihannover.swp2015.robots2.visual.entity.modifier.base.IEntityModifier;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;

/**
 * The base class of all objects displayed at the (beamer) visualization
 *
 * Every object has placement coordinates, an image and a value to determine whether it's shown.
 * 
 * @version 0.1
 * @author Daphne Schössow
 */
public abstract class Entity implements IEntity {

	/**
	 * List of all modifiers created for the entity.
	 */
	protected List<IEntityModifier> modList;
	
	/**
	 * Data model of the entity.
	 */
	protected IAbstractModel model;

	/**
	 * X-coordinate of the entity on the (virtual) screen.
	 */
	protected float renderX; 

	/**
	 * Y-coordinate of the entity on the (virtual) screen.
	 */
	protected float renderY; 

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
     * Batch, which should be used to draw the entity.
     */
    protected final SpriteBatch batch;
    
    /**
     * Constructs an entity. The constructor will make the entity observe the <code>model</code> and the preference-object of the <code>gameHandler</code>
     * 
     * @param model data model of the entity
     * @param batch batch for drawing
     * @param gameHandler parent
     */
    public Entity(final IAbstractModel model, final SpriteBatch batch, final IGameHandler gameHandler) {
    	this.batch = batch;
    	this.gameHandler = gameHandler;
    	this.resHandler = gameHandler.getResourceHandler();

    	this.prefs = gameHandler.getPreferences();
    	this.prefs.addObserver(this);
    	
    	this.model = model;
    	this.model.observe(this);
    }
    
    @Override
    public void update() {
    	if (modList == null)
    		return;
    	for (int i = modList.size()-1; i >= 0 ; --i) {
    		final IEntityModifier mod = modList.get(i);
    		if (mod.hasFinished()) {
    			mod.onFinish();
    			modList.remove(i);
    			break;
    		}
    		mod.update();
    	}
    }
    
    @Override
    public void registerModifier(final IEntityModifier mod) {
    	if (modList == null)
    		this.modList = new ArrayList<>();
    	this.modList.add(mod);
    	
    	mod.onInit();
    }
    
    @Override 
    public void onModelUpdate(final IEvent event) {
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				Entity.this.onManagedModelUpdate(event);
			}
		});
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
    
}
