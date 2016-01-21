package de.unihannover.swp2015.robots2.ai.graph;

import java.util.ArrayList;
import java.util.List;

import de.unihannover.swp2015.robots2.ai.core.Robot;
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
		
		this.neighbors = new ArrayList<Edge>();
	}

	public Node(int x, int y) {
		this.x = x;
		this.y = y;
		
		this.neighbors = new ArrayList<Edge>();
	}

	public Node(int x, int y, int food) {
		this(x, y);
		this.ressourceValue = food;
	
		this.neighbors = new ArrayList<Edge>();
	}

	public Node(int x, int y, int food, int growRate) {
		this(x, y, food);
		this.growRate = growRate;
		
		this.neighbors = new ArrayList<Edge>();
	}
	
	public void setRobot(Robot robot) {
		this.robot = robot;
	}

	public List<Edge> getNeighbors() {
		return neighbors;
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

	public int getY() {
		return y;
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
	
	public Robot getRobot() {
		return this.robot;
	}
}