package de.unihannover.swp2015.robots2.graph;

import java.util.ArrayList;
import java.util.List;

public class AIGraph {
	
	private Node[][] nodes; //as array for potentially faster calculations, else a list would do, too
	private Node myPos; //position of robot, possibly creates redundancy in "myself" variable in Robot.java
	private int dimX, dimY; //x and y dimensions
		
	private class Node {
		private List<Edge> neighbors;
		private int food;
		private boolean robot; //true, if a robot is currently on this Field (potentially redundant)
		private Robot robot; //robot, who is on the field or null, if there isn't one
		private int x;
		private int y;
		//private double foodGrowth; // per Second, maybe later
		//private double weight; //used later
	
		public Node() {
			this.neighbors = new ArrayList<Edge>();
		}
		
		//adds Node to list of neighbors
		public void addNeighbor(Edge edge) {
			this.neighbors.add(edge);
		}
		
		
		public List<Edge> getNeighbors() {
			return this.neighbors;
		}
	}

	private class Edge {
		private Node source; // Ggf. redundant
		private Node target;
		private Orientation direction;

		public Edge(Node source, Node target, Orientation dir) {
			this.source = source;
			this.target = target;
			this.direction = dir;
		}
	}
	
	public AIGraph(int fieldX, int fieldY) { //creates graph of field without walls/ resources/ robots
		nodes = new Node[fieldX][fieldY];
		this.dimX = fieldX;
		this.dimY = fieldY;
		for(int i = 0; i < fieldX; i++) {
			for(int j = 0; j < fieldY; j++) {
				nodes[i][j] = new Node();
			}
		}
		for(int i = 0; i < fieldX; i++) {
			for(int j = 0; j < fieldY; j++) {
				if(i > 0) nodes[i][j].addNeighbor(nodes[i - 1][j]);
				if(j > 0) nodes[i][j].addNeighbor(nodes[i][j - 1]);
				if(i < fieldX - 1) nodes[i][j].addNeighbor(nodes[i + 1][j]);
				if(j < fieldY - 1) nodes[i][j].addNeighbor(nodes[i][j + 1]);
			}
		}
	}

	/**
	 * Transforms given map data to an AIGraph object
	 * @param map Map data object
	 */
	public void mapToGraph(GameMap map) {
		/*
		Maybe use this(x,y) and make method static instead of initializing new graph
		 */
		AIGraph graph = new AIGraph(map.getWidth(), map.getHeight());

		for(Field field : map.getFields()) {
			// ...
		}
	}
}