package de.unihannover.swp2015.robots2.visual.util;

import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

/**
 * Utility methods for working with the model provided by the protocol.
 * 
 * @author Rico Schrage
 */
public class ModelUtil {

	private ModelUtil() {
		// utility class
	}

	/**
	 * Maps the orientation to the rotation libgdx expects. Assumes that north
	 * equals 0�. <br>
	 * <br>
	 * NORTH -> 0 <br>
	 * SOUTH -> 180 <br>
	 * WEST -> -90 <br>
	 * EAST -> 90 <br>
	 * 
	 * @param direction
	 *            orientation, which should get mapped
	 * @return Appropriate rotation to the given direction
	 */
	public static float calculateRotation(final Orientation direction) {
		switch (direction) {
		case SOUTH:
			return 180;

		case NORTH:
			return 0;

		case WEST:
			return -90;

		case EAST:
			return 90;

		default:
			return 0;
		}
	}

}
