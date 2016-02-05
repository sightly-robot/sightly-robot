package de.sightly_robot.sightly_robot.visual.core;

/**
 * All classes that implement this interface are marked as
 * <code>updateable</code>.
 * 
 * This means you can call {@link IUpdateable#update()} to update the internal
 * state of these objects. Note: {@link IUpdateable#update()} has to be called
 * before {@link IRenderable#render()} otherwise the resulting behavior may be
 * undefined.
 * 
 * @author Rico Schrage
 */
public interface IUpdateable {

	/**
	 * Should contain everything which affects the state of the object.
	 */
	void update();

}
