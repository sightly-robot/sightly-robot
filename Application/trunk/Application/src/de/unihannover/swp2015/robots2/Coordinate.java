package de.unihannover.swp2015.robots2;

/**
 * A coordinate representing a point in a map
 * @author Tim
 */
public class Coordinate {
	private int x;
	private int y;
	
	Coordinate() {
		
	}	
	Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
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
}
