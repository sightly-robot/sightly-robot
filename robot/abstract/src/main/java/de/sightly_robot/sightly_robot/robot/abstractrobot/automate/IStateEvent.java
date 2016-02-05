package de.sightly_robot.sightly_robot.robot.abstractrobot.automate;

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
