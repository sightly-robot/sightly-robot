package de.unihannover.swp2015.robots2.visual.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.unihannover.swp2015.robots2.visual.core.IGameHandler;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;

/**
 * The base class of all objects displayed at the (beamer) visualization
 *
 * Every object has placement coordinates, an image and a value to determine wheather it's shown.
 * 
 * @version 0.1
 * @author Daphne Schössow
 */
public abstract class Entity implements IEntity {

    protected float renderX; 
    protected float renderY; 
    protected int zIndex = 0;
    
    protected final IResourceHandler resHandler;
    protected final IPreferences prefs;
    protected final IGameHandler gameHandler;
    protected final SpriteBatch batch;
    
    public Entity(final SpriteBatch batch, final IGameHandler gameHandler, final IPreferences prefs, final IResourceHandler resHandler) {
    	this.batch = batch;
    	this.gameHandler = gameHandler;
    	this.resHandler = resHandler;

    	this.prefs = prefs;
    	this.prefs.addObserver(this);
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
    
}
