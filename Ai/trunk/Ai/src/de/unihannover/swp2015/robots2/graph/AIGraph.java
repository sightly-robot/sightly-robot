package de.unihannover.swp2015.robots2.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.unihannover.swp2015.robots2.core.Robot;
import de.unihannover.swp2015.robots2.exceptions.InvalidStageException;
import de.unihannover.swp2015.robots2.exceptions.NoValidOrientationException;
import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;

public class AIGraph {

	private IStage stage;

	private Node[][] nodes;

	private Robot myself;

	private int dimX;
	private int dimY;

	public AIGraph(IStage stage) throws InvalidStageException {
		this.stage = stage;
		this.loadFromStage(stage);
	}

	public AIGraph(int x, int y) {
		this.nodes = new Node[x][y];
		this.dimX = x;
		this.dimY = y;
	}

	/**
	 * Loads map data into Graph object
	 * 
	 * @param stage
	 *            IStage data object
	 * @throws InvalidStageException
	 */
	public void loadFromStage(IStage stage) throws InvalidStageException {
		/*
		 * Handle exceptions for faulty stages
		 */
		if (stage == null) {
			throw new InvalidStageException("Stage is empty");
		} else if (stage.getWidth() < 1) {
			throw new InvalidStageException("Width of stage is too small");
		} else if (stage.getHeight() < 1) {
			throw new InvalidStageException("Height of stage is too small");
		}

		int width = stage.getWidth();
		int height = stage.getHeight();

		this.nodes = new Node[width][height];
		this.dimX = width;
		this.dimY = height;

		/*
		 * Init graph from stage data
		 */
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				IField field = stage.getField(i, j);
				/*
				 * Check if node already exists and eventually create new one
				 */
				Node source = this.nodes[i][j] == null ? new Node(field) : this.nodes[i][j];
				if (i > 0 && !field.isWall(Orientation.WEST)) {
					Node target = this.nodes[i - 1][j] == null ? new Node(stage.getField(i - 1, j))
							: this.nodes[i - 1][j];
					Edge edge = new Edge(source, target, Orientation.WEST);
					source.getNeighbors().add(edge);
				}
				if (i < this.dimX && !field.isWall(Orientation.EAST)) {
					Node target = this.nodes[i + 1][j] == null ? new Node(stage.getField(i + 1, j))
							: this.nodes[i + 1][j];
					Edge edge = new Edge(source, target, Orientation.EAST);
					source.getNeighbors().add(edge);
				}
				if (j > 0 && !field.isWall(Orientation.NORTH)) {
					Node target = this.nodes[i][j - 1] == null ? new Node(stage.getField(i, j - 1))
							: this.nodes[i][j - 1];
					Edge edge = new Edge(source, target, Orientation.NORTH);
					source.getNeighbors().add(edge);
				}
				if (j < this.dimY && !field.isWall(Orientation.SOUTH)) {
					Node target = this.nodes[i][j + 1] == null ? new Node(stage.getField(i, j + 1))
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
		for (IPosition pos : stage.getStartPositions()) {
			Node posNode = this.nodes[pos.getX()][pos.getY()];
			IField field = stage.getField(pos.getX(), pos.getY());
			String id = field.getLockedBy();
			Robot robot = new Robot(id, posNode, pos.getOrientation());
			posNode.setRobot(robot);
		}
	}

	// not returning backward in increment 1
	/**
	 * This method returns a random valid Orientation, which the robot is
	 * supposed to move in next.
	 * 
	 * @return Orientation, the robot should move in next
	 */
	// TODO: Exception if there is no valid Orientation
	public Orientation getRandomOrientation() throws NoValidOrientationException {
		List<Orientation> available = new ArrayList<Orientation>();
		Orientation curr = this.myself.getOrientation();
		Node myPos = getMyPosition();

		switch (curr) {
		case NORTH:
			if (!myPos.isWall(Orientation.WEST))
				available.add(Orientation.WEST);
			if (!myPos.isWall(Orientation.NORTH))
				available.add(Orientation.NORTH);
			if (!myPos.isWall(Orientation.EAST))
				available.add(Orientation.EAST);
			break;
		case EAST:
			if (!myPos.isWall(Orientation.NORTH))
				available.add(Orientation.NORTH);
			if (!myPos.isWall(Orientation.EAST))
				available.add(Orientation.EAST);
			if (!myPos.isWall(Orientation.SOUTH))
				available.add(Orientation.SOUTH);
			break;
		case SOUTH:
			if (!myPos.isWall(Orientation.EAST))
				available.add(Orientation.EAST);
			if (!myPos.isWall(Orientation.SOUTH))
				available.add(Orientation.SOUTH);
			if (!myPos.isWall(Orientation.WEST))
				available.add(Orientation.WEST);
			break;
		case WEST:
			if (!myPos.isWall(Orientation.SOUTH))
				available.add(Orientation.SOUTH);
			if (!myPos.isWall(Orientation.WEST))
				available.add(Orientation.WEST);
			if (!myPos.isWall(Orientation.NORTH))
				available.add(Orientation.NORTH);
			break;
		}

		Collections.shuffle(available);
		return available.get(0);
	}

	/**
	 * Sets the new position of a robot in the graph
	 * 
	 * @param robot
	 *            Robot to be set
	 * @param pos
	 *            new position
	 */
	public void setRobotPosition(Robot robot, IPosition pos) {
		int oldX = robot.getPosition().getX();
		int oldY = robot.getPosition().getY();

		/*
		 * Delete robot from old position in graph
		 */
		this.nodes[oldX][oldY].setRobot(null);
		/*
		 * Set new orientation for robot Set robot on new position in graph
		 */
		robot.setOrientation(pos.getOrientation());
		this.nodes[pos.getX()][pos.getY()].setRobot(robot);
		robot.setPosition(this.nodes[pos.getX()][pos.getY()]);
		if(robot.isMyself()) {
			this.myself = robot;
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

	public IStage getStage() {
		return stage;
	}

	public void setStage(IStage stage) {
		this.stage = stage;
	}
}