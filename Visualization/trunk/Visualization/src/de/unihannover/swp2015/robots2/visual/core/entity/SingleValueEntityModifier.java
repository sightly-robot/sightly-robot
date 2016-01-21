package de.unihannover.swp2015.robots2.visual.core.entity;

import de.unihannover.swp2015.robots2.visual.util.math.function.IEaseFunction;
import de.unihannover.swp2015.robots2.visual.util.math.function.LinearEaseFunction;

/**
 * Implements an EntityModifier. It should be used when you want to modify
 * exactly one value (stepwise of course).
 * 
 * @author Rico Schrage
 */
public abstract class SingleValueEntityModifier extends EntityModifier {

	/** Start value */
	protected final float fromValue;

	/** End value, it will always be the last value the modifier will set. */
	protected final float toValue;

	/** {@link IEaseFunction} */
	protected final IEaseFunction easeFunction;

	/**
	 * Constructs SingleValueEntityModifier using {@link LinearEaseFunction}.
	 * 
	 * @param target
	 *            target
	 * @param duration
	 *            estimated duration
	 * @param fromValue
	 *            start value
	 * @param toValue
	 *            end value
	 */
	public SingleValueEntityModifier(final IEntity target, final float duration, final float fromValue,
			final float toValue) {
		this(target, duration, fromValue, toValue, LinearEaseFunction.getInstance());
	}

	/**
	 * Constructs SingleValueEntityModifier.
	 * 
	 * @param target
	 *            target
	 * @param duration
	 *            estimated duration
	 * @param fromValue
	 *            start value
	 * @param toValue
	 *            end value
	 * @param easeFunction
	 *            {@link IEaseFunction}
	 */
	public SingleValueEntityModifier(final IEntity target, final float duration, final float fromValue,
			final float toValue, final IEaseFunction easeFunction) {
		super(target, duration);

		this.fromValue = fromValue;
		this.toValue = toValue;

		this.easeFunction = easeFunction;
	}

	@Override
	public void tick() {

		if (fromValue >= toValue) {
			onSetValue(easeFunction.getPercentage(currentDuration, duration) * Math.abs(toValue - fromValue) + toValue,
					target);
		} else {
			onSetValue(easeFunction.getPercentage(duration - currentDuration, duration) * Math.abs(toValue - fromValue)
					+ fromValue, target);
		}
	}

	/**
	 * Will be called when the initial value should be set.
	 * 
	 * @param value
	 *            initial value
	 * @param entity
	 *            target
	 */
	protected abstract void onSetInitialValue(final float value, final IEntity entity);

	/**
	 * Will be called when a value should be set.
	 * 
	 * @param value
	 *            currentValue calculated with the {@link IEaseFunction}
	 * @param entity
	 *            target
	 */
	protected abstract void onSetValue(final float value, final IEntity entity);

	@Override
	public void onFinish() {
		super.onFinish();

		onSetValue(toValue, target);
	}

	@Override
	public void onInit() {
		onSetInitialValue(fromValue, target);
	}

}
