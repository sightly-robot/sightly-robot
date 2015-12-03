package de.unihannover.swp2015.robots2.ai.graph;

import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

public class Edge {
	private Node source; // Ggf. redundant
	private Node target;
	private Orientation direction;

	public Edge(Node source, Node target, Orientation dir) {
		this.source = source;
		this.target = target;
		this.direction = dir;
	}

	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public Node getTarget() {
		return target;
	}

	public void setTarget(Node target) {
		this.target = target;
	}

	public Orientation getDirection() {
		return direction;
	}

	public void setDirection(Orientation direction) {
		this.direction = direction;
	}
}
