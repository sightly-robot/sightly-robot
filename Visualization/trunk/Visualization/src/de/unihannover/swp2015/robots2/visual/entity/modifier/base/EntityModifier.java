package de.unihannover.swp2015.robots2.visual.entity.modifier.base;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;

import de.unihannover.swp2015.robots2.visual.entity.IEntity;

/**
 * Base class for most of the entity-modifier. Contains a standard implementation of {@link IUpdateable.update}.
 * 
 * @author Rico Schrage
 */
public abstract class EntityModifier implements IEntityModifier {

	/**
	 * Estimated duration.
	 */
    protected final float duration;
    
    /**
     * Remaining duration. 
     */
    protected float currentDuration;

    /**
     * Target, which the modifier will affect.
     */
    protected final IEntity target;

    /**
     * True if currentDuration <= 0
     */
    protected boolean hasFinished = false;

    /**
     * List of {@link IFinishListener}
     */
    protected List<IFinishListener> finishListenerList;

    /**
     * Constructs an EntityModifier using given target and duration.
     * 
     * @param target target
     * @param duration estimated duration
     */
    public EntityModifier(final IEntity target, final float duration) {
        this.duration = duration;
        this.currentDuration = duration;
        this.target = target;
        this.finishListenerList = new ArrayList<>();
    }

    @Override
    public void update() {
        this.currentDuration -= Gdx.graphics.getDeltaTime();
        if (currentDuration > 0) {
            this.tick();
        }
        else if(!hasFinished) {
            currentDuration = 0;
            this.tick();
            this.hasFinished = true;
        }
    }

    @Override
    public void addFinishListener(IFinishListener finishListener) {
        finishListenerList.add(finishListener);
    }

    @Override
    public boolean hasFinished() {
        return hasFinished;
    }

    @Override
    public void onFinish() {
        for (int i = 0 ; i < finishListenerList.size(); ++i) {
            finishListenerList.get(i).onFinish();
        }
    }

    @Override
    public void kill() {
        this.hasFinished = true;
    }

    @Override
    public void onInit() {}

}