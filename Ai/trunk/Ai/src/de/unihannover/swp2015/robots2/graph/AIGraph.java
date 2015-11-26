package de.unihannover.swp2015.robots2.graph;

import de.unihannover.swp2015.robots2.core.Robot;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;

public class AIGraph {

	private Node[][] nodes;

	private Node myPos;

	private int dimX;
	private int dimY;

	public AIGraph() {

	}

	public AIGraph(int x, int y) {
		this.nodes = new Node[x][y];
		this.dimX = x;
		this.dimY = y;
	}

	public AIGraph(IStage map) {
		this.loadFromMap(map);
	}

	/**
	 * Loads map data into Graph object
	 * 
	 * @param map
	 *            IStage data object
	 */
	public void loadFromMap(IStage map) {
		int width = map.getWidth();
		int height = map.getHeight();

		this.nodes = new Node[width][height];
		this.dimX = width;
		this.dimY = height;

		/*
		 * Init graph from map data
		 */
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				IField field = map.getField(i, j);
				/*
				 * Check if node already exists and eventually create new one
				 */
				Node source = this.nodes[i][j] == null ? new Node(field) : this.nodes[i][j];
				if (i > 0 && !field.isWall(Orientation.WEST)) {
					Node target = this.nodes[i - 1][j] == null ? new Node(map.getField(i - 1, j))
							: this.nodes[i - 1][j];
					Edge edge = new Edge(source, target, Orientation.WEST);
					source.getNeighbors().add(edge);
				}
				if (i < this.dimX && !field.isWall(Orientation.EAST)) {
					Node target = this.nodes[i + 1][j] == null ? new Node(map.getField(i + 1, j))
							: this.nodes[i + 1][j];
					Edge edge = new Edge(source, target, Orientation.EAST);
					source.getNeighbors().add(edge);
				}
				if (j > 0 && !field.isWall(Orientation.NORTH)) {
					Node target = this.nodes[i][j - 1] == null ? new Node(map.getField(i, j - 1))
							: this.nodes[i][j - 1];
					Edge edge = new Edge(source, target, Orientation.NORTH);
					source.getNeighbors().add(edge);
				}
				if (j < this.dimY && !field.isWall(Orientation.SOUTH)) {
					Node target = this.nodes[i][j + 1] == null ? new Node(map.getField(i, j + 1))
							: this.nodes[i][j + 1];
					Edge edge = new Edge(source, target, Orientation.SOUTH);
					source.getNeighbors().add(edge);
				}
				this.nodes[i][j] = source;
			}
		}
		/*
		 * Set our and other robots positions
		 */
		for (IPosition pos : map.getStartPositions()) {
			Node posNode = this.nodes[pos.getX()][pos.getY()];
			IField field = map.getField(pos.getX(), pos.getY());
			String id = field.getLockedBy();
			Robot robot = new Robot(id, posNode, pos.getOrientation());
			posNode.setRobot(robot);
		}
	}

	public void setStartPositions(IStage map) {

	}

	public Node[][] getNodes() {
		return nodes;
	}

	public void setNodes(Node[][] nodes) {
		this.nodes = nodes;
	}

	public Node getMyPos() {
		return myPos;
	}

	public void setMyPos(Node myPos) {
		this.myPos = myPos;
	}

	public int getDimX() {
		return dimX;
	}

	public void setDimX(int dimX) {
		this.dimX = dimX;
	}

	public int getDimY() {
		return dimY;
	}

	public void setDimY(int dimY) {
		this.dimY = dimY;
	}
}