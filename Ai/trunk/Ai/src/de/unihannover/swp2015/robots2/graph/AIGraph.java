package de.unihannover.swp2015.robots2.graph;

import java.util.ArrayList;
import java.util.List;

import de.unihannover.swp2015.robots2.Field;
import de.unihannover.swp2015.robots2.GameMap;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

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
					nodes[i][j].addNeighbor(nodes[i - 1][j]);
				if (j > 0)
					nodes[i][j].addNeighbor(nodes[i][j - 1]);
				if (i < fieldX - 1)
					nodes[i][j].addNeighbor(nodes[i + 1][j]);
				if (j < fieldY - 1)
					nodes[i][j].addNeighbor(nodes[i][j + 1]);
			}
		}
	}

	/**
	 * Transforms given map data to an AIGraph object
	 * 
	 * @param map
	 *            Map data object
	 */
	public void mapToGraph(GameMap map) {
		/*
		 * Maybe use this(x,y) and make method static instead of initializing
		 * new graph
		 */
		AIGraph graph = new AIGraph(map.getWidth(), map.getHeight());

		for (Field field : map.getFields()) {
			// ...
		}
	}
}