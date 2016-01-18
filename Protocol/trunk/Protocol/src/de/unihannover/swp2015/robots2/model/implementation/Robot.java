package de.unihannover.swp2015.robots2.model.implementation;

import java.awt.Color;

import de.unihannover.swp2015.robots2.model.interfaces.*;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IPositionWritable;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IRobotWriteable;


/**
 * Basic implementation of the interface IRobotWritable.
 * 
 * All changeable properties are declared as volatile or protected with
 * synchronized blocks, which should enable multithread access of this object.
 * 
 * @version 0.2
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public class Robot extends AbstractModel implements IRobot, IRobotWriteable {

	/** ID of this robot. Never changes at runtime. */
	private final String id;
	/**
	 * true if this robot object represents a hardware robot in opposite to a
	 * virtual robot.
	 */
	private final boolean hardwareRobot;
	/**
	 * Current position of this Robot. Wraps coordinates, orientation and
	 * drivign progress.
	 */
	private final IPositionWritable position;
	/** Current score of this robot. */
	private int score;
	/** Object used to synchronize the access to score property. */
	private final Object scoreLock;
	/**
	 * the robot's current state (or last state before disconnection).
	 * Disconnection will be stored in {@link #connectionState}
	 */
	private volatile RobotState state;
	/** true if this robot is currently connected to the MQTT broker. */
	private volatile boolean connectionState;
	/**
	 * true if this Robot object represents the robot running this piece of
	 * software
	 */
	private final boolean myself;

	/**
	 * Contstruct a new Robot object.
	 * 
	 * @param id
	 *            The ID of the robot represented by this object
	 * @param hardwareRobot
	 *            Whether this Robot is a hardware robot or virtual.
	 * @param myself
	 *            Whether this Robot object represents the robot running this
	 *            software.
	 */
	public Robot(String id, boolean hardwareRobot, boolean myself) {
		super();

		this.id = id;
		this.myself = myself;
		this.hardwareRobot = hardwareRobot;
		this.position = new Position(-1, -1, Orientation.NORTH);
		this.scoreLock = new Object();

		/*
		 * We assume any other robot to be connected initially, as we will only
		 * receive their connection state on failure. Our own connection state
		 * will be updated by the main controller on connection establishment so
		 * we initially set it to false.
		 */
		this.connectionState = !myself;

		/*
		 * The CONNECTED state is the first state any robot enters. (after
		 * connection. But DISCONNECTED state is managed with the
		 */
		this.state = RobotState.CONNECTED;
	}

	@Override
	public void setPosition(int x, int y, IPosition.Orientation orientation) {
		this.position.setPosition(x, y);
		this.position.setOrientation(orientation);
	}

	@Override
	public void setProgress(int progress) {
		this.position.setProgress(progress);
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public boolean isHardwareRobot() {
		return this.hardwareRobot;
	}

	@Override
	public IPosition getPosition() {
		return this.position;
	}

	@Override
	public int getScore() {
		synchronized (this.scoreLock) {
			return this.score;
		}
	}

	@Override
	public void setScore(int score) {
		synchronized (this.scoreLock) {
			this.score = score;
		}
	}

	@Override
	public int addScore(int score) {
		synchronized (this.scoreLock) {
			this.score += score;
			return this.score;
		}
	}

	@Override
	public RobotState getState() {
		// In case of disconnected robot return DISCONNECTED state
		if (!this.connectionState)
			return RobotState.DISCONNECTED;
		else
			return this.state;
	}

	@Override
	public void setRobotState(RobotState state) {
		this.state = state;
	}

	@Override
	public void setRobotConnectionState(boolean state) {
		this.connectionState = state;
	}

	@Override
	public boolean isMyself() {
		return this.myself;
	}

	@Override
	public Color getColor() {
		float hue = Integer.parseInt(this.id.substring(0, 2), 16) / 255f;
		return new Color(Color.HSBtoRGB(hue, 1, 1));
	}

	@Override
	public String getName() {
		return RobotNames.getName(Integer.parseInt(this.id.substring(2, 6), 16));
	}

}
