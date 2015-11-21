package de.unihannover.swp2015.robots2.model.implementation;

import java.awt.Color;

import de.unihannover.swp2015.robots2.model.interfaces.*;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IPositionWritable;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IRobotWriteable;

;

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
	/** Name of this robot. May be used as nice label in visualizations. */
	private String name;
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
	/** true if the robot is currently in setup state. */
	private volatile boolean setupState;
	/** true if the robot encountered an error. */
	private volatile boolean errorState;
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
		this.position = new Position(0, 0, Orientation.NORTH);
		this.scoreLock = new Object();
	}

	@Override
	public void setPosition(int x, int y, IPosition.Orientation orientation) {
		this.position.setPosition(x, y);
		this.position.setOrientation(orientation);
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setSetupState(boolean setupState) {
		this.setupState = setupState;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
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
	public boolean isSetupState() {
		return this.setupState;
	}

	@Override
	public boolean isMyself() {
		return this.myself;
	}

	@Override
	public Color getColor() {
		// TODO get color from ID hash.
		return Color.black;
	}

	@Override
	public boolean isErrorState() {
		return this.errorState;
	}

	@Override
	public void setErrorState(boolean errorState) {
		this.errorState = errorState;
	}

}
