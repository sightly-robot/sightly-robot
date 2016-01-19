package de.unihannover.swp2015.robots2.application.events;

import java.util.concurrent.atomic.AtomicReference;

public interface IVisualizationClickEvent {
	/**
	 * Called when the user clicks on the visualization somewhere.
	 * 
	 * @param selectedRobotId (Outward parameter) Set to the selected robot in the visualization
	 * @param button The button object
	 * @param rx which game field (x-axis)
	 * @param ry which game field (y-axis)
	 */
	public void visualizationClicked(org.apache.pivot.wtk.Mouse.Button button, int rx, int ry);
}
