package de.unihannover.swp2015.robots2.application.events;

public interface IVisualizationClickEvent {
	/**
	 * Called when the user clicks on the visualization somewhere.
	 * 
	 * @param button The button object
	 * @param rx which game field (x-axis)
	 * @param ry which game field (y-axis)
	 */
	public void visualizationClicked(org.apache.pivot.wtk.Mouse.Button button, int rx, int ry);
}