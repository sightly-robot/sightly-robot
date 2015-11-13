package de.unihannover.swp2015.robots2.model.implementation;

import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IPositionWritable;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class Position implements IPosition, IPositionWritable {

	private volatile int x;
	private volatile int y;
	private volatile Orientation orientation;
	private volatile int progress;

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
