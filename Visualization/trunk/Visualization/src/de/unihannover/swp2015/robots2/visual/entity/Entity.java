package de.unihannover.swp2015.robots2.visual.entity;

import com.badlogic.gdx.graphics.Texture;

import de.unihannover.swp2015.robots2.visual.core.IGameHandler;

/**
 * The base class of all objects displayed at the (beamer) visualization
 *
 * Every object has placement coordinates, an image and a value to determine wheather it's shown.
 * 
 * @version 0.1
 * @author Daphne Schössow
 */
public abstract class Entity implements IEntity {

	protected Texture texture;
    protected float renderX, renderY; 
    protected IGameHandler gameHandler;
    protected boolean isVisible; //Optional
    
    public abstract void setPosition(int x, int y);
    
    public abstract void hide(); //Optional

}
