package de.unihannover.swp2015.robots2.abstractrobot;

import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

/**
 * An enumeration for the moving directions of a robot.
 * 
 * @author Lenard Spiecker
 */
public enum Direction {

	FORWARDS, RIGHT, BACKWARDS, LEFT;

	/**
	 * Calculates the moving direction of the robot for his current orientation
	 * and the orientation of the next position according to the current one.
	 * 
	 * @param robotOrientation
	 *            the robot's orientation
	 * @param orientation
	 *            the orientation of the next position according to the current
	 *            position
	 * @return the moving direction to get to the next position
	 */
	public static Direction calcDirection(Orientation robotOrientation, Orientation orientation) {
		switch (orientation) {
		case NORTH:
			switch (robotOrientation) {
			case EAST:
				return LEFT;
			case SOUTH:
				return BACKWARDS;
			case WEST:
				return RIGHT;
			case NORTH:
				return FORWARDS;
			}
			break;
		case EAST:
			switch (robotOrientation) {
			case NORTH:
				return RIGHT;
			case WEST:
				return BACKWARDS;
			case SOUTH:
				return LEFT;
			case EAST:
				return FORWARDS;
			}
			break;
		case SOUTH:
			switch (robotOrientation) {
			case EAST:
				return RIGHT;
			case NORTH:
				return BACKWARDS;
			case WEST:
				return LEFT;
			case SOUTH:
				return FORWARDS;
			}
			break;
		case WEST:
			switch (robotOrientation) {
			case SOUTH:
				return RIGHT;
			case EAST:
				return BACKWARDS;
			case NORTH:
				return LEFT;
			case WEST:
				return FORWARDS;
			}
			break;
		}
		return FORWARDS;
	}
}
