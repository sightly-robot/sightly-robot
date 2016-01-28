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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.ai.core.Robot;
import de.unihannover.swp2015.robots2.ai.exceptions.InvalidStageException;
import de.unihannover.swp2015.robots2.ai.exceptions.NoValidOrientationException;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;

public class AIGraph extends Thread implements Runnable {

	private static Logger logger = LogManager.getLogger(AIGraph.class.getName());

	private IStage stage;	
	private Node[][] nodes;

	private boolean calculated = false;
	
	/*
	 * Should be set by the AI class!
	 */
	private Robot myself;
	private Node myNextNode; // position this robot will be on next
	private Orientation nextOrientation; // next Orientation, robot should drive
											// in
	/*
	 * To identify other robots String is ID of the robot TODO implement
	 */
	private HashMap<String, Robot> robots;

	private int dimX;
	private int dimY;
	
	/**
	 * Constructor for AIGraph. Creates an AIGraph object with information from 
	 * given stage object and with given Robot object resembling "myself": 
	 * The robot, from whichs perspective the graph is generated.
	 * 
	 * @param stage The stage which the graph should resemble.
	 * @param myself The robot object, from whichs perspective the graph is generated.
	 * @throws InvalidStageException Exception, which is thrown if the stage is invalid.
	 */
	public AIGraph(IStage stage, Robot myself) throws InvalidStageException {
		super();
		logger.debug("Initializing AIGraph from stage");
		this.stage = stage;
		this.loadFromStage(stage);

		this.myself = myself;

		this.start();
	}

	/**
	 * Creates empty Graph of size x * y without Walls.
	 * 
	 * @param x The width of the created graph.
	 * @param y The height of the created graph.
	 */
	public AIGraph(int x, int y) {
		super();
		logger.debug("Initializing AIGraph from ({},{})", x, y);
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
		while (true) { // TODO something better than while true?
			try {
				sleep(10); // TODO this is supposed to keep the thread alive and
							// waiting for method calls...
			} catch (InterruptedException e) {
				logger.error("AIGraph: thread was interrupted", e);
			}
		}
	}

