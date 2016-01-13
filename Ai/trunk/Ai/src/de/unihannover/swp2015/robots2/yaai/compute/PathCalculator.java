package de.unihannover.swp2015.robots2.yaai.compute;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.yaai.model.Graph;
import de.unihannover.swp2015.robots2.yaai.model.Node;

public class PathCalculator {
	private final Graph graph;
	private final IRobotController controller;

	private final int PATH_LENGTH = 10;

	private Logger log = LogManager.getLogger(this.getClass().getName());
	private Random random = new Random();

	public PathCalculator(Graph graph, IRobotController controller) {
		this.controller = controller;
		this.graph = graph;
	}

	/**
	 * Calculate the best path of the given length.
	 * 
	 * @return The first node of the path.
	 */
	public Node calculate(Node currentNode) {
		log.trace(
				"Calculating best Path with length {} beginning at Field {}-{}...",
				PATH_LENGTH, currentNode.getField().getX(), currentNode
						.getField().getY());

		if (currentNode.getEdges().size() == 0) {
			log.trace("Current Position has no following fields.");
			return currentNode;
		}

		Set<Node> visitedNodes = new HashSet<>();
		visitedNodes.add(currentNode);
		double bestScore = 0;
		Node nextNode = currentNode.getEdges().get(
				random.nextInt(currentNode.getEdges().size()));
		for (Node n : currentNode.getEdges()) {
			double score = this.searchBestPath(n, 1, visitedNodes);
			if (score > bestScore) {
				bestScore = score;
				nextNode = n;
			}
		}

		log.trace("Best Path has score {} and starts at {}-{}", bestScore,
				nextNode.getField().getX(), nextNode.getField().getY());

		return nextNode;
	}

	/**
	 * Recursively (depth first search) search for the best path (measured by
	 * sum of weights of nodes) with length PATH_LENGTH - depth.
	 * 
	 * An already visited node will be weighted with 0.
	 * 
	 * @param node
	 *            Node to begin search
	 * @param depth
	 *            current depth of search
	 * @param visitedNodes
	 *            A set of all nodes visited up to here
	 * @return Score (sum of weights) of the best path found from here
	 */
	private double searchBestPath(Node node, int depth, Set<Node> visitedNodes) {
		if (depth > PATH_LENGTH)
			return 0;

		float thisWeight = visitedNodes.contains(node) ? 0 : node.getWeight();

		visitedNodes.add(node);
		double bestScore = 0;
		for (Node n : node.getEdges()) {
			double score = this.searchBestPath(n, depth + 1, visitedNodes);
			if (score > bestScore)
				bestScore = score;
		}
		visitedNodes.remove(node);
		return bestScore + thisWeight * Math.sqrt(depth);
	}

}
