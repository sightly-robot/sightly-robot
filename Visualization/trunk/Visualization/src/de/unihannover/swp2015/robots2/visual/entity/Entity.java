package de.unihannover.swp2015.robots2.visual.entity;

/**
 * The base class of all objects displayed at the (beamer) visualization
 *
 * Every object has placement coordinates, an image and a value to determine wheather it's shown.
 * 
 * @version 0.1
 * @author Daphne Schössow
 */
public abstract class Entity implements IEntity {

	private Texture texture;
    private float renderX, renderY; 
    private IGameHandler gameHandler;
    private boolean isVisible; //Optional
    
    public abstract void setPosition(int x, int y);
    
    public abstract void hide(); //Optional

}
