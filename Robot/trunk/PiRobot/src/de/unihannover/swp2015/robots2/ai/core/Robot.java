package de.unihannover.swp2015.robots2.ai.core;

import de.unihannover.swp2015.robots2.ai.graph.Node;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

public class Robot { 
	private String id;
	private Node position;
	//private int score;
	private Orientation orientation; // enum taken from data modell
	//private boolean myself;
	//private boolean isVirtual;

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

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}
}