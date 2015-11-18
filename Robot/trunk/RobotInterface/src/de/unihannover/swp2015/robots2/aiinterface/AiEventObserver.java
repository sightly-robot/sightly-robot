package de.unihannover.swp2015.robots2.aiinterface;

import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

public interface AiEventObserver {

	public boolean nextOrientationEvent(Orientation orientation);
	
	public void nextButOneOrientationEvent(Orientation orientation);
}
