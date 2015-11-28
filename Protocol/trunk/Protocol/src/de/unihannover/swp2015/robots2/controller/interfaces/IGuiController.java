package de.unihannover.swp2015.robots2.controller.interfaces;

import java.util.List;
import java.util.Set;

import de.unihannover.swp2015.robots2.controller.externalInterfaces.IHardwareRobotControl;
import de.unihannover.swp2015.robots2.controller.externalInterfaces.IVisualizationControl;
import de.unihannover.swp2015.robots2.model.interfaces.*;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

/**
 * This interface specifies methods of the main controller to be used by the
 * game control gui.
 * 
 * @version 0.2
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public interface IGuiController extends IController {

	/**
	 * Send the walls of a new map to the game server. The game server will
	 * share the new map with all clients.
	 * 
	 * @param walls
	 *            A list of lists containing the walls of each field as set of
	 *            Orientations line by line. Each inner list represents a row
	 *            (west to east) and each item in these lists will be the new
	 *            walls of the corresponding field. If an Orientation is
	 *            contained in a set, it wont be possible to leave the field in
	 *            this direction.
	 */
	public void sendWalls(List<List<Set<Orientation>>> walls);

	/**
	 * Send a list of the current food state of each field to the game server.
	 * This is necessary to set the initial food after loading a new Map from
	 * file. The game server will share the new state with all clients.
	 * 
	 * @param food
	 *            A list of lists containing the new food state of each field
	 *            line by line. Each inner list represents a row (west to east)
	 *            and each item in these lists will be the new food state of the
	 *            corresponding field.
	 */
	public void sendFood(List<List<Integer>> food);

	/**
	 * Send a list of new growing rates for each field to the game server.
	 * 
	 * @param growingRates
	 *            A list of lists containing the new growing rate for each field
	 *            line by line. Each inner list represents a row (west to east)
	 *            and each item in these lists will be the new growing rate of
	 *            the corresponding field. Growing rate is given as milliseconds
	 *            per food level change.
	 */
	public void sendGrowingRates(List<List<Integer>> growingRates);

	/**
	 * Send game parameters to the game server after loading them from a map
	 * file. The game server will share them with all clients.
	 * 
	 * @param robotSpeed
	 * @param hesitationTime
	 */
	public void sendGameParameters(float robotSpeed, int hesitationTime);

	/**
	 * Set the position of a robot. This is used to set the start position of
	 * each robot. Please use with care after starting the game.
	 * 
	 * @param x
	 *            x coordinate of the field the robot is located on
	 * @param y
	 *            y coordinate of the field the robot is located on
	 * @param orientation
	 *            Orientation of the robot
	 * @param robot
	 *            The robot that should be repositioned
	 */
	public void setRobotPosition(int x, int y,
			IPosition.Orientation orientation, IRobot robot);

	/**
	 * Instruct the server to start the game.
	 */
	public void startGame();

	/**
	 * Instruct the server to stop the game.
	 */
	public void stopGame();

	/**
	 * Reset the game state. Occupied fields will be released and robot's scores
	 * set to zero.
	 * 
	 * Should only be called after stopping the game.
	 * 
	 */
	public void resetGame();

	/**
	 * Pass a String of settings to a robot to control hardware functionality.
	 * 
	 * @param id
	 *            The id of the Robot
	 * @param settings
	 *            A String of settings to be sent to the robot.
	 */
	public void setRobotSettings(String id, String settings);

	/**
	 * Request the settings of a single robot. The robot will be asked for its
	 * current settings, they are transmitted via MQTT and presented to the
	 * registered IHardwareRobotControl via its
	 * {@link IHardwareRobotControl#receiveSettings} method.
	 * 
	 * @param id
	 *            Ask the robot with this id for its settings
	 */
	public void getRobotSettings(String id);

	/**
	 * Pass a String of settings to all visualizations to control their
	 * rendering.
	 * 
	 * @param settings
	 *            A String of settings to be sent to all visualizations.
	 */
	public void setVisualizationSettings(String settings);

	/**
	 * Request the current settings of all visualizations.
	 * 
	 * The visualizations will be asked for their current settings, the settings
	 * are transmitted via MQTT and presented to the registered
	 * IVisualizationControl via its
	 * {@link IVisualizationControl#receiveSettings} method.
	 */
	public void getVisualizationSettings();

	/**
	 * Send a message to a robot to let him lighten up his LEDs.
	 * 
	 * @param id
	 *            The id of the robot that will light up his LEDs
	 */
	public void letRobotBlink(String id);

	/**
	 * Register an IVisualizationControl that will receive the settings of each
	 * visualization after calling {@link #getVisualizationSettings()}.
	 * 
	 * @param control
	 *            The visualization control component that handles the received
	 *            settings.
	 */
	public void registerVisualizationControl(IVisualizationControl control);

	/**
	 * Register an IHardwareRobotControl that will receive the settings of a
	 * robot after calling {@link #getRobotSettings(String id)}.
	 * 
	 * @param control
	 *            The hardware robot control component that handles the received
	 *            settings.
	 */
	public void registerHardwareRobotControl(IHardwareRobotControl control);
}
