package de.unihannover.swp2015.robots2;

/**
 * Basic class that represents a robot.
 *  
 * @author Tim
 */
public class Robot {
	private Position position;
	private boolean virtual;
	
	public Robot(boolean virtual) {
		this.position = new Position();
		this.virtual = virtual;
	}
	
	public Robot(Position position, boolean virtual) {
		this.position = position;
		this.virtual = virtual;
	}
	
	public Robot(int x, int y, CardinalDirection direction, boolean virtual) {
		this.position = new Position(x, y, direction);
		this.virtual = virtual;
	}
	
	public CardinalDirection getFaceingDirection() {
		return position.getFaceingDirection();
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public void setPosition(int x, int y)  {
		this.position.setX(x);
		this.position.setY(y);
	}
	public boolean isVirtual() {
		return this.virtual;
	}
	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}
}
