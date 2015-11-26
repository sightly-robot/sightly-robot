package de.unihannover.swp2015.robots2.abstractrobot.automate;

import de.unihannover.swp2015.robots2.abstractrobot.Direction;

/**
 * Automate State Interface for standard automate. (Hard and Software)
 * @author Lenard Spiecker
 *
 */
public interface IState {
	
	public IState execute();
	
	public void start();
	
	public boolean isWait();
	
	public double getProgress();

	/**
	 * Should be overriden once or static.
	 * @param direction
	 * @return
	 */
	public IState getStateForDirection(Direction direction);
}
