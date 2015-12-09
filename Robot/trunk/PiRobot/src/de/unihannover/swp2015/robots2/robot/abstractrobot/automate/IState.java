package de.unihannover.swp2015.robots2.robot.abstractrobot.automate;

import de.unihannover.swp2015.robots2.robot.abstractrobot.Direction;

/**
 * Interface for the automate states of hardware robots as well as software
 * robots.
 * 
 * @author Lenard Spiecker
 */
public interface IState {

	/**
	 * Executes the current state.
	 * 
	 * @return the following state; maybe the same one
	 */
	public IState execute();

	/**
	 * Starts the current state.<br>
	 * This is meant to be the initialization of the state.
	 */
	public void start();

	/**
	 * Returns whether the current state is the wait state or not.
	 * 
	 * @return {@code true} if the current state is the wait state,
	 *         {@code false} otherwise
	 */
	public boolean isWait();

	/**
	 * Returns the state for driving into the specified direction.
	 * 
	 * @param direction
	 *            the direction to get the according state for
	 * @return the state for driving into the direction
	 */
	public IState getStateForDirection(Direction direction);
}
