package de.unihannover.swp2015.robots2.softwarerobot.automate;

import de.unihannover.swp2015.robots2.abstractrobot.Direction;
import de.unihannover.swp2015.robots2.abstractrobot.automate.IState;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

/**
 * The states of the automate of the Pi2Go controls.
 * 
 * @author Lenard Spiecker
 */
public enum SoftwareState implements IState {

	WAIT {
		@Override
		public IState execute() {
			return this;
		}
	},
	FOREWARD, RIGHT, BACKWARD, LEFT;

	private static double DURATION = 2000;

	private double progress = 0.0;
	private long startTime;

	private SoftwareState() {
		//Nothing to do here
	}

	@Override
	public void start() {
		startTime = System.currentTimeMillis();
	}

	@Override
	public IState execute() {
		setProgress((System.currentTimeMillis() - getStartTime()) / DURATION);
		return getProgress() == 1 ? SoftwareState.WAIT : this;
	}

	protected long getStartTime() {
		return startTime;
	}

	/**
	 * Gets the progress of the current state.
	 * 
	 * @return 0.0 <= progress <= 1.0
	 */
	@Override
	public double getProgress() {
		return progress;
	}

	/**
	 * Sets the progress of the current state.
	 * 
	 * @param progress
	 *            0.0 <= progress
	 */
	protected void setProgress(double progress) {
		this.progress = Math.min(1,Math.max(0,progress));
	}

	@Override
	public boolean isWait() {
		return this == WAIT;
	}

	@Override
	public IState getStateForDirection(Direction direction) {
		switch (direction) {
		case LEFT:
			return SoftwareState.LEFT;
		case BACKWARD:
			return SoftwareState.BACKWARD;
		case RIGHT:
			return SoftwareState.RIGHT;
		case FOREWARD:
			return SoftwareState.FOREWARD;
		}
		return FOREWARD;
	}
	
	public static void setVSpeed(float secondsPerField)
	{
		DURATION = secondsPerField*1000;
	}
}
