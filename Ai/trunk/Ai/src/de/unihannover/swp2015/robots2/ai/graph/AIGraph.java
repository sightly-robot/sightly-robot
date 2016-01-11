package de.unihannover.swp2015.robots2.ai.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import de.unihannover.swp2015.robots2.ai.core.Robot;
import de.unihannover.swp2015.robots2.ai.exceptions.InvalidPathException;
import de.unihannover.swp2015.robots2.ai.exceptions.InvalidStageException;
import de.unihannover.swp2015.robots2.ai.exceptions.NoValidOrientationException;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
//import testpackage.Edge;
//import testpackage.Node;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;

public class AIGraph extends Thread implements Runnable {

	private IStage stage;

	private Node[][] nodes;

	/*
	 * Should be set by the AI class!
	 */
	private Robot myself;
	/*
	 * To identify other robots
	 * String is ID of the robot
	 * TODO implement
	 */
	private HashMap<String, Robot> robots;
	
	private int dimX;
	private int dimY;

	public AIGraph(IStage stage, Robot myself) throws InvalidStageException {
		super();
		
		this.stage = stage;
		this.loadFromStage(stage);
		
		this.myself = myself;
		
		this.start();
	}

	/**
	 * Creates Graph of size x * y without Walls.
	 * 
	 * @param x
	 * @param y
	 */
	public AIGraph(int x, int y) {
		super();
		
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

	@Override
	public void run() {
		while(true) { //TODO something better than while true?
			try{
				sleep(10); //TODO this is supposed to keep the thread alive and waiting for method calls...
			} 
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
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
					/*Node target = this.nodes[i - 1][j] == null ? new Node(stage.getField(i - 1, j))
							: this.nodes[i - 1][j];*/
					Node target;
					if(this.nodes[i - 1][j] == null) {
						target = new Node(stage.getField(i - 1, j));
						nodes[i - 1][j] = target;
					} else {
						target = this.nodes[i - 1][j];
					}
					Edge edge = new Edge(source, target, Orientation.WEST);
					source.getNeighbors().add(edge);
				}
				if (i < this.dimX - 1 && !field.isWall(Orientation.EAST)) {
					/*Node target = this.nodes[i + 1][j] == null ? new Node(stage.getField(i + 1, j))
							: this.nodes[i + 1][j];*/
					Node target;
					if(this.nodes[i + 1][j] == null) {
						target = new Node(stage.getField(i + 1, j));
						nodes[i + 1][j] = target;
					} else {
						target = this.nodes[i + 1][j];
					}
					Edge edge = new Edge(source, target, Orientation.EAST);
					source.getNeighbors().add(edge);
				}
				if (j > 0 && !field.isWall(Orientation.NORTH)) {
					/*Node target = this.nodes[i][j - 1] == null ? new Node(stage.getField(i, j - 1))
							: this.nodes[i][j - 1];*/
					Node target;
					if(this.nodes[i][j - 1] == null) {
						target = new Node(stage.getField(i, j - 1));
						nodes[i][j - 1] = target;
					} else {
						target = this.nodes[i][j - 1];
					}
					Edge edge = new Edge(source, target, Orientation.NORTH);
					source.getNeighbors().add(edge);
				}
				if (j < this.dimY - 1 && !field.isWall(Orientation.SOUTH)) {
					/*Node target = this.nodes[i][j + 1] == null ? new Node(stage.getField(i, j + 1))
							: this.nodes[i][j + 1];*/
					Node target;
					if(this.nodes[i][j + 1] == null) {
						target = new Node(stage.getField(i, j + 1));
						nodes[i][j + 1] = target;
					} else {
						target = this.nodes[i][j + 1];
					}
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
		Node myPos = getMyPosition();
		int x = myPos.getX();
		int y = myPos.getY();
		
		if (!myPos.isWall(Orientation.NORTH)) {
			available.add(Orientation.NORTH);
		}
		if (!myPos.isWall(Orientation.EAST)) {
			available.add(Orientation.EAST);
		}			
		if (!myPos.isWall(Orientation.SOUTH)) {
			available.add(Orientation.SOUTH);
		}			
		if (!myPos.isWall(Orientation.WEST)) {
			available.add(Orientation.WEST);
		}
			
		Collections.shuffle(available);
		return available.get(0);
	}
	
	/**
	 * Finds shortest path to given Node using breadth first search. Used to determine next Orientation, the robot 
	 * should drive in.
	 * @param target Node, the robot is supposed to drive to.
	 * @return
	 */
	public List<Node> getBFSPath(Node target) {
		System.out.println("target: [" + target.getX() + "," + target.getY() + "]");

		Map<Node, Node> pred = new HashMap<Node, Node>();
		Set<Node> visited = new HashSet<Node>();
		Queue<Node> q = new LinkedList<Node>();
		Node curr = myself.getPosition();
		q.add(curr);
		visited.add(curr);

		while(curr != target) {
			curr = q.remove();
			
			Set<Node> neighbors = new HashSet<Node>();
			for(Edge edge : curr.getNeighbors()) {
				neighbors.add(edge.getTarget());
			}
			for(Node node : neighbors) {
				if(!visited.contains(node)) {
					pred.put(node, curr);
					visited.add(node);
					q.add(node);
				}
			}
		}
		LinkedList<Node> path = new LinkedList<Node>();
		Node tmp = target;
		path.addFirst(target);
		while(tmp != myself.getPosition()) {
			path.addFirst(pred.get(tmp));
			tmp = pred.get(tmp);
		}
		return path;
	}

	/**
	 * Gets next Orientation to drive in from given path. Used, to find Orientation from path determined by 
	 * getBFSPath().
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public Orientation getOrientationFromPath(List<Node> path) throws InvalidPathException {
		
		for(Edge edge : myself.getPosition().getNeighbors()) {
			if(edge.getTarget() == path.get(1)) {
				return edge.getDirection();
			}
		}
		throw new InvalidPathException();
	}
	
	/**
	 * Finds node with highest amount of resources in given range (at this point not taking into account walls or other obstacles).
	 * Used to find target node to drive towards.
	 * @param range
	 * @return
	 */
	public Node findBestNode(int range) {
		int x = myself.getPosition().getX();
		int y = myself.getPosition().getY();
		//Set<Node> set = new HashSet<Node>();
		Node curr = this.nodes[Math.max(x - range, 0)][Math.max(y - range, 0)];
		for(int i = Math.max(x - range, 0); i < Math.min(x + range + 1, this.nodes.length); i++) {
			for(int j = Math.max(y - range, 0); j < Math.min(y + range + 1, this.nodes[0].length); j++) {
				if((i != x || j != y) 
						&& this.nodes[i][j].getRessourceValue() > curr.getRessourceValue()) {
					curr = this.nodes[i][j];
				}
			}
		}
		return curr;
	}
	/**
	 * Sets food value on a specific Field.
	 * @param x 
	 * @param y
	 * @param food
	 */
	public void setFood(int x, int y, int food) {
		this.nodes[x][y].setRessourceValue(food);
	}

	/**
	 * Sets the new position of a robot in the graph and also sets it for the robot itself
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

	public HashMap<String, Robot> getRobots() {
		return robots;
	}

	public void setRobots(HashMap<String, Robot> robots) {
		this.robots = robots;
	}
}