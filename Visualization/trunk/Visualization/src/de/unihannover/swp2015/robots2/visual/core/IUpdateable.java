package de.unihannover.swp2015.robots2.visual.core;

/**
 * All classes, which implement this interface are marked as <code>updateable</code>. This means you can call {@link IUpdateable#update()} to update the internal state of these objects.
 * Note: {@link IUpdateable#update()} have to be called before {@link IRenderable#render()}, otherwise the resulting behavior is probably undefined.
 * 
 * @author Rico Schrage
 */
public interface IUpdateable {

	/**
	 * Should contain everything, which affects the state of the object.
	 */
	void update();
	
}
