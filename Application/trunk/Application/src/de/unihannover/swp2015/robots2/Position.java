package de.unihannover.swp2015.robots2;

/**
 * A coordinate representing a point in a map
 * @author Tim
 */
public class Position {
	private int x;
	private int y;
	CardinalDirection faceingDirection;
	
	Position() {
		
	}	
	Position(int x, int y, CardinalDirection faceingDirection) {
		this.x = x;
		this.y = y;
		this.faceingDirection = faceingDirection;
	}	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public CardinalDirection getFaceingDirection() {
		return faceingDirection;
	}
	public void setFaceingDirection(CardinalDirection faceingDirection) {
		this.faceingDirection = faceingDirection;
	}
}
