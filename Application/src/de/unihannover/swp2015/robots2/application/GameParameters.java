package de.unihannover.swp2015.robots2.application;

/**
 * A holder for game paramters (loaded from the map).
 *  
 * @author Tim Ebbeke
 */
public class GameParameters {
	private double movementSpeed = 0.; // in ?
	private int rotationSpeed = 0; // in milliseconds
	
	/**
	 * Constructor for default state.
	 */
	GameParameters() {}
	
	/**
	 * Constructor for initialized state.
	 * 
	 * @param movementSpeed Maximum movement speed of virtual robots.
	 * @param maxStayTime Maximum stay time for roboters.
	 */
	GameParameters(double movementSpeed, int rotationSpeed) {
		this.movementSpeed = movementSpeed;
		this.rotationSpeed = rotationSpeed;
	}
	
	/**
	 * Getter for virtual robot movement speed. 
	 * @return Returns the robots movement speed.
	 */
	public double getMovementSpeed() {
		return movementSpeed;
	}
	
	/**
	 * Setter for virtual robot movement speed. 
	 * @param movementSpeed virtual robot movement speed. 
	 */
	public void setMovementSpeed(double movementSpeed) {
		this.movementSpeed = movementSpeed;
	}
	
	/**
	 * Getter for maximum stay time on a field for any robot in milliseconds.
	 * @return Returns the maximum stay time on a field for any robot in milliseconds.
	 */
	public int getRotationSpeed() {
		return rotationSpeed;
	}
	
	/**
	 * Setter for maximum stay time on a field for any robot in milliseconds.
	 * @param maxStayTime maximum stay time on a field for any robot in milliseconds.
	 */
	public void setRotationSpeed(int rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}
	
	
}
