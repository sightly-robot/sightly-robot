package de.sightly_robot.sightly_robot.ai.core;

import de.sightly_robot.sightly_robot.ai.graph.Node;
import de.sightly_robot.sightly_robot.model.interfaces.IPosition.Orientation;

public class Robot { 
	private String id;
	private Node position;
	//private int score;
	private Orientation orientation; // enum taken from data modell
	//private boolean myself;
	//private boolean isVirtual;
	
	/**
	 * Basic constructor for Robot objects. Only sets id.
	 * 
	 * @param id The robot's id.
	 */
	public Robot(String id) {
		this.id = id;
	}
	
	/**
	 * Basic constructor for Robot objects. Sets id, position and orientation.
	 * 
	 * @param id The robot's id.
	 * @param position The robot's position.
	 * @param orientation The robot's orientation.
	 */
	public Robot(String id, Node position, Orientation orientation) {
		this(id);
		this.position = position;
		this.orientation = orientation;
	}
	
	/**
	 * Returns the robot's current position.
	 * 
	 * @return The robot's current position.
	 */
	public Node getPosition() {
		return this.position;
	}
	
	/**
	 * Sets the robot's position.
	 * 
	 * @param node Node the robot should be put on.
	 */
	public void setPosition(Node node) {
		this.position = node;
	}
	
	/**
	 * Sets the robot's orientation.
	 * 
	 * @param orientation The orientation the robot should be facing.
	 */
	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}
}