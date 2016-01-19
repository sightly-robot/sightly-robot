package de.unihannover.swp2015.robots2.robot.abstractrobot.automate;

/**
 * For receiving events while 
 * @author Lenard Spiecker
 *
 */
public interface IStateEvent {

	/**
	 * When Hardware (or Software) -Robot detects an error while executing a state.
	 */
	public void iStateErrorOccured();
}
