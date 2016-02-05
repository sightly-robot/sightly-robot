package de.sightly_robot.sightly_robot.controller.interfaces;

import java.util.List;
import java.util.Set;

import de.sightly_robot.sightly_robot.controller.externalInterfaces.IHardwareRobotControl;
import de.sightly_robot.sightly_robot.controller.externalInterfaces.IVisualizationControl;
import de.sightly_robot.sightly_robot.model.interfaces.*;
import de.sightly_robot.sightly_robot.model.interfaces.IPosition.Orientation;

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
	 * Send the walls of a new map to all MQTT clients.
	 * 
	 * This method does not affect the local model immediately, but instead
	 * sends the walls to all parts of the software via MQTT. Our GUI will also
	 * receive this message and so write the new walls to the model. This won't
	 * work without MQTT connected.
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
	 * Send possible start positions to all listening GUIs.
	 * 
	 * This method does not affect the local model immediately, but instead
	 * sends the list of start positions to the MQTT network. All listening GUIs
	 * (including ourself) will receive this message and take over the list to
	 * their model. Thus, this method will not work without MQTT connected.
	 * 
	 * @param positions
	 *            A list of positions which contain the x, y and orientation of
	 *            the start position. Robots can be placed there and shall have
	 *            the give orientation.
	 */
	public void sendStartPositions(List<IPosition> positions);

	/**
	 * Send a list of the current food state of each field to the game server.
	 * 
	 * This is necessary to set the initial food after loading a new Map from
	 * file. The game server will share the new state with all clients. Of
	 * course, this won't work without MQTT connection and a running game
	 * server.
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
	 * The growing rates are not stored in the local model.
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
	 * Send game parameters to the MQTT network.
	 * 
	 * The hesitation time is not used anymore. Please use
	 * {@link #sendVRobotSpeed(float, float)} instead, which sends the new
	 * rotation speed instead of hesitation time.
	 * 
	 * @deprecated
	 * @param robotSpeed
	 *            maximum movement speed of virtual robots in seconds per field
	 * @param hesitationTime
	 *            (deprecated and not used)
	 */
	@Deprecated
	public void sendGameParameters(float robotSpeed, int hesitationTime);

	/**
	 * Send game maximum speeds for virtual robots to the MQTT network.
	 * 
	 * @param robotSpeed
	 *            maximum movement speed of virtual robots in seconds per field
	 * @param robotRotationSpeed
	 *            maximum rotation speed of virtual robots in seconds per 360°
	 */
	public void sendVRobotSpeed(float robotSpeed, float robotRotationSpeed);

	/**
	 * Set the position of a robot.
	 * 
	 * Please use the new version of this method that takes the robot's id as
	 * last argument.
	 * 
	 * @deprecated
	 * @param x
	 *            x coordinate of the field the robot is located on
	 * @param y
	 *            y coordinate of the field the robot is located on
	 * @param orientation
	 *            Orientation of the robot
	 * @param robot
	 *            The robot that should be repositioned
	 */
	@Deprecated
	public void setRobotPosition(int x, int y, IPosition.Orientation orientation, IRobot robot);

	/**
	 * Set the position of a robot.
	 * 
	 * This is used to set the start position of each robot. Please use with
	 * care after starting the game.
	 * 
	 * This action does not affect the local model, but only sends the
	 * 
	 * @param x
	 *            x coordinate of the field the robot is located on
	 * @param y
	 *            y coordinate of the field the robot is located on
	 * @param orientation
	 *            Orientation of the robot
	 * @param robot
	 *            ID of the robot that should be repositioned
	 */
	public void setRobotPosition(int x, int y, IPosition.Orientation orientation, String robot);

	/**
	 * Start the game.
	 * 
	 * This method sends a MQTT message to the CONTROL_STATE topic to inform all
	 * components about the game start.
	 * 
	 * The local model is not changed immediately, but will be updated in
	 * response to the MQTT message, when it returns. Thus, calling this method
	 * will not have any affect before MQTT connection establishment.
	 */
	public void startGame();

	/**
	 * Stop the game.
	 * 
	 * This method sends a MQTT message to the CONTROL_STATE topic to inform all
	 * components about the game stop.
	 * 
	 * The local model is not changed immediately, but will be updated in
	 * response to the MQTT message, when it returns. Thus, calling this method
	 * will not have any affect before MQTT connection establishment.
	 */
	public void stopGame();

	/**
	 * Reset the game.
	 * 
	 * This will instruct the game server to reset all robot's scores to zero.
	 * It does not have any immediate effect on the local model.
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
	 * Request the settings of a single robot.
	 * 
	 * The robot will be asked for its current settings, they are transmitted
	 * via MQTT and presented to the registered IHardwareRobotControl via its
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
	 * Set a robot to disabled state.
	 * 
	 * This will sent an MQTT message to manually disable this robot. The
	 * message will inform all clients (including ourself) about this status
	 * change, which subsequently set the robot's state to MANUAL_DISABLE_GUI.
	 * This should also cause the robot to stop.
	 * 
	 * @param id
	 *            Id of the robot to disable
	 */
	public void disableRobot(String id);

	/**
	 * Delete a robot from the game.
	 * 
	 * This method sends an MQTT message instructing all components to remove
	 * this robot from their model. It does not modify the local model
	 * immediately and therefore only works with MQTT connected.
	 * 
	 * All fields the robot has been occupying are instantly marked as released
	 * by this method.
	 * 
	 * @param id
	 *            The id of the robot to be deleted
	 */
	public void deleteRobot(String id);

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