	/**
	 * Loads map data into Graph object.
	 * 
	 * @param stage Stage which the graph should resemble.
	 * @throws InvalidStageException Exception which is thrown if the stage is invalid.
	 */
	public void loadFromStage(IStage stage) throws InvalidStageException {
		logger.debug("Loading AIGraph from stage");
		/*
		 * Handle exceptions for faulty stages
		 */
		if (stage == null) {
			logger.error("loadFromStage: stage is empty!");
			throw new InvalidStageException("Stage is empty");
		} else if (stage.getWidth() < 1) {
			logger.error("loadFromStage: width is too small!");
			throw new InvalidStageException("Width of stage is too small");
		} else if (stage.getHeight() < 1) {
			logger.error("loadFromStage: height is too small!");
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
				logger.trace("Setting node for field ({},{})", i, j);
				/*
				 * Check if node already exists and eventually create new one
				 */
				Node source = this.nodes[i][j] == null ? new Node(field) : this.nodes[i][j];
				if (i > 0 && !field.isWall(Orientation.WEST)) {
					Node target;
					if (this.nodes[i - 1][j] == null) {
						target = new Node(stage.getField(i - 1, j));
						nodes[i - 1][j] = target;
					} else {
						target = this.nodes[i - 1][j];
					}
					Edge edge = new Edge(source, target, Orientation.WEST);
					source.getNeighbors().add(edge);
				}
				if (i < this.dimX - 1 && !field.isWall(Orientation.EAST)) {
					Node target;
					if (this.nodes[i + 1][j] == null) {
						target = new Node(stage.getField(i + 1, j));
						nodes[i + 1][j] = target;
					} else {
						target = this.nodes[i + 1][j];
					}
					Edge edge = new Edge(source, target, Orientation.EAST);
					source.getNeighbors().add(edge);
				}
				if (j > 0 && !field.isWall(Orientation.NORTH)) {
					Node target;
					if (this.nodes[i][j - 1] == null) {
						target = new Node(stage.getField(i, j - 1));
						nodes[i][j - 1] = target;
					} else {
						target = this.nodes[i][j - 1];
					}
					Edge edge = new Edge(source, target, Orientation.NORTH);
					source.getNeighbors().add(edge);
				}
				if (j < this.dimY - 1 && !field.isWall(Orientation.SOUTH)) {
					Node target;
					if (this.nodes[i][j + 1] == null) {
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
	}

	/**
	 * This method returns a random valid Orientation, which the robot is
	 * supposed to move in next.
	 * 
	 * @return Orientation, the robot should move in next
	 */
	// TODO: Exception if there is no valid Orientation
	public Orientation getRandomOrientation() {
		logger.trace("Calling getRandomOrientation");
		List<Orientation> available = new ArrayList<Orientation>();
		Node myPos = this.myNextNode;
		
		while(available.isEmpty()) {
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
			logger.trace("Returning orientation {}", available.get(0));
		}
		
		return available.get(0);
	}

	/**
	 * Finds shortest path to given Node using breadth first search. Used to
	 * determine next Orientation, the robot should drive in.
	 * 
	 * @param target
	 *            Node, the robot is supposed to drive to.
	 * @return Shortest path to target Node in form of a List of nodes.
	 */
	public List<Node> getBFSPath(Node target) {
		logger.trace("Calling getBFSPath");

		Map<Node, Node> pred = new HashMap<Node, Node>();
		Set<Node> visited = new HashSet<Node>();
		Queue<Node> q = new LinkedList<Node>();
		Node curr = this.myNextNode;
		q.add(curr);
		visited.add(curr);

		while (curr != target) {
			curr = q.remove();

			Set<Node> neighbors = new HashSet<Node>();
			for (Edge edge : curr.getNeighbors()) {
				neighbors.add(edge.getTarget());
			}
			for (Node node : neighbors) {
				if (!visited.contains(node)) {
					pred.put(node, curr);
					visited.add(node);
					q.add(node);
				}
			}
		}
		LinkedList<Node> path = new LinkedList<Node>();
		Node tmp = target;
		path.addFirst(target);
		while (tmp != this.myNextNode) {
			path.addFirst(pred.get(tmp));
			tmp = pred.get(tmp);
		}
		return path;
	}

	/**
	 * Gets next Orientation to drive in from given path. Used, to find
	 * Orientation from path determined by getBFSPath().
	 * 
	 * @param path The path from which the next Orientation to drive in should
	 * 			be determined.
	 * @return The Orientation the robot should drive in next.
	 * @throws Exception //TODO
	 */
	public Orientation getOrientationFromPath(List<Node> path) throws Exception {
		logger.trace("Calling getOrientationFromPath");

		for (Edge edge : this.myNextNode.getNeighbors()) {
			if (edge.getTarget() == path.get(1)) {
	
					return edge.getDirection();
				//else {
//					while(true) {
//						logger.debug("in while true");
//						for(Edge tmpEdge : myNextNode.getNeighbors()) {
//							if(tmpEdge.getTarget().getRobot() == null && tmpEdge.getDirection() == getRandomOrientation()) {
//								return tmpEdge.getDirection();
//							}
//						}
//						sleep(10);
//					}
//				}
			}
		}
		logger.error("getOrientationFromPath: No neighbors for current node found!");
		throw new Exception();
	}

	/**
	 * Finds node with highest amount of resources in given range (at this point
	 * not taking into account walls or other obstacles). Used to find target
	 * node to drive towards.
	 * 
	 * @param range The range in which the robot should look for fields.
	 * @return The best Node in range. 
	 */
	public Node findBestNode(int range) { // TODO better way to determine best
											// node via bfs
		logger.trace("Calling findBestNode");
		int x = this.myNextNode.getX();
		int y = this.myNextNode.getY();
		// Set<Node> set = new HashSet<Node>();
		Node curr;
		if (x != Math.max(x - range, 0) || y != Math.max(y - range, 0)) {
			curr = this.nodes[Math.max(x - range, 0)][Math.max(y - range, 0)];
		} else {
			curr = this.nodes[Math.max(x - range, 1)][Math.max(y - range, 0)]; // TODO
																				// make
																				// sure
																				// no
																				// inaccessible
																				// field
																				// is
																				// selected
		}
		for (int i = Math.max(x - range, 0); i < Math.min(x + range + 1, this.nodes.length); i++) {
			for (int j = Math.max(y - range, 0); j < Math.min(y + range + 1, this.nodes[0].length); j++) {
				if ((i != x || j != y) && this.nodes[i][j].getRessourceValue() > curr.getRessourceValue()) {
					curr = this.nodes[i][j];
				}
			}
		}
		return curr;
	}
	
	/**
	 * Helper class that stores two values. Used to return Node and Orientation in 
	 * findBestNodeBFS().
	 *
	 * @param <X> First value of the Tuple.
	 * @param <Y> Second value of the Tuple.
	 */
	private class Tuple<X, Y> {
		public final X x;
		public final Y y;

		public Tuple(X x, Y y) {
			this.x = x;
			this.y = y;
		}
	}

	/**
	 * Finds node with highest amount of resources in given distance via breadth
	 * first search. Used to find target node to drive towards.
	 * 
	 * @param range The range in which the robot should look for the best (currently: highest
	 * amount of food) node.
	 * @return The best Node (currently: highest amount of food) in given range.
	 */
	public Node findBestNodeBFS(int range) {
		logger.trace("Calling findBestNodeBFS");
		Set<Node> visited = new HashSet<Node>();
		Queue<Tuple<Node, Integer>> q = new LinkedList<Tuple<Node, Integer>>();
		Tuple<Node, Integer> curr = new Tuple(this.myNextNode, 0);
		q.add(curr);
		visited.add(curr.x);

		// initialize with random neighbor
		List<Edge> myNeighbors = this.myNextNode.getNeighbors();
		Node best = myNeighbors.get((int) (Math.random() * myNeighbors.size())).getTarget();

		while (curr.y <= range) { // TODO check for empty queue
			curr = q.remove();

			Set<Node> neighbors = new HashSet<Node>();
			for (Edge edge : curr.x.getNeighbors()) {
				neighbors.add(edge.getTarget());
			}
			for (Node node : neighbors) {
				if (!visited.contains(node)) {
					if (node.getRessourceValue() > best.getRessourceValue()) {
						best = node;
					}
					visited.add(node);
					q.add(new Tuple(node, curr.y + 1));
				}
			}
		}
		return best;
	}

	/**
	 * Sets food value on a specific Field.
	 * 
	 * @param x The x coordinate of the field.
	 * @param y The y coordinate of the field.
	 * @param food
	 */
	public void setFood(int x, int y, int food) {
		logger.trace("Setting {} food on ({},{})", food, x, y);
		this.nodes[x][y].setRessourceValue(food);
	}

	/**
	 * Sets the new position of a robot in the graph and also sets it for the
	 * robot itself.
	 * 
	 * @param robot Robot whichs position should be set.
	 * @param newPosition Position, the robot should be put on.
	 */
	public void setRobotPosition(Robot robot, IPosition newPosition) {
		logger.trace("Calling setRobotPosition");
		if (robot.getPosition() != null) {
			/*
			 * Delete robot from old position in graph
			 */
			logger.debug("Deleting robot from old position");
			Node currPos = robot.getPosition();
			nodes[currPos.getX()][currPos.getY()].removeRobot();
		}
		if (newPosition.getX() != -1) {
			/*
			 * Set new orientation for robot Set robot on new position in graph
			 */
			logger.debug("Setting new orientation and position for robot");
			robot.setOrientation(newPosition.getOrientation());
			this.nodes[newPosition.getX()][newPosition.getY()].setRobot(robot);
			robot.setPosition(this.nodes[newPosition.getX()][newPosition.getY()]);
		}
	}
	
	/**
	 * Returns the robots stored in a hash map with their ids as values.
	 * 
	 * @return The current HashMap storing the robots.
	 */
	public HashMap<String, Robot> getRobots() {
		return robots;
	}
	
	/**
	 * Sets myNextNode, the position the robot is driving to currently. Then calculates the
	 * next Orientation, the robot should drive in, when he reaches his the field he is 
	 * driving towards. Called, when the robot starts driving. 
	 * 
	 * @param x The x coordinate of the robots next Position.
	 * @param y The y coordinate of the robots next Position.
	 */
	public void calculateNextOrientation(int x, int y) {
		this.calculated = false;
		this.myNextNode = this.nodes[x][y];
		logger.debug("My next node is ({},{})", x, y);
		try {
			Node bestNode = findBestNodeBFS(3);
			logger.debug("Next target: ({},{})", bestNode.getX(), bestNode.getY());
			this.nextOrientation = getOrientationFromPath(getBFSPath(bestNode));
			this.calculated = true;
			logger.debug("Next calculated orientation is {}", this.nextOrientation.name());
		} catch (Exception e) {
			logger.error("calculateNextOrientation: no valid orientation was found!", e);
		}
	}
	
	/**
	 * Returns the calculated next Orientation, the robot should drive in.
	 * 
	 * @return The calculated nextOrientation, the robot should drive in.
	 */
	public Orientation getNextOrientation() {
		try {
			while(!this.calculated) {
				sleep(10);
			}
			return this.nextOrientation;
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
		return this.getRandomOrientation();
	}
}