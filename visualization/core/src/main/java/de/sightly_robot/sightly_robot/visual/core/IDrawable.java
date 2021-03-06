package de.sightly_robot.sightly_robot.visual.core;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * All classes that implement this interface are marked as <code>drawable</code>
 * .
 * 
 * This means you can call {@link #draw(Batch)} to visualize the internal state
 * of these objects. <br>
 * The difference to {@link IRenderable} is that drawable classes don't have to
 * manage their batches on their own. <br>
 * Note: Normally, a drawable object will be child of a renderable object. Also
 * you don't have to call {@link Batch#begin()} and {@link Batch#end()}.
 * 
 * @author Rico Schrage
 */
public interface IDrawable {

	/**
	 * Draws the current visualization of the object. It will use the given
	 * {@link Batch}.
	 * 
	 * {@link Batch#begin()} has to be called before invoking this method.
	 * 
	 * @param batch
	 *            batch which will be used to render the object
	 */
	void draw(final Batch batch);

}
