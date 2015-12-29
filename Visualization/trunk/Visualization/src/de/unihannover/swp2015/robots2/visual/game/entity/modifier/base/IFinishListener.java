package de.unihannover.swp2015.robots2.visual.game.entity.modifier.base;

/**
 * Listener, which can be added to an {@link IEntityModifier}. It will be called on finish.
 * 
 * @author Rico Schrage
 */
public interface IFinishListener {

	/**
	 * Will be called when the modifier finishes.
	 */
	public void onFinish();
	
}
