package de.unihannover.swp2015.robots2.svg;

import de.unihannover.swp2015.robots2.CardinalDirection;
import de.unihannover.swp2015.robots2.Position;

/**
 * Produces strings for transformations in svg files
 * 
 * @author Tim Ebbeke
 */
public class TransformationStringBuilder {
	/**
	 * Transforms a direction into a rotation angle in degree 
	 * 
	 * @param baseDirection Which direction is 0 degree?
	 * @param direction The direction to transform
	 * @return An angle in degree
	 */
	private static int getRotationFromDirection(CardinalDirection direction, CardinalDirection baseDirection) {
		int rotation = direction.getAngle() - baseDirection.getAngle();
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
	public static String getObjectRotationTransformation(Position pos, CardinalDirection baseDirection, double fieldWidth, double fieldHeight) {
		StringBuilder builder = new StringBuilder();
		builder.append("translate(").append(pos.getX() * fieldWidth).append(" ")
	           .append(pos.getY() * fieldHeight)
	       	   .append(") rotate(").append(getRotationFromDirection(pos.getFaceingDirection(), baseDirection))
		       .append(" ").append(fieldWidth / 2).append(" ").append(fieldHeight / 2).append(")")
		;
		return builder.toString();
	}
}
