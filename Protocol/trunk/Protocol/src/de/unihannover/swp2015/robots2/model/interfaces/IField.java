package de.unihannover.swp2015.robots2.model.interfaces;

/**
 * Read-only interface of a Labyrinth-Field
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IField extends IAbstractModel {

	/**
	 * 
	 * @version 0.1
	 * @author Patrick Kawczynski
	 */
	public enum State {
		FREE, LOCKED, OCCUPIED, OURS, LOCK_WAIT, RANDOM_WAIT
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
	 * @param orientation
	 * @return
	 */
	public boolean isWall(IPosition.Orientation orientation);

	/**
	 * 
	 * @return [0-10]
	 */
	public int getFood();

	/**
	 * 
	 * @return
	 */
	public State getState();

	/**
	 * Returns the id of the robot, who is locked the field.
	 * 
	 * @return robot-id
	 */
	public String getLockedBy();
	
	/**
	 * Only available on Server and GUI!
	 * 
	 * @return
	 */
	public int getGrowingRate();
}