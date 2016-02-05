package de.sightly_robot.sightly_robot.visual.util;

import de.sightly_robot.sightly_robot.model.interfaces.IPosition.Orientation;

/**
 * Translate orientation to rotation
 * 
 * Utility methods for working with the model provided by the protocol.
 * 
 * @author Rico Schrage
 */
public class ModelUtil {

	private ModelUtil() {
		// utility class
	}

	/**
	 * Maps the orientation to the rotation libgdx expects assuming north equals
	 * 0°. <br>
	 * <br>
	 * NORTH -> 0 <br>
	 * SOUTH -> 180 <br>
	 * WEST -> -90 <br>
	 * EAST -> 90 <br>
	 * 
	 * @param direction
	 *            orientation to be mapped
	 * @return rotation of the given direction
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
