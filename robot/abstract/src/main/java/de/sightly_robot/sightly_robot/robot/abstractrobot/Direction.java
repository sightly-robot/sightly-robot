package de.sightly_robot.sightly_robot.robot.abstractrobot;

import de.sightly_robot.sightly_robot.model.interfaces.IPosition.Orientation;

/**
 * An enumeration for the moving directions of a robot.
 * 
 * @author Lenard Spiecker
 * @author Philipp Rohde
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
		Direction result = FORWARDS;
		switch (orientation) {
		case NORTH:
			result = calcDirectionNorth(robotOrientation);
			break;
		case EAST:
			result = calcDirectionEast(robotOrientation);
			break;
		case SOUTH:
			result = calcDirectionSouth(robotOrientation);
			break;
		case WEST:
			result = calcDirectionWest(robotOrientation);
			break;
		}
		return result;
	}

	/**
	 * Calculates the moving direction of a robot for driving north.
	 * 
	 * @param robotOrientation
	 *            the robot's orientation
	 * @return the moving direction to get to the next position
	 */
	private static Direction calcDirectionNorth(Orientation robotOrientation) {
		switch (robotOrientation) {
		case EAST:
			return LEFT;
		case SOUTH:
			return BACKWARDS;
		case WEST:
			return RIGHT;
		case NORTH:
			return FORWARDS;
		default:
			throw new UnknownOrientationException(robotOrientation);
		}
	}

	/**
	 * Calculates the moving direction of a robot for driving east.
	 * 
	 * @param robotOrientation
	 *            the robot's orientation
	 * @return the moving direction to get to the next position
	 */
	private static Direction calcDirectionEast(Orientation robotOrientation) {
		switch (robotOrientation) {
		case NORTH:
			return RIGHT;
		case WEST:
			return BACKWARDS;
		case SOUTH:
			return LEFT;
		case EAST:
			return FORWARDS;
		default:
			throw new UnknownOrientationException(robotOrientation);
		}
	}

	/**
	 * Calculates the moving direction of a robot for driving south.
	 * 
	 * @param robotOrientation
	 *            the robot's orientation
	 * @return the moving direction to get to the next position
	 */
	private static Direction calcDirectionSouth(Orientation robotOrientation) {
		switch (robotOrientation) {
		case EAST:
			return RIGHT;
		case NORTH:
			return BACKWARDS;
		case WEST:
			return LEFT;
		case SOUTH:
			return FORWARDS;
		default:
			throw new UnknownOrientationException(robotOrientation);
		}
	}

	/**
	 * Calculates the moving direction of a robot for driving west.
	 * 
	 * @param robotOrientation
	 *            the robot's orientation
	 * @return the moving direction to get to the next position
	 */
	private static Direction calcDirectionWest(Orientation robotOrientation) {
		switch (robotOrientation) {
		case SOUTH:
			return RIGHT;
		case EAST:
			return BACKWARDS;
		case NORTH:
			return LEFT;
		case WEST:
			return FORWARDS;
		default:
			throw new UnknownOrientationException(robotOrientation);
		}
	}

	/**
	 * A dedicated {@link IllegalArgumentException} for a wrong
	 * {@code Orientation} of the robot.
	 * 
	 * @author Philipp Rohde
	 */
	@SuppressWarnings("serial")
	private static class UnknownOrientationException extends IllegalArgumentException {

		/**
		 * Constructs a new {@code UnknownOrientationException} with the
		 * specified {@code Orientation} of the robot.
		 * 
		 * @param robotOrientation
		 *            the robot's orientation
		 */
		public UnknownOrientationException(Orientation robotOrientation) {
			super("unknown orientation: " + robotOrientation.name());
		}
	}
}
