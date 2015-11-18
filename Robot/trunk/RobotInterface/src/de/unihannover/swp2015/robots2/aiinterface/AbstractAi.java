package de.unihannover.swp2015.robots2.aiinterface;

import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

/**
 * AbstractAi is an abstract class and standard as an interface for the hardware and software robot part.
 * @author Lenard Spiecker
 */
public abstract class AbstractAi {

	AiEventObserver aiEventObserver;
	
	public AbstractAi(IGame iGame) {
		
	}
	
	/**
	 * To set calculated relative move durations. The Ai can use them to find the fastest way.
	 * @param foreward
	 * @param sideward
	 * @param backward
	 */
	public abstract void setRelativeSpeed(double foreward, double sideward, double backward);
	
	/**
	 * Sets a AiEventObserver for observing direction decisions.
	 * @param aiEventObserver
	 */
	public void setAiEventObserver(AiEventObserver aiEventObserver)
	{
		this.aiEventObserver = aiEventObserver;
	}
	
	/**
	 * Fires a calculated next direction to the AiEventObserver.
	 * @param x
	 * @param y
	 * @return true, if there is a observer and the observer accepts the event.
	 */
	protected boolean fireNextOrientationEvent(Orientation orientation) {
		if(aiEventObserver != null)
		{
			return aiEventObserver.nextOrientationEvent(orientation);
		}
		return false;
	}
	
	/**
	 * Fires a calculated next but one direction to the AiEventObserver.
	 * The next but one direction should not be used for driving.
	 * So there is no accept return boolean.
	 * @param x
	 * @param y
	 */
	protected void fireNextButOneOrientationEvent(Orientation orientation) {
		if(aiEventObserver != null)
		{
			aiEventObserver.nextButOneOrientationEvent(orientation);
		}
	}
}
