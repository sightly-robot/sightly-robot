package de.sightly_robot.sightly_robot.visual.game.entity.modifier;

import de.sightly_robot.sightly_robot.visual.core.entity.IEntity;
import de.sightly_robot.sightly_robot.visual.core.entity.SingleValueEntityModifier;
import de.sightly_robot.sightly_robot.visual.util.math.function.IEaseFunction;
import de.sightly_robot.sightly_robot.visual.util.math.function.LinearEaseFunction;

/**
 * specialized modifier for tick-wise alpha modification
 * 
 * See {@link IEntityModifier} for more information.
 * 
 * @author Rico Schrage
 */
public class AlphaModifier extends SingleValueEntityModifier {

	/**
	 * Constructs a BlurModifier using {@link LinearEaseFunction}.
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
	public AlphaModifier(IEntity target, float duration, float fromValue,
			float toValue) {
		this(target, duration, fromValue, toValue, LinearEaseFunction
				.getInstance());
	}

	/**
	 * Constructs a BlurModifier.
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
	public AlphaModifier(IEntity target, float duration, float fromValue,
			float toValue, final IEaseFunction easeFunction) {
		super(target, duration, fromValue, toValue, easeFunction);
	}

	@Override
	protected void onSetInitialValue(float value, IEntity entity) {
		entity.getColor().a = value;

	}

	@Override
	protected void onSetValue(float value, IEntity entity) {
		entity.getColor().a = value;
	}

}
