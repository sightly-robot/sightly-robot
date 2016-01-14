package de.unihannover.swp2015.robots2.yaai.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;
import de.unihannover.swp2015.robots2.yaai.YetAnotherAi;

/**
 * A data structure that stores additional information for each Field of the
 * given Stage to be used by {@link YetAnotherAi}. The number of nodes and their
 * links updated from the original Stage triggered by its events.
 * 
 * This data model also stores a graph structure (a list of following nodes for
 * each node), but that's just redundant to the wall data of the Stage although
 * nicer to iterate in path calculation.
 * 
 * @author Michael Thies
 */
public class Graph implements IModelObserver {
	private IStage stage;
	private List<List<Node>> nodes;
	private static final Logger LOGGER = LogManager.getLogger(Graph.class
			.getName());
	private int width;
	private int height;

	public Graph(IStage stage) {
		this.stage = stage;
		stage.observe(this);
		this.nodes = new ArrayList<>();
		this.resizeGraph();
		this.rebuildEdges();
	}

	@Override
	public void onModelUpdate(IEvent event) {
		switch (event.getType()) {
		case STAGE_SIZE:
			LOGGER.debug("Stage was resized. Resizing Graph.");
			this.resizeGraph();
			break;

		case STAGE_WALL:
			LOGGER.debug("Walls of Map were updated. Updating Graph edges.");
			this.rebuildEdges();
			break;

		default:
			break;
		}
	}

	/**
	 * Add or remove Nodes from this Graph to fit to new stage sizes.
	 */
	private void resizeGraph() {
		int height = this.stage.getHeight();
		int width = this.stage.getWidth();

		// Delete obsolete rows
		if (height < this.height) {
			for (int y = this.height - 1; y > height - 1; y--) {
				this.nodes.remove(y);
			}
			// ... or add new rows
		} else if (height > this.height) {
			for (int y = this.height; y < height; y++) {
				ArrayList<Node> newRow = new ArrayList<>();
				for (int x = 0; x < width; x++) {
					newRow.add(new Node(this.stage.getField(x, y)));
				}
				this.nodes.add(newRow);
			}
		}

		// Resize all rows still existing from before
		for (int y = 0; y < Math.min(this.height, height); y++) {
			if (width < this.width) {
				for (int x = this.width - 1; x > width - 1; x--) {
					this.nodes.get(y).remove(x);
				}
			} else if (width > this.width) {
				for (int x = this.width; x < width; x++) {
					this.nodes.get(y).add(new Node(this.stage.getField(x, y)));
				}
			}
		}

		this.width = width;
		this.height = height;
	}

	/**
	 * Rebuild all edges if walls of Stage changed.
	 */
	private void rebuildEdges() {
		if (this.width != this.stage.getWidth()
				|| this.height != this.stage.getHeight())
			return;

		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				Node n = this.getNode(x, y);
				n.getEdges().clear();
				for (Orientation o : Orientation.values()) {
					if (!n.getField().isWall(o)) {
						Node nextNode = null;
						switch (o) {
						case EAST:
							nextNode = this.getNode(x + 1, y);
							break;
						case NORTH:
							nextNode = this.getNode(x, y - 1);
							break;
						case SOUTH:
							nextNode = this.getNode(x, y + 1);
							break;
						case WEST:
							nextNode = this.getNode(x - 1, y);
							break;
						}
						n.getEdges().add(nextNode);
					}
				}
			}
		}

	}

	/**
	 * Get the Node of this Graph corresponding to the Field at Position x,y.
	 * 
	 * @param x
	 *            X coordinate of the field/node
	 * @param y
	 *            Y coordinate of the field/node
	 * @return The node corresponding to the field at the given position
	 */
	public Node getNode(int x, int y) {
		return this.nodes.get(y).get(x);
	}

}
