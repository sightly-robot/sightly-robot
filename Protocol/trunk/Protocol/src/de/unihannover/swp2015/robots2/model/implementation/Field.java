package de.unihannover.swp2015.robots2.model.implementation;

import de.unihannover.swp2015.robots2.model.interfaces.*;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.*;

/**
 * Basic implementation of the interface IField resp. IFieldWritable.
 * 
 * Implementation of a Field model, representing a single position on the Stage.
 * 
 * @version 0.2
 * @author Patrick Kawczynski and Michael Thies
 */
public class Field extends AbstractModel implements IField, IFieldWriteable {

	/** x-coordinate of this field */
	private final int x;
	/** y-coordinate of this field */
	private final int y;
	/**
	 * true if the field has a wall in northern direction. In that case it's not
	 * possible for a robot to leave it in this direction.
	 */
	private volatile boolean northWall;
	/**
	 * true if the field has a wall in eastern direction. In that case it's not
	 * possible for a robot to leave it in this direction.
	 */
	private volatile boolean eastWall;
	/**
	 * true if the field has a wall in southern direction. In that case it's not
	 * possible for a robot to leave it in this direction.
	 */
	private volatile boolean southWall;
	/**
	 * true if the field has a wall in western direction. In that case it's not
	 * possible for a robot to leave it in this direction.
	 */
	private volatile boolean westWall;
	/** Current state of food on this field. */
	private int food;
	/**
	 * Object its Monitor is used to prevent concurrent write access
	 * (increments) of the food state.
	 */
	private final Object foodLock;
	/**
	 * Current state of the field, used for conflict and collision detection of
	 * multiple robots.
	 */
	private volatile State state;
	/** ID of the robot currently occupying or locking this field. */
	private volatile String lockedBy;
	/**
	 * Growing rate of food on this field. This value is intended to be used by
	 * the server program and may be zero in other parts of the software.
	 */
	private volatile int growingRate;

	/**
	 * Constructs a new Field object.
	 * 
	 * As this Field will always represent the same position on the game's map
	 * the x and y coordinates are final and must be handed to the constructor.
	 * 
	 * @param x
	 *            x-coordinate of the new Field
	 * @param y
	 *            y-coordinate of the new Field
	 */
	public Field(int x, int y) {
		super();

		this.x = x;
		this.y = y;
		this.foodLock = new Object();
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}

	@Override
	public boolean isWall(IPosition.Orientation orientation) {
		switch (orientation) {
		case NORTH:
			return this.northWall;
		case EAST:
			return this.eastWall;
		case SOUTH:
			return this.southWall;
		case WEST:
			return this.westWall;
		default:
			return false;
		}
	}

	@Override
	public int getFood() {
		return this.food;
	}

	@Override
	public State getState() {
		return this.state;
	}

	@Override
	public String getLockedBy() {
		return this.lockedBy;
	}

	@Override
	public void setWall(IPosition.Orientation orientation, boolean wall) {
		switch (orientation) {
		case NORTH:
			this.northWall = wall;
			break;
		case EAST:
			this.eastWall = wall;
			break;
		case SOUTH:
			this.southWall = wall;
			break;
		case WEST:
			this.westWall = wall;
			break;
		}
	}

	@Override
	public void setFood(int food) {
		synchronized (this.foodLock) {
			this.food = food;
		}
	}

	@Override
	public int incrementFood() {
		synchronized (this.foodLock) {
			this.food++;
			return this.food;
		}
	}

	@Override
	public void setState(State state) {
		this.state = state;
	}

	@Override
	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

	@Override
	public void setGrowingRate(int growingRate) {
		this.growingRate = growingRate;
	}

	@Override
	public int getGrowingRate() {
		return this.growingRate;
	}

}
