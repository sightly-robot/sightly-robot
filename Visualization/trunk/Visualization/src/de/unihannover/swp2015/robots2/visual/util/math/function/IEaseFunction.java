package de.unihannover.swp2015.robots2.visual.util.math.function;

/**
 * Describes a mathematical ease function.
 * 
 * @author Rico Schrage
 */
public interface IEaseFunction {
	
	/**
	 * Returns the currently reached percentage of <code>duration</code>. The value is based on a mathematical function. 
	 * 
	 * @param currentDuration current duration
	 * @param duration estimated duration
	 * @return percentage [0,1]
	 */
	float getPercentage(final float currentDuration, final float duration);

}
