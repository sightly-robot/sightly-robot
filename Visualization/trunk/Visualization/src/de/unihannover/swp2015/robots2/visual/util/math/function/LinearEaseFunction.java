package de.unihannover.swp2015.robots2.visual.util.math.function;

/**
 * Linear ease function (<code>currentDuration/duration</code>).
 * 
 * @author Rico Schrages
 */
public class LinearEaseFunction implements IEaseFunction {

	private static LinearEaseFunction instance = new LinearEaseFunction();
	
	private LinearEaseFunction() {}
	
	/**
	 * @return the static instance of the function.
	 */
	public static LinearEaseFunction getInstance() {
		return instance;
	}
	
	@Override
	public float getPercentage(float currentDuration, float duration) {
		return currentDuration/duration;
	}

}
