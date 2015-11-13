package de.unihannover.swp2015.robots2.model.implementation;

import de.unihannover.swp2015.robots2.model.interfaces.*;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.*;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class Field extends AbstractModel implements IField, IFieldWriteable {

	private final int x;
	private final int y;
	private volatile boolean northWall;
	private volatile boolean eastWall;
	private volatile boolean southWall;
	private volatile boolean westWall;
	private int food;
	private final Object foodLock;
	private volatile State state;
	private volatile String lockedBy;
	private volatile int growingRate;
	
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
