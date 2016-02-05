package de.sightly_robot.sightly_robot.controller.externalInterfaces;

import de.sightly_robot.sightly_robot.controller.interfaces.IGuiController;

/**
 * Interface for a software component that is controlling a graphical game
 * visualization. It may get new settings from a IVisualizationControl interface
 * and be asked for its current settings.
 * 
 * @version 0.1
 * @author Michael Thies
 */
public interface IVisualization {

	/**
	 * Update this visualization's settings.
	 * 
	 * @param settings
	 *            The encoded settings as sent by the control component.
	 */
	public void setSettings(String settings);

	/**
	 * Return the current settings of this visualization, encoded to be
	 * transmitted to the control component.
	 * 
	 * This function will be used after a call of
	 * {@link IGuiController#getVisualizationSettings()} and the returned string
	 * will be passed to
	 * {@link IVisualizationControl#receiveSettings(String)} of each
	 * connected {@link IVisualizationControl}.
	 * 
	 * @return Encoded hardware settings
	 */
	public String getSettings();

}
