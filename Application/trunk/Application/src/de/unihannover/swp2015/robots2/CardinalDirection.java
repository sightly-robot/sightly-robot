package de.unihannover.swp2015.robots2;

public enum CardinalDirection {
	NORTH(0),
	EAST(90),
	SOUTH(180),
	WEST(270);
	
	private final int angle;
	CardinalDirection(int angle) {
		this.angle = angle;
	}
	
	public int getAngle() {
		return this.angle;
	}
	
	public int getAngleDifference(CardinalDirection otherDirection) {
		return Math.max(otherDirection.getAngle(), getAngle()) - 
			   Math.min(otherDirection.getAngle(), getAngle());
	}
}
