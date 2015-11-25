package de.unihannover.swp2015.robots2.graph;

import java.util.ArrayList;
import java.util.List;

import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;

public class AIGraph {

	private Node[][] nodes; // as array for potentially faster calculations,
							// else a list would do, too
	private Node myPos; // position of robot, possibly creates redundancy in
						// "myself" variable in Robot.java
	private int dimX, dimY; // x and y dimensions

	private class Node {
		private List<Edge> neighbors;
		private int ressourceValue;
		private boolean isOccupied; // true, if a robot is currently on this
									// Field (potentially redundant)
		private Robot robot; // robot, who is on the field or null, if there
								// isn't one
		private int x;
		private int y;
		// private double foodGrowth; // per Second, maybe later
		// private double weight; //used later

		public Node() {
			this.setNeighbors(new ArrayList<Edge>());
		}

		public List<Edge> getNeighbors() {
			return neighbors;
		}

		public void setNeighbors(List<Edge> neighbors) {
			this.neighbors = neighbors;
		}
		
		public void addNeighbor(Edge edge) {
			this.neighbors.add(edge);
		}

		public int getRessourceValue() {
			return ressourceValue;
		}

		public void setRessourceValue(int ressourceValue) {
			this.ressourceValue = ressourceValue;
		}

		public boolean isOccupied() {
			return isOccupied;
		}

		public void setOccupied(boolean isOccupied) {
			this.isOccupied = isOccupied;
		}

		public Robot getRobot() {
			return robot;
		}

		public void setRobot(Robot robot) {
			this.robot = robot;
		}
		
		public void removeRobot() {
			this.robot = null;
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

	private class Edge {
		private Node source; // Ggf. redundant
		private Node target;
		private Orientation direction;

		public Edge(Node source, Node target, Orientation dir) {
			this.setSource(source);
			this.setTarget(target);
			this.setDirection(dir);
		}

		public Node getSource() {
			return source;
		}

		public void setSource(Node source) {
			this.source = source;
		}

		public Node getTarget() {
			return target;
		}

		public void setTarget(Node target) {
			this.target = target;
		}

		public Orientation getDirection() {
			return direction;
		}

		public void setDirection(Orientation direction) {
			this.direction = direction;
		}
	}

	public AIGraph(int fieldX, int fieldY) { // creates graph of field without
												// walls/ resources/ robots
		nodes = new Node[fieldX][fieldY];
		this.dimX = fieldX;
		this.dimY = fieldY;
		for (int i = 0; i < fieldX; i++) {
			for (int j = 0; j < fieldY; j++) {
				nodes[i][j] = new Node();
			}
		}
		// TODO
		for (int i = 0; i < fieldX; i++) {
			for (int j = 0; j < fieldY; j++) {
				if (i > 0)
					nodes[i][j].addNeighbor(new Edge(nodes[i][j], nodes[i - 1][j], IPosition.Orientation.West));
				if (j > 0)
					nodes[i][j].addNeighbor(new Edge(nodes[i][j], nodes[i][j - 1], IPosition.Orientation.North));
				if (i < fieldX - 1)
					nodes[i][j].addNeighbor(new Edge(nodes[i][j], nodes[i + 1][j], IPosition.Orientation.East));
				if (j < fieldY - 1)
					nodes[i][j].addNeighbor(new Edge(nodes[i][j], nodes[i][j + 1], IPosition.Orientation.South));
			}
		}
	}
	
	public AIGraph(IStage stage) {
		this.dimX = stage.getWidth();
		this.dimY = stage.getHeight();
		this.nodes = new Node[this.dimX][this.dimY];
		
		for (int i = 0; i < fieldX; i++) {
			for (int j = 0; j < fieldY; j++) {
				nodes[i][j] = new Node();
			}
		}
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				IField field = map.getField(i, j);
				nodes[i][j].setRessourceValue(field.getFood());
				//nodes[i][j].setFoodGrowth(field.getGrowingRate()); maybe later
				if (i > 0 && !field.isWall(IPosition.Orientation.West))
					nodes[i][j].addNeighbor(new Edge(nodes[i][j], nodes[i - 1][j], IPosition.Orientation.West));
				if (j > 0 && !field.isWall(IPosition.Orientation.North))
					nodes[i][j].addNeighbor(new Edge(nodes[i][j], nodes[i][j - 1], IPosition.Orientation.North));
				if (i < fieldX - 1 && !field.isWall(IPosition.Orientation.East))
					nodes[i][j].addNeighbor(new Edge(nodes[i][j], nodes[i + 1][j], IPosition.Orientation.East));
				if (j < fieldY - 1 && !field.isWall(IPosition.Orientation.South))
					nodes[i][j].addNeighbor(new Edge(nodes[i][j], nodes[i][j + 1], IPosition.Orientation.South));
			}
		}
	}

	/**
	 * Transforms given map data to an AIGraph object
	 * 
	 * @param map
	 *            Map data object
	 */
	public void loadFromMap(IStage map) {
		/*
		 * Maybe use this(x,y) and make method static instead of initializing
		 * new graph
		 */
		//AIGraph graph = new AIGraph(map.getWidth(), map.getHeight());
		int width = map.getWidth();
		int height = map.getHeight();

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				IField field = map.getField(i, j);
				int x = field.getX();
				int y = field.getY();
				int food = field.getFood();
				int growRate = field.getGrowingRate();
			}
		}
	}
}