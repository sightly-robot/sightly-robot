package de.sightly_robot.sightly_robot.controller.interfaces;

import de.sightly_robot.sightly_robot.controller.externalInterfaces.IVisualization;
import de.sightly_robot.sightly_robot.controller.externalInterfaces.IVisualizationControl;

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
