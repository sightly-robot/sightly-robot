package de.unihannover.swp2015.robots2.aiinterface;

import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

/**
 * An AiEventObserver is a class that needs to react to events posted by the AI.
 * 
 * @author Lenard Spiecker
 */
public interface AiEventObserver {

	/**
	 * Notifies a robot about the next position to go to.
	 * 
	 * @param orientation
	 *            the orientation of the next position
	 * @return {@code true} if the robot is capable of going to the next
	 *         position, {@code false} otherwise
	 */
	public boolean nextOrientationEvent(Orientation orientation);

	/**
	 * Notifies a robot about the next but one position to go to.
	 * 
	 * @param orientation
	 *            the orientation of the next but one position
	 */
	public void nextButOneOrientationEvent(Orientation orientation);
}
