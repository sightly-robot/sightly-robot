package de.sightly_robot.sightly_robot.visual.core;

/**
 * All classes that implement this interface are marked as
 * <code>renderable</code>.
 * 
 * This means you can call {@link IRenderable#render()} to visualize the
 * internal state of these objects. <br>
 * The main difference to {@link IDrawable} is that renderable classes manage
 * their batches on their own. <br>
 * Note: {@link IRenderable#render()} has to be called after
 * {@link IUpdateable#update()}, otherwise the resulting behavior may be
 * undefined. You have to call methods like
 * {@link com.badlogic.gdx.graphics.g2d.Batch#begin()} yourself.
 * 
 * @author Rico Schrage
 */
public interface IRenderable {

	/**
	 * Renders the current visualization of the object.
	 */
	void render();

}
