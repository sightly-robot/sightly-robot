package de.unihannover.swp2015.robots2.yaai.compute;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.yaai.model.Graph;
import de.unihannover.swp2015.robots2.yaai.model.Node;

/**
 * Path calculator to be used with YAAI.
 * 
 * Provides a method to calculate a weight for each Node of the Graph
 * (corresponding to each Field of the Stage). The calculation is based on the
 * current food on the field and the position of other robots.
 * 
 * @author Michael Thies
 */
public class WeightCalculator {
	private final Graph graph;
	private final IRobotController controller;

	private final int ROBOT_MOVEMENT_DEPTH = 8;

	private Logger log = LogManager.getLogger(this.getClass().getName());

	public WeightCalculator(Graph graph, IRobotController controller) {
		this.controller = controller;
		this.graph = graph;
	}

	/**
	 * Recalculate the weight for each node of the graph.
	 * 
	 * The calculation uses the current food on the field multiplied with the
	 * likelihood of any opposing robot reaching the field before us. (Assuming
	 * all opponents have random AIs.)
	 * 
	 * The temporary fields distance and otherRobotLikelyhood of the Nodes will
	 * are overwritten and used for the calculation.
	 * 
	 * @param startNode
	 */
	public void calculate(Node startNode) {
		int width = this.controller.getGame().getStage().getWidth();
		int height = this.controller.getGame().getStage().getHeight();

		// Normalize distance and likelihoods.
		log.trace("Deleting temporary data from Graph.");
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Node node = graph.getNode(x, y);
				node.setOtherRobotLikelihood(0);
				node.setDistance(Integer.MAX_VALUE);
			}
		}

		// calculate our distance to each Node
		log.trace("Calculating distances from current position to each field.");
		this.calcDistanceRecursive(startNode, 0);

		// calculate likelihood of any other robot (with random ai) reaching any
		// node before us
		log.trace("Calculating Likelihoods for other robots reaching fields before us...");
		for (IRobot r : this.controller.getGame().getRobots().values()) {
			if (r.isMyself())
				continue;
			log.trace("Calc Likelihood for robot {}...", r.getId());

			try {
				Node rNode = this.graph.getNode(r.getPosition().getX(), r
						.getPosition().getY());
				calcRobotLikelihood(rNode, 1, 0);
			} catch (IndexOutOfBoundsException e) {
				// Other robot has no valid position
			}
		}

		// calculate new weights
		log.trace("Calculating new node weights.");
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Node node = graph.getNode(x, y);
				node.setWeight(node.getField().getFood()
						* (1 - node.getOtherRobotLikelihood()));
			}
		}
	}

	/**
	 * Recursively (depth first search) calculate and save the minimal distance
	 * from a specific node to any other node. Given a distance offset from
	 * start.
	 * 
	 * Only edit (and explore edges of) nodes that have
	 * 
	 * @param node
	 *            The node to search at
	 * @param depth
	 *            The distance offset/current recursion depth
	 */
	private void calcDistanceRecursive(Node node, int depth) {
		if (node.getDistance() > depth) {
			node.setDistance(depth);
			for (Node n : node.getEdges()) {
				this.calcDistanceRecursive(n, depth + 1);
			}
		}
	}

	/**
	 * Recursively explore all nodes around a given node and change the
	 * otherRobotLikelihood according to the given likelihood and the number of
	 * possibilities.
	 * 
	 * Stop recursion if the depth reaches ROBOT_MOVEMENT_DEPTH.
	 */
	private void calcRobotLikelihood(Node node, float likelihood, int depth) {
		if (depth > ROBOT_MOVEMENT_DEPTH)
			return;

		if (depth <= node.getDistance()) {
			float newLikelihood = 1 - ((1 - likelihood) * (1 - node
					.getOtherRobotLikelihood()));
			node.setOtherRobotLikelihood(newLikelihood);
		}

		for (Node n : node.getEdges()) {
			this.calcRobotLikelihood(n, likelihood / node.getEdges().size(),
					depth + 1);
		}
	}
}
