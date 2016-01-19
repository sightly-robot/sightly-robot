package de.unihannover.swp2015.robots2.application.models;

import java.net.URL;

import org.apache.pivot.util.concurrent.TaskExecutionException;
import org.apache.pivot.wtk.media.Image;

import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot.RobotState;

/**
 * Table element data holder for pivot table view.
 * 
 * @author Tim Ebbeke
 */
public class TableElement {
	private Image state = null;
	private String id;
	private String player;
	private String name;
	private int points;
	private String virtual;
	
	private final String baseResourcePath = "/de/unihannover/swp2015/robots2/application/images/";	
	private final String virtualMarker = "yes";
	private final String notVirtualMarker = "not";

	// source: Iconfinder.com - free - no link back licenses
	private Image disconnectedImage;
	private Image connectedImage;
	private Image disabledImage; // MANUAL_DISABLED_GUI + MANUAL_DISABLED_ROBOT
	private Image robotErrorImage; 
	private Image setupStateImage;
	private Image enabledImage;

	/**
	 * Construct a table element from its components 
	 * @param id The robot id. used internally.
	 * @param player The robot name, can be choosen by robot developers.
	 * @param points The robot points.
	 */
	public TableElement(IRobot robot, boolean showId) {
		super();
		this.id = robot.getId();
		this.name = robot.getName();
		this.points = robot.getScore();
		
		setVirtual(!robot.isHardwareRobot());		
		shallShowId(showId);
		try {
			disconnectedImage = Image.load(getClass().getResource(baseResourcePath + "disconnected.png"));
			connectedImage = Image.load(getClass().getResource(baseResourcePath + "connected.png"));
			disabledImage = Image.load(getClass().getResource(baseResourcePath + "disabled.png"));
			robotErrorImage = Image.load(getClass().getResource(baseResourcePath + "error.png"));
			setupStateImage = Image.load(getClass().getResource(baseResourcePath + "setup.png"));
			enabledImage = Image.load(getClass().getResource(baseResourcePath + "enabled.png"));
		} catch (TaskExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setState(robot.getState());
	}
	
	/**
	 * The the player name.
	 * @return Returns the robot name.
	 */
	public String getPlayer() {
		return player;
	}
	
	public void setState(RobotState robotState) {
		switch (robotState) {
		case CONNECTED:
			state = connectedImage;
			break;
		case DISCONNECTED:
			state = disconnectedImage;
			break;
		case ENABLED:
			state = enabledImage;
			break;
		case MANUAL_DISABLED_GUI:
			state = disabledImage;
			break;
		case MANUAL_DISABLED_ROBOT:
			state = disabledImage;
			break;
		case ROBOTICS_ERROR:
			state = robotErrorImage;
			break;
		case SETUPSTATE:
			state = setupStateImage;
			break;
		default:
			break;		
		}
	}
	
	/**
	 * Toggles in between name and id. 
	 * @param showId Will show the id if set to true.
	 */
	public void shallShowId(boolean showId) {
		if (showId)
			this.player = id;
		else
			this.player = name;
	}
	
	/**
	 * Sets the player name.
	 * @param player The robot name.
	 */
	public void setPlayer(String player) {
		this.player = player;
	}
	
	/**
	 * Gets the robot points / score.
	 * @return The robot score
	 */
	public int getPoints() {
		return points;
	}
	
	/**
	 * Sets the robots points / score
	 * @param points The robot score.
	 */
	public void setPoints(int points) {
		this.points = points;
	}
	
	/**
	 * Gets the robot id (which is unique in a network / game).
	 * @return The robot id.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the robot id (which has to be unique in a network / game).
	 * @param id The robot id.
	 */
	public void setId(String id) {
		this.id = id;
	}	
	
	/**
	 * Sets whether the robot is virtual or not. 
	 * @param isVirtual is the robot virtual?
	 */
	public void setVirtual(boolean isVirtual) {
		if (isVirtual)
			this.virtual = virtualMarker;
		else
			this.virtual = notVirtualMarker;
	}
	
	/**
	 * Gets whether the robot is virtual or not. Must be string for correct displaying
	 * @return is the robot virtual?
	 */
	public String isVirtual() {
		return virtual;
	}

	public Image getState() {
		return state;
	}

	public void setState(Image state) {
		this.state = state;
	}
}
