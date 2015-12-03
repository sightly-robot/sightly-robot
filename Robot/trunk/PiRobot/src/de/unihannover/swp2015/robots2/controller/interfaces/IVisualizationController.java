package de.unihannover.swp2015.robots2.controller.interfaces;

import de.unihannover.swp2015.robots2.controller.externalInterfaces.IVisualization;
import de.unihannover.swp2015.robots2.controller.externalInterfaces.IVisualizationControl;

public interface IVisualizationController extends IController {

	/**
	 * Register an IVisualization that will receive, store and use the settings
	 * broadcasted with an {@link IVisualizationControl}.
	 * 
	 * @param control
	 *            The visualization component that stores settings
	 */
	public void registerVisualization(IVisualization visualization);

}
