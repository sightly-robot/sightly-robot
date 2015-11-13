package de.unihannover.swp2015.robots2.model.interfaces;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IPosition {

	/**
	 * Contains the four cardinal points as the possible orientations in the
	 * labyrinth
	 * 
	 * @version 0.1
	 * @author Patrick Kawczynski
	 */
	public enum Orientation {
		NORTH, EAST, SOUTH, WEST
	}

	/**
	 * 
	 * @return
	 */
	public int getX();

	/**
	 * 
	 * @return
	 */
	public int getY();

	/**
	 * 
	 * @return
	 */
	public Orientation getOrientation();

	/**
	 * 
	 * @return
	 */
	public int getProgress();
}
