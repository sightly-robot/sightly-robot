package de.unihannover.swp2015.robots2.yaai.model;

import java.util.List;
import java.util.Vector;

import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.yaai.YetAnotherAi;

/**
 * A node of the {@link Graph} corresponding to a single Field of the games
 * Stage.
 * 
 * The node stores additional data used for different steps of path calculation
 * by {@link YetAnotherAi} and holds a list of following nodes (reachable
 * Fields) for faster iteration.
 * 
 * @author Michael Thies
 */
public class Node {
	private final IField field;
	private float weight;
	private List<Node> edges;

	/** distance from our current position. Used for weight calculation. */
	private int distance;
	/**
	 * likelihood that any other robot will reach this Node before us. Assuming
	 * they have random Ais. Used for weight calculation.
	 */
	private float otherRobotLikelihood;

	public Node(IField field) {
		this.field = field;
		this.edges = new Vector<>();
	}

	/**
	 * Get the weight of this node. The higher the better (e.g. more food, no
	 * other robots, ...).
	 * 
	 * @return The current weight of the node.
	 */
	public float getWeight() {
		return this.weight;
	}

	/**
	 * Get the Field in the controller's data model corresponding to this graph
	 * node.
	 * 
	 * @return The Field corresponding to this node.
	 */
	public IField getField() {
		return this.field;
	}

	/**
	 * Get the Edges of the Graph that start at this node. Each edge is a Node,
	 * that is reachable from here direct connection, no wall in current map.
	 * 
	 * @return A list of Nodes reachable from this node
	 */
	public List<Node> getEdges() {
		return this.edges;
	}

	/**
	 * Set the weight of this node. The higher the better (e.g. more food, no
	 * other robots, ...).
	 * 
	 * @param weight
	 *            The new weight of the node.
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public float getOtherRobotLikelihood() {
		return otherRobotLikelihood;
	}

	public void setOtherRobotLikelihood(float otherRobotLikelihood) {
		this.otherRobotLikelihood = otherRobotLikelihood;
	}

}
