package de.unihannover.swp2015.robots2;

/**
 * Basic class that represents a robot.
 *  
 * @author Tim
 */
public class Robot {
	private CardinalDirection faceingDirection;
	private Coordinate position;
	private boolean virtual;
	
	public CardinalDirection getFaceingDirection() {
		return faceingDirection;
	}
	public void setFaceingDirection(CardinalDirection faceingDirection) {
		this.faceingDirection = faceingDirection;
	}
	public Coordinate getPosition() {
		return position;
	}
	public void setPosition(Coordinate position) {
		this.position = position;
	}	
	public boolean isVirtual() {
		return this.virtual;
	}
	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}
}
