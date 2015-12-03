package de.unihannover.swp2015.robots2.ai.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.unihannover.swp2015.robots2.ai.core.Robot;
import de.unihannover.swp2015.robots2.ai.exceptions.InvalidStageException;
import de.unihannover.swp2015.robots2.ai.exceptions.NoValidOrientationException;
import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;

public class AIGraph {

	private IStage stage;

	private Node[][] nodes;

	/*
	 * Should be set by the AI class!
	 */
	private Robot myself;
	
//	private IRobot iMyself;

	private int dimX;
	private int dimY;

	public AIGraph(IStage stage, Robot myself) throws InvalidStageException {
		this.stage = stage;
		this.loadFromStage(stage);
		
//		iMyself = iRobot;
		
		this.myself = myself;
		
//		iRobot.observe(new IModelObserver() {	
//			@Override
//			public void onModelUpdate(IEvent event) {
//				switch (event.getType()) {
//				case ROBOT_POSITION:
//					myself.setPosition(getNodes()[iMyself.getPosition().getX()][iMyself.getPosition().getY()]);
//					myself.setOrientation(iMyself.getPosition().getOrientation());
//					setRobotPosition(myself, iMyself.getPosition());
//					break;
//
//				default:
//					break;
//				}
//			}
//		});
	}

	/**
	 * Creates Graph of size x * y without Walls.
	 * 
	 * @param x
	 * @param y
	 */
	public AIGraph(int x, int y) {
		this.nodes = new Node[x][y];
		this.dimX = x;
		this.dimY = y;

		for (int i = 0; i < dimX; i++) {
			for (int j = 0; j < dimY; j++) {
				this.nodes[i][j] = new Node(i, j);
			}
		}

		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				if (i > 0) {
					Edge edge = new Edge(nodes[i][j], nodes[i - 1][j], Orientation.WEST);
					nodes[i][j].getNeighbors().add(edge);
				}
				if (i < this.dimX - 1) {
					Edge edge = new Edge(nodes[i][j], nodes[i + 1][j], Orientation.EAST);
					nodes[i][j].getNeighbors().add(edge);
				}
				if (j > 0) {
					Edge edge = new Edge(nodes[i][j], nodes[i][j - 1], Orientation.NORTH);
					nodes[i][j].getNeighbors().add(edge);
				}
				if (j < this.dimY - 1) {
					Edge edge = new Edge(nodes[i][j], nodes[i][j + 1], Orientation.SOUTH);
					nodes[i][j].getNeighbors().add(edge);
				}
			}
		}
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
				if (i < this.dimX - 1 && !field.isWall(Orientation.EAST)) {
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
				if (j < this.dimY - 1 && !field.isWall(Orientation.SOUTH)) {
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
		if (stage.getStartPositions() != null) {
//			for (IPosition pos : stage.getStartPositions()) {
//				Node posNode = this.nodes[pos.getX()][pos.getY()];
//				IField field = stage.getField(pos.getX(), pos.getY());
//				String id = field.getLockedBy();
//				Robot robot = new Robot(id, posNode, pos.getOrientation());
//				this.myself = robot;
//				posNode.setRobot(robot);
//			}
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
	 * @param newPosition
	 *            new position
	 */
	public void setRobotPosition(Robot robot, IPosition newPosition) {
		if(robot.getPosition() != null)
		{
			/*
			 * Delete robot from old position in graph
			 */
			robot.getPosition().setRobot(null);
		}
		if(newPosition.getX() != -1)
		{
			/*
			 * Set new orientation for robot Set robot on new position in graph
			 */
			robot.setOrientation(newPosition.getOrientation());
			this.nodes[newPosition.getX()][newPosition.getY()].setRobot(robot);
			robot.setPosition(this.nodes[newPosition.getX()][newPosition.getY()]);
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

	public Robot getMyself() {
		return myself;
	}

	public void setMyself(Robot myself) {
		this.myself = myself;
	}
}