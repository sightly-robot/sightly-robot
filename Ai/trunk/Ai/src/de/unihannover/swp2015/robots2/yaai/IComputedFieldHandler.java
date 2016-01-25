package de.unihannover.swp2015.robots2.yaai;

/**
 * Defines an interface for {@link YetAnotherAi} providing an event handler to
 * be called when the {@link IYaaiCalculator} has calculated a new field.
 * 
 * The IComputedFieldHandler may request this field in return dependent on its
 * state.
 * 
 * @author Michael Thies
 */
public interface IComputedFieldHandler {

	/**
	 * Event handler to be called when a new field has been computed by the
	 * known {@link IYaaiCalculator}.
	 */
	public void onNewFieldComputed();

}
