package de.unihannover.swp2015.robots2.visual.game.entity.modifier;

import de.unihannover.swp2015.robots2.visual.core.entity.IEntity;
import de.unihannover.swp2015.robots2.visual.core.entity.SingleValueEntityModifier;
import de.unihannover.swp2015.robots2.visual.util.math.function.IEaseFunction;
import de.unihannover.swp2015.robots2.visual.util.math.function.LinearEaseFunction;

/**
 * Specialized modifier for tick-wise move modification.
 * 
 * See {@link IEntityModifier} for more information.
 * 
 * @author Rico Schrage
 */
public class MoveModifierX extends SingleValueEntityModifier {

	/**
	 * Constructs a MoveModifier for the x direction using
	 * {@link LinearEaseFunction}.
	 * 
	 * @param target
	 *            modifier target
	 * @param duration
	 *            estimated time
	 * @param fromValue
	 *            start value
	 * @param toValue
	 *            end value
	 */
	public MoveModifierX(IEntity target, float duration, float fromValue,
			float toValue) {
		this(target, duration, fromValue, toValue, LinearEaseFunction
				.getInstance());
	}

	/**
	 * Constructs a MoveModifier for the x direction.
	 * 
	 * @param target
	 *            modifier target
	 * @param duration
	 *            estimated time
	 * @param fromValue
	 *            start value
	 * @param toValue
	 *            end value
	 * @param easeFunction
	 *            {@link IEaseFunction}
	 */
	public MoveModifierX(IEntity target, float duration, float fromValue,
			float toValue, final IEaseFunction easeFunction) {
		super(target, duration, fromValue, toValue, easeFunction);
	}

	@Override
	protected void onSetInitialValue(float value, IEntity target) {
		target.setPosition(value, target.getPositionY());
	}

	@Override
	protected void onSetValue(float value, IEntity target) {
		target.setPosition(value, target.getPositionY());
	}
}
