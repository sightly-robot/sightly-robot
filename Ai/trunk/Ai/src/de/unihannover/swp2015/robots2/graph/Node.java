package de.unihannover.swp2015.robots2.graph;

import java.util.List;

import de.unihannover.swp2015.robots2.core.Robot;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

public class Node {
	private List<Edge> neighbors;
	private Robot robot;

	private int ressourceValue;
	private int growRate;

	private int x;
	private int y;

	public Node(IField field) {
		this.x = field.getX();
		this.y = field.getY();

		this.growRate = field.getGrowingRate();
		this.ressourceValue = field.getFood();
	}

	public Node(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Node(int x, int y, int food) {
		this(x, y);
		this.ressourceValue = food;
	}

	public Node(int x, int y, int food, int growRate) {
		this(x, y, food);
		this.growRate = growRate;
	}
	

	/**
	 * Returns true if node is occupied with a robot, else false
	 * 
	 * @return if occupied
	 */
	public boolean isOccupied() {
		return !(this.robot == null);
	}

	public Robot getRobot() {
		return this.robot;
	}

	public void setRobot(Robot robot) {
		this.robot = robot;
	}

	public List<Edge> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(List<Edge> neighbors) {
		this.neighbors = neighbors;
	}

	public int getRessourceValue() {
		return ressourceValue;
	}

	public void setRessourceValue(int ressourceValue) {
		this.ressourceValue = ressourceValue;
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

	public int getGrowRate() {
		return growRate;
	}

	public void setGrowRate(int growRate) {
		this.growRate = growRate;
	}
	/**
	 * This method checks, whether or not there is a Wall in the given Orientation, to 
	 * find out, whether the robot would be allowed to move there.
	 * 
	 * @param orientation	Orientation, in which to check.
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
}