package de.unihannover.swp2015.robots2.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.unihannover.swp2015.robots2.core.Robot;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;

public class AIGraph {

	private Node[][] nodes;

	private Robot myself;

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
	
	//not returning backward in increment 1
	/**
	 * This method returns a random valid Orientation, which the robot is supposed to 
	 * move in next.
	 * 
	 * @return	Orientation, the robot should move in next
	 */
	//TODO: Exception if there is no valid Orientation
	public Orientation getRandomOrientation() {
		List<Orientation> available = new ArrayList<Orientation>();
		Orientation curr = this.myself.getOrientation();
		Node myPos = getMyPosition();
		
		switch(curr) {
			case NORTH:	if(!myPos.isWall(Orientation.WEST))
							available.add(Orientation.WEST);
						if(!myPos.isWall(Orientation.NORTH))
							available.add(Orientation.NORTH);
						if(!myPos.isWall(Orientation.EAST))
							available.add(Orientation.EAST);
						break;
			case EAST:	if(!myPos.isWall(Orientation.NORTH))
							available.add(Orientation.NORTH);
						if(!myPos.isWall(Orientation.EAST))
							available.add(Orientation.EAST);
						if(!myPos.isWall(Orientation.SOUTH))
							available.add(Orientation.SOUTH);
						break;
			case SOUTH:	if(!myPos.isWall(Orientation.EAST))
							available.add(Orientation.EAST);
						if(!myPos.isWall(Orientation.SOUTH))
							available.add(Orientation.SOUTH);
						if(!myPos.isWall(Orientation.WEST))
							available.add(Orientation.WEST);
						break;
			case WEST:	if(!myPos.isWall(Orientation.SOUTH))
							available.add(Orientation.SOUTH);
						if(!myPos.isWall(Orientation.WEST))
							available.add(Orientation.WEST);
						if(!myPos.isWall(Orientation.NORTH))
							available.add(Orientation.NORTH);
						break;
		}
		
		Collections.shuffle(available);
		return available.get(0);
	}
	
	public void setStartPositions(IStage map) {

	}

	public Node[][] getNodes() {
		return nodes;
	}

	public void setNodes(Node[][] nodes) {
		this.nodes = nodes;
	}

	public Node getMyPosition() {
		return this.myself.getPosition();
	}

	public void setMyPosition(Node myPosition) {
		this.myself.setPosition(myPosition);
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