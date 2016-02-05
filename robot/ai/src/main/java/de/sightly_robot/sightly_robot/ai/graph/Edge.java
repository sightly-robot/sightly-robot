package de.sightly_robot.sightly_robot.ai.graph;

import de.sightly_robot.sightly_robot.model.interfaces.IPosition.Orientation;

/**
 * A Class used to represent transitions between fields of the stage.
 * 
 * @author Timon Barlag, Philip Naumann
 *
 */
public class Edge {
	private Node source; 
	private Node target;
	private Orientation direction;
	
	/**
	 * Basic constructor for Edge objects. Setting Source-Node, Target-Node
	 * and Direction.
	 * 
	 * @param source The Source-Node of the Edge.
	 * @param target The Target-Node of the Edge.
	 * @param dir Orientation, the Edge is facing.
	 */
	public Edge(Node source, Node target, Orientation dir) {
		this.source = source;
		this.target = target;
		this.direction = dir;
	}
	
	/**
	 * Returns the Source-Node of the Edge.
	 * 
	 * @return The Source-Node of the Edge.
	 */
	public Node getSource() {
		return source;
	}
	
	/**
	 * Returns the Target-Node of the Edge.
	 * 
	 * @return The Target-Node of the Edge.
	 */
	public Node getTarget() {
		return target;
	}

	/**
	 * Returns the direction of the edge.
	 * 
	  * @return The Orientation the Edge is facing.
	 */
	public Orientation getDirection() {
		return direction;
	}
}
