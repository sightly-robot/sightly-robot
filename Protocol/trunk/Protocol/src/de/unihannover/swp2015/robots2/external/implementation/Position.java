package de.unihannover.swp2015.robots2.external.implementation;

import de.unihannover.swp2015.robots2.external.interfaces.IPosition;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class Position implements IPosition {

	private int x;
	private int y;
	private Orientation orientation;
	private int progress;
	
	/**
	 * Contains the four cardinal points as the possible orientations in the labyrinth
	 * 
	 * @version 0.1
	 * @author Patrick Kawczynski
	 */
	public enum Orientation {
		NORTH, EAST, SOUTH, WEST
	}
	
	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}

	@Override
	public Orientation getOrientation() {
		return this.orientation;
	}

	@Override
	public int getProgress() {
		return this.progress;
	}

}
