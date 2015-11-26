package de.unihannover.swp2015.robots2.graph;

import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

public class Robot { // potentially only used later on
	private String id;
	private Node position;
	private int score;
	private Orientation orientation; // enum taken from data modell
	// private int/double speed maybe used later
	private boolean myself;
	private boolean isVirtual;

	public Robot(String id) {
		this.id = id;
	}

	public Robot(String id, Node position, Orientation orientation) {
		this(id);
		this.position = position;
		this.orientation = orientation;
	}

	public Node getPosition() {
		return this.position;
	}
}