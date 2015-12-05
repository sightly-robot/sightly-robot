package de.unihannover.swp2015.robots2.visual.core;

/**
 * All classes, which implement this interface are marked as <code>renderable</code>. This means you can call {@link IRenderable#render()} to visualize the internal state of these objects.
 * <br>
 * The key difference to {@link IDrawable} is, that renderable classes manage their batches theirself. 
 * <br>
 * Note: {@link IRenderable#render()} have to be called after {@link IUpdateable#update()}, otherwise the resulting behavior is probably undefined. 
 * Also You have to call methods like {@link com.badlogic.gdx.graphics.g2d.Batch#begin()} yourself.
 * 
 * @author Rico Schrage
 */
public interface IRenderable {

	/**
	 * Renders the current visualization of the object.
	 */
	void render();
	
}
