package de.unihannover.swp2015.robots2.model.implementation;

import de.unihannover.swp2015.robots2.external.implementation.Position;
import de.unihannover.swp2015.robots2.external.interfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IFieldWriteable;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class Field implements IField, IFieldWriteable {
	
	private int x;
	private int y;
	private boolean northWall;
	private boolean eastWall;
	private boolean southWall;
	private boolean westWall;
	private int food;
	private State state;
	private String lockedBy;
	
	/**
	 * 
	 * @version 0.1
	 * @author Patrick Kawczynski
	 */
	public enum State {
		FREE, LOCKED, OCCUPIED, OURS, LOCK_WAIT, RANDOM_WAIT
	}

	@Override
	public void observe(IModelObserver observer) {
		// TODO Auto-generated method stub
		
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
	public boolean isWall(Position.Orientation orientation) {
		switch(orientation) {
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
	public void emitEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWall(Position.Orientation orientation, boolean wall) {
		switch(orientation) {
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
		this.food = food;
	}

	@Override
	public void setState(State state) {
		this.state = state;
	}

	@Override
	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

}
