package de.unihannover.swp2015.robots2.ai.core;

import de.unihannover.swp2015.robots2.ai.graph.Node;
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

	public void setPosition(Node node) {
		this.position = node;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isMyself() {
		return myself;
	}

	public void setMyself(boolean myself) {
		this.myself = myself;
	}

	public boolean isVirtual() {
		return isVirtual;
	}

	public void setVirtual(boolean isVirtual) {
		this.isVirtual = isVirtual;
	}

	public Orientation getOrientation() {
		return this.orientation;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}
}