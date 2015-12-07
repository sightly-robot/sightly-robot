package de.unihannover.swp2015.robots2.visual.entity.modifier.base;

import de.unihannover.swp2015.robots2.visual.core.base.IUpdateable;

/**
 * An EntityModifier can be created to successively update an entity.  
 * 
 * @author Rico Schrage
 */
public interface IEntityModifier extends IUpdateable {

	/**
	 * Describes what happens, when an update occurs.
	 */
    public void tick();
    
    /**
     * Returns whether the modifier has finished.
     */
    public boolean hasFinished();
    
    /**
     * Forces the modifier to stop.
     */
    public void kill();
    
    /**
     * Describes what happens on finish (last tick).
     */
    public void onFinish();
    
    /**
     * You can add a {@link IFinishListener}, which will be called in {@link IEntityModifier#onFinish()}.
     * @param finishListener {@link IFinishListener}
     */
    public void addFinishListener(final IFinishListener finishListener);
    
    /**
     * Describes what happens on init (before first tick).
     */
    public void onInit();
	
}
