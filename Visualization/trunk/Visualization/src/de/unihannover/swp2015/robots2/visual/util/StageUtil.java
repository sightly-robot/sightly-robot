package de.unihannover.swp2015.robots2.visual.util;

import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;

/**
 * All utility methods related to {@link IStage}.
 * 
 * @author Rico Schrage
 */
public class StageUtil {
	
	/**
	 * Checks if it is possible to drive from field <code>model</code> in the direction <code>direction</code>. 
	 * @param model current field of the robot
	 * @param direction orientation of the roboz
	 * @param width width of the stage
	 * @param height height of the stage
	 * @return true if you are not able to drive in this direction, false otherwise
	 */
	public static boolean checkDriveDirection(final IField model, final Orientation direction, final int width, final int height) {
		if (model.isWall(direction))
				return true;
		switch(direction) {
		case NORTH: 
			if (model.getY() == 0)
				return true;
			break;
		case EAST:
			if (model.getX()+1 == width)
				return true;
			break;
		case WEST:
			if (model.getX() == 0) 
				return true;
			break;
		case SOUTH:
			if (model.getY()+1 == height)
				return true;
		}
		return false;
	}	
	
	/**
	 * Checks if there is a wall in the drive direction. In this method it does'nt matter if the wall is set in <code>model</code> or
	 * in one the neighbors of <code>model</code>.
	 *
	 * @param model target field
	 * @param stage parent of the field (stage)
	 * @param direction direction you want do drive in
	 * @return true if there is a wall in your way, false otherwise.
	 */
	public static boolean checkDriveDirectionOrNeighbors(final IField model, final IStage stage, final Orientation direction) {
		boolean result = checkDriveDirection(model, direction, stage.getWidth(), stage.getHeight());
		switch(direction) {
		case NORTH: 
			if (model.getY() == 0)
				return result;
			return result || stage.getField(model.getX(), model.getY()-1).isWall(Orientation.SOUTH);
		case EAST:
			if (model.getX()+1 == stage.getWidth())
				return result;
			return result || stage.getField(model.getX()+1, model.getY()).isWall(Orientation.WEST);
		case WEST:
			if (model.getX() == 0) 
				return result;
			return result || stage.getField(model.getX()-1, model.getY()).isWall(Orientation.EAST);
		case SOUTH:
			if (model.getY()+1 == stage.getHeight())
				return result;
			return result || stage.getField(model.getX(), model.getY()+1).isWall(Orientation.NORTH);
		}	
		return result;
	}
	
	/**
	 * Checks if there is a wall in the drive direction. In this method the wall have to be set in the current field (<code>model</code>)
	 * <b>and</b> in the regarding neighbor.
	 * 
	 * @param model target field
	 * @param stage parent
	 * @param direction direction you want to drive in
	 * @return true if there is a wall in the model in that direction and in the regarding neighbor, false otherwise
	 */
	public static boolean checkDriveDirectionAndNeighbours(final IField model, final IStage stage, final Orientation direction) {
		boolean result = checkDriveDirection(model, direction, stage.getWidth(), stage.getHeight());
		switch(direction) {
		case NORTH: 
			if (model.getY() == 0)
				return result;
			return result && stage.getField(model.getX(), model.getY()-1).isWall(Orientation.SOUTH);
		case EAST:
			if (model.getX()+1 == stage.getWidth())
				return result;
			return result && stage.getField(model.getX()+1, model.getY()).isWall(Orientation.WEST);
		case WEST:
			if (model.getX() == 0) 
				return result;
			return result && stage.getField(model.getX()-1, model.getY()).isWall(Orientation.EAST);
		case SOUTH:
			if (model.getY()+1 == stage.getHeight())
				return result;
			return result && stage.getField(model.getX(), model.getY()+1).isWall(Orientation.NORTH);
		}	
		return result;
	}
	
	/**
	 * Counts the number of true bool's in an array.
	 * 
	 * @param boolArray c-style array
	 * @return number of true booleans in the array
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
	 * Converts a boolean array to an integer representation:
	 * <br>
	 * Bsp. (radix: 10): input {true, false, true}
	 *       result 101
	 * <br>
	 * Bsp. (radix: 2) : input (true, false, true)
	 * 		 result 5
	 * 
	 * @param boolArray regarding array
	 * @return integer, every digit is either a 1 or a 0
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
