package de.unihannover.swp2015.robots2.visual.core.entity;

import de.unihannover.swp2015.robots2.visual.core.IUpdateable;

/**
 * An EntityModifier can be created to successively update an entity.
 * 
 * @author Rico Schrage
 */
public interface IEntityModifier extends IUpdateable {

	/**
	 * describes what happens when an update occurs
	 */
	public void tick();

	/**
	 * returns whether the modifier has finished
	 */
	public boolean hasFinished();

	/**
	 * forces the modifier to stop
	 */
	public void kill();

	/**
	 * describes what happens on finish (last tick)
	 */
	public void onFinish();

	/**
	 * You can add a {@link IFinishListener} which will be called in
	 * {@link IEntityModifier#onFinish()}.
	 * 
	 * @param finishListener
	 *            {@link IFinishListener}
	 */
	public void addFinishListener(final IFinishListener finishListener);

	/**
	 * Describes what happens on initialization (before first tick).
	 */
	public void onInit();

}
