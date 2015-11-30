package de.unihannover.swp2015.robots2.visual.util;

import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;

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
	
	public static boolean checkDriveDirectionPlusNeighbours(final IField model, final IStage stage, final Orientation direction) {
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
	
	public static int countTrue(final boolean[] boolArray) {
		int counter = 0;
		for (int i = 0; i < boolArray.length; ++i) {
			if (boolArray[i])
				counter++;
		}
		return counter;
	}
	
	public static int convertToInt(final boolean[] boolArray) {
		int result = 0;
		for (int i = 0; i < boolArray.length; ++i) {
			if (boolArray[i])
				result += Math.pow(10, i);
		}
		return result;
	}
	
}
