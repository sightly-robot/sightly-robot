package de.unihannover.swp2015.robots2.controller.externalInterfaces;

import de.unihannover.swp2015.robots2.controller.interfaces.IGuiController;

/**
 * Interface for a software component that is able to remote control
 * visualizations.
 * 
 * @version 0.1
 * @author Michael Thies
 */
public interface IVisualizationControl {

	/**
	 * Receive settings that were requested from visualizations.
	 * 
	 * This method will be called once for each running visualizations after
	 * they were requested with
	 * {@link IGuiController#getVisualizationSettings()}. Or if another
	 * VisualizationControl updates the settings of a visualization via {@link IGuiController#setVisualizationSettings(String)}.
	 * 
	 * @param settings
	 *            The String encoding of the visualizations's current settings
	 */
	public void receiveSettings(String settings);

}
