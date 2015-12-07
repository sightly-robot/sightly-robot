package de.unihannover.swp2015.robots2.visual.entity.modifier;

import de.unihannover.swp2015.robots2.visual.entity.IEntity;
import de.unihannover.swp2015.robots2.visual.entity.modifier.base.SingleValueEntityModifier;
import de.unihannover.swp2015.robots2.visual.util.math.function.IEaseFunction;
import de.unihannover.swp2015.robots2.visual.util.math.function.LinearEaseFunction;

/**
 * Specialized modifier for tick-wise move modification. 
 * {@link IEntityModifier} for more information. 
 * 
 * @author Rico Schrage
 */
public class RotationModifier extends SingleValueEntityModifier {
	
	/**
	 * Constructs a MoveModifier using {@link LinearEaseFunction}.
	 * 
	 * @param target modifier target
	 * @param duration estimated time
	 * @param fromValue start value
	 * @param toValue end value
	 */
	public RotationModifier(IEntity target, float duration, float fromValue, float toValue) {
		this(target, duration, fromValue, toValue, LinearEaseFunction.getInstance());
	}
	
	/**
	 * Constructs a MoveModifier.
	 * 
	 * @param target modifier target
	 * @param duration estimated time
	 * @param fromValue start value
	 * @param toValue end value
	 * @param easeFunction {@link IEaseFunction}
	 */
	public RotationModifier(IEntity target, float duration, float fromValue, float toValue, final IEaseFunction easeFunction) {
		super(target, duration, fromValue, toValue, easeFunction);
	}

	@Override
	protected void onSetInitialValue(float value, IEntity target) {
		target.setRotation(value);
	}

	@Override
	protected void onSetValue(float value, IEntity target) {
		target.setRotation(value);
	}
}