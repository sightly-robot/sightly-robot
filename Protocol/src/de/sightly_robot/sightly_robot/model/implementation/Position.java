package de.sightly_robot.sightly_robot.model.implementation;

import de.sightly_robot.sightly_robot.model.interfaces.IPosition;
import de.sightly_robot.sightly_robot.model.writeableInterfaces.IPositionWritable;

/**
 * Basic implementation of the interface IPositionWritable. Wraps Field
 * coordinates on the Stage, an Orientation and a detailed drive progress for
 * usage as current robot position or start position.
 * 
 * All properties are declared as volatile, hence they should be multi thread
 * save.
 * 
 * @version 0.2
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public class Position implements IPosition, IPositionWritable {

	/** x coordinate of the Field referenced by this position */
	private volatile int x;
	/** y coordinate of the Field referenced by this position */
	private volatile int y;
	/** Orientation of the Robot or start position */
	private volatile Orientation orientation;
	/**
	 * Progress to the next field in per mills: 0 = just started, 1000 = reached
	 * next field
	 */
	private volatile int progress;

	/**
	 * Construct a new Position object with given coordinates and orientation.
	 * 
	 * The progress property defaults to 0. This object should only be
	 * constructed once and only updated via its setter methods afterwards.
	 * 
	 * @param x
	 * @param y
	 * @param orientation
	 */
	public Position(int x, int y, Orientation orientation) {
		this.x = x;
		this.y = y;
		this.orientation = orientation;
		this.progress = 0;
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
	public Orientation getOrientation() {
		return this.orientation;
	}

	@Override
	public int getProgress() {
		return this.progress;
	}

	@Override
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}

	@Override
	public void setProgress(int progress) {
		this.progress = progress;
	}

}
