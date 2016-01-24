package de.unihannover.swp2015.robots2.visual.util;

/**
 * Contains several utility methods for transformations.
 * 
 * @author Rico Schrage
 *
 */
public class TransformUtil {

	/**
	 * Calculates the shortest rotation between two angles.
	 * 
	 * @param from start point
	 * @param to end point
	 * @return shortest rotation
	 */
	public static float calculateShortestRotation(float from, float to) {
		return from + calculateShortestDistance(from, to);
	}
	
	/**
	 * Calculates the shortest distance (signed) between two angles.
	 * 
	 * @param from start point
	 * @param to end point
	 * @return signed distance
	 */
	public static float calculateShortestDistance(float from, float to) {
		float difference = to - from; 
		return wrap(difference, -180.0f, 180.0f); 
	}

	/**
	 * Wraps a value into a given interval described by max and min values.
	 *  
	 * @param value value you want to wrap into that interval
	 * @param lower min value of the interval
	 * @param upper max value of the interval
	 * @return wrapped value
	 */
	public static float wrap(float value, float lower, float upper) { 
	  if (upper <= lower) 
		  throw new ArithmeticException("Rotary bounds are of negative or zero size"); 
	 
	  float distance = upper - lower; 
	  float times = (float) Math.floor((value - lower) / distance); 
	 
	  return value - (times * distance); 
	}

}
