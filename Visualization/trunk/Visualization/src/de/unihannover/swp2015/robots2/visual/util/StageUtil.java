package de.unihannover.swp2015.robots2.visual.util;

import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;

/**
 * All utility methods are related to {@link IStage}.
 * 
 * @author Rico Schrage
 */
public class StageUtil {

	private StageUtil() {
		// utility class
	}

	/**
	 * Checks if it is possible to drive from field <code>model</code> in the
	 * direction <code>direction</code>.
	 * 
	 * @param model
	 *            current field of the robot
	 * @param direction
	 *            orientation of the robot
	 * @param width
	 *            width of the stage
	 * @param height
	 *            height of the stage
	 * @return true if there's a wall, false otherwise
	 */
	public static boolean checkDriveDirection(final IField model,
			final Orientation direction, final int width, final int height) {
		if (model.isWall(direction))
			return true;
		switch (direction) {
		case NORTH:
			if (model.getY() == 0)
				return true;
			break;

		case EAST:
			if (model.getX() + 1 == width)
				return true;
			break;

		case WEST:
			if (model.getX() == 0)
				return true;
			break;

		case SOUTH:
			if (model.getY() + 1 == height)
				return true;
		default:
			break;
		}
		return false;
	}

	/**
	 * Checks if there's a wall in the driving direction.
	 * 
	 * It doesn't matter, if the wall is set in <code>model</code> or in one of
	 * it's neighbors.
	 *
	 * @param model
	 *            target field
	 * @param stage
	 *            current stage
	 * @param direction
	 *            desired driving direction
	 * @return true if there is a wall, false otherwise
	 */
	public static boolean checkDriveDirectionOrNeighbors(final IField model,
			final IStage stage, final Orientation direction) {
		boolean result = checkDriveDirection(model, direction,
				stage.getWidth(), stage.getHeight());
		switch (direction) {
		case NORTH:
			if (model.getY() == 0)
				return result;
			return result
					|| stage.getField(model.getX(), model.getY() - 1).isWall(
							Orientation.SOUTH);

		case EAST:
			if (model.getX() + 1 == stage.getWidth())
				return result;
			return result
					|| stage.getField(model.getX() + 1, model.getY()).isWall(
							Orientation.WEST);

		case WEST:
			if (model.getX() == 0)
				return result;
			return result
					|| stage.getField(model.getX() - 1, model.getY()).isWall(
							Orientation.EAST);

		case SOUTH:
			if (model.getY() + 1 == stage.getHeight())
				return result;
			return result
					|| stage.getField(model.getX(), model.getY() + 1).isWall(
							Orientation.NORTH);
		default:
			break;
		}
		return result;
	}

	/**
	 * Checks if there's a solid wall in the driving direction.
	 * 
	 * In this method the wall has to be set in the current field (
	 * <code>model</code>) <b>and</b> in the regarding neighbor.
	 * 
	 * @param model
	 *            target field
	 * @param stage
	 *            current stage
	 * @param direction
	 *            desired driving direction
	 * @return true if there's a wall in that direction, false otherwise
	 */
	public static boolean checkDriveDirectionAndNeighbours(final IField model,
			final IStage stage, final Orientation direction) {
		boolean result = checkDriveDirection(model, direction,
				stage.getWidth(), stage.getHeight());
		switch (direction) {
		case NORTH:
			if (model.getY() == 0)
				return result;
			return result
					&& stage.getField(model.getX(), model.getY() - 1).isWall(
							Orientation.SOUTH);

		case EAST:
			if (model.getX() + 1 == stage.getWidth())
				return result;
			return result
					&& stage.getField(model.getX() + 1, model.getY()).isWall(
							Orientation.WEST);

		case WEST:
			if (model.getX() == 0)
				return result;
			return result
					&& stage.getField(model.getX() - 1, model.getY()).isWall(
							Orientation.EAST);

		case SOUTH:
			if (model.getY() + 1 == stage.getHeight())
				return result;
			return result
					&& stage.getField(model.getX(), model.getY() + 1).isWall(
							Orientation.NORTH);
		default:
			break;
		}
		return result;
	}

	/**
	 * Checks if there is a wall in the drive direction.
	 * 
	 * In this method the wall has to be set in the current field (
	 * <code>model</code>) <b>and not</b> in the regarding neighbor.
	 * 
	 * @param model
	 *            target field
	 * @param stage
	 *            current stage
	 * @param direction
	 *            desired driving direction
	 * @return true if there is a wall in only one of the fields , false
	 *         otherwise
	 */
	public static boolean checkDriveDirectionAndNotNeighbours(
			final IField model, final IStage stage, final Orientation direction) {
		boolean result = checkDriveDirection(model, direction,
				stage.getWidth(), stage.getHeight());
		switch (direction) {
		case NORTH:
			if (model.getY() == 0)
				return false;
			return result
					&& !stage.getField(model.getX(), model.getY() - 1).isWall(
							Orientation.SOUTH);

		case EAST:
			if (model.getX() + 1 == stage.getWidth())
				return false;
			return result
					&& !stage.getField(model.getX() + 1, model.getY()).isWall(
							Orientation.WEST);

		case WEST:
			if (model.getX() == 0)
				return false;
			return result
					&& !stage.getField(model.getX() - 1, model.getY()).isWall(
							Orientation.EAST);

		case SOUTH:
			if (model.getY() + 1 == stage.getHeight())
				return false;
			return result
					&& !stage.getField(model.getX(), model.getY() + 1).isWall(
							Orientation.NORTH);
		default:
			break;
		}
		return result;
	}

	/**
	 * Counts the number of true booleans in an array.
	 * 
	 * @param boolArray
	 *            c-style array
	 * @return number of true booleans in that array
	 */
	public static int countTrue(final boolean[] boolArray) {
		int counter = 0;
		for (int i = 0; i < boolArray.length; ++i) {
			if (boolArray[i])
				counter++;
		}
		return counter;
	}

	/**
	 * Converts a boolean array to an integer representation: <br>
	 * e.g. radix= 10 , input {true, false, true} has result 101 <br>
	 * e.g. radix= 2 , input (true, false, true) has result 5
	 * 
	 * @param boolArray
	 *            array of booleans
	 * @return integer representation
	 */
	public static int convertToInt(final boolean[] boolArray, final int radix) {
		int result = 0;
		for (int i = 0; i < boolArray.length; ++i) {
			if (boolArray[i])
				result += Math.pow(radix, i);
		}
		return result;
	}

}
