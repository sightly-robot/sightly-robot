package de.unihannover.swp2015.robots2.ai.graph;

import java.util.ArrayList;
import java.util.List;

import de.unihannover.swp2015.robots2.ai.core.Robot;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

/**
 * A class representing fields on the stage.
 * 
 * @author Timon Barlag, Philip Naumann
 *
 */
public class Node {
	private List<Edge> neighbors;
	private Robot robot;

	private int ressourceValue;
	private int growRate;

	private int x;
	private int y;
	
	/**
	 * Basic constructor for Node objects, creating a Node resembling an IField.
	 * 
	 * @param field The IField, the Node should resemble.
	 */
	public Node(IField field) {
		this.x = field.getX();
		this.y = field.getY();

		this.growRate = field.getGrowingRate();
		this.ressourceValue = field.getFood();
		
		this.neighbors = new ArrayList<Edge>();
	}
	
	/**
	 * Basic constructor for Node objects, creating a Node on 
	 * given coordinates.
	 * 
	 * @param x The x coordinate the Node should be created on.
	 * @param y The y coordinate the Node should be created on.
	 */
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
		
		this.neighbors = new ArrayList<Edge>();
	}

	/**
	 * Basic constructor for Node objects, creating a Node on 
	 * given coordinates and setting food.
	 * 
	 * @param x The x coordinate the Node should be created on.
	 * @param y The x coordinate the Node should be created on.
	 * @param food The amount of food on the Node.
	 */
	public Node(int x, int y, int food) {
		this(x, y);
		this.ressourceValue = food;
	
		this.neighbors = new ArrayList<Edge>();
	}
	
	/**
	 * Basic constructor for Node objects, creating a Node on 
	 * given coordinates and setting food and growRate.
	 * 
	 * @param x The x coordinate the Node should be created on.
	 * @param y The x coordinate the Node should be created on.
	 * @param food The amount of food on the Node.
	 * @param growRate The rate in which food grows on that Node.
	 */
	public Node(int x, int y, int food, int growRate) {
		this(x, y, food);
		this.growRate = growRate;
		
		this.neighbors = new ArrayList<Edge>();
	}
	
	/**
	 * Sets Robot on the Node.
	 * 
	 * @param robot The Robot which should be on the Node.
	 */
	public void setRobot(Robot robot) {
		this.robot = robot;
	}

	/**
	 * Returns Edges to neighboring Nodes.
	 *  
	 * @return List of Edges to neighboring Nodes.
	 */
	public List<Edge> getNeighbors() {
		return neighbors;
	}
	
	/**
	 * Returns the amount of food on the Node.
	 * 
	 * @return The current ressourceValue.
	 */
	public int getRessourceValue() {
		return ressourceValue;
	}
	
	/**
	 * Sets the amount of food on the Node.
	 * 
	 * @param ressourceValue The current ressourceValue of the Node.
	 */
	public void setRessourceValue(int ressourceValue) {
		this.ressourceValue = ressourceValue;
	}
	
	/**
	 * Returns the x coordinate of the Node.
	 * 
	 * @return The x coordinate of the Node.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the y coordinate of the Node.
	 * 
	 * @return The y coordinate of the Node.
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * This method checks, whether or not there is a Wall in the given Orientation to 
	 * find out, whether the robot would be allowed to move there.
	 * 
	 * @param orientation	Orientation, in which to check for a wall.
	 * @return 				true if there is a Wall (the robot would not be allowed to move in that orientation)
	 * 						false otherwise
	 */
	public boolean isWall(Orientation orientation) {
		for(Edge edge : neighbors) {
			if(edge.getDirection() == orientation) {
				return false;
			}
		}
		return true;
	}
	
	public void removeRobot() {
		this.robot = null;
	}
	
	public boolean hasRobot() {
		return this.robot != null;
	}
	
	/**
	 * Returns the Robot object which is on the Node (or null, if there is none).
	 * 
	 * @return The Robot which is currently on the Node or null if there isn't one.
	 */
	public Robot getRobot() {
		return this.robot;
	}
}