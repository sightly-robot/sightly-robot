package de.unihannover.swp2015.robots2.svg;

import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

/**
 * Produces strings for transformations in svg files
 * 
 * @author Tim Ebbeke
 */
public class TransformationStringBuilder {
	private static int orientationToAngle(Orientation orientation) {
		switch (orientation) {
			case NORTH:
				return 0;
			case EAST:
				return 90;
			case SOUTH:
				return 180;
			case WEST:
				return 270;
		}
		return 0; // shut up! Its impossible ffs. 
	}
	
	/**
	 * Transforms a direction into a rotation angle in degree 
	 * 
	 * @param baseDirection Which direction is 0 degree?
	 * @param direction The direction to transform
	 * @return An angle in degree
	 */
	private static int getRotationFromDirection(Orientation direction, Orientation baseDirection) {
		int rotation = orientationToAngle(direction) - orientationToAngle(baseDirection);
		if (rotation < 0)
			rotation += 360;
		return rotation;
	}
		
	/**
	 * Produces transformations for objects on fields
	 * 
	 * @param pos Which field is the object on?
	 * @param fieldWidth The width of a field
	 * @param fieldHeight The height of a field
	 * @return A string that can be passed to a svg transformation attribute.
	 */
	public static String getObjectRotationTransformation(IPosition pos, Orientation baseDirection, double fieldWidth, double fieldHeight) {
		StringBuilder builder = new StringBuilder();
		builder.append("translate(").append(pos.getX() * fieldWidth).append(" ")
	           .append(pos.getY() * fieldHeight)
	       	   .append(") rotate(").append(getRotationFromDirection(pos.getOrientation(), baseDirection))
		       .append(" ").append(fieldWidth / 2).append(" ").append(fieldHeight / 2).append(")")
		;
		return builder.toString();
	}
}
