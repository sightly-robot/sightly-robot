package de.unihannover.swp2015.robots2.robot.softwarerobot.automate;

import de.unihannover.swp2015.robots2.robot.abstractrobot.Direction;
import de.unihannover.swp2015.robots2.robot.abstractrobot.automate.IState;

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
	CONNECTED {
		@Override
		public IState execute() {
			return this;
		}
	},
	SETUP {
		@Override
		public IState execute() {
			return this;
		}
	},
	DISABLED {
		@Override
		public IState execute() {
			return this;
		}
	},
	FOREWARD, RIGHT, BACKWARD, LEFT;

	private static double DURATION = 2000;

	private long startTime;

	private SoftwareState() {
		// Nothing to do here
	}

	@Override
	public void start() {
		startTime = System.currentTimeMillis();
	}

	@Override
	public IState execute() {
		return (System.currentTimeMillis() - getStartTime()) >= DURATION ? SoftwareState.WAIT : this;
	}

	protected long getStartTime() {
		return startTime;
	}

	@Override
	public boolean isWait() {
		return this == WAIT;
	}
	
	@Override
	public boolean isDriving() {
		return this != WAIT && this != CONNECTED && this != DISABLED && this != SETUP;
	}

	@Override
	public IState getStateForDirection(Direction direction) {
		switch (direction) {
		case LEFT:
			return SoftwareState.LEFT;
		case BACKWARDS:
			return SoftwareState.BACKWARD;
		case RIGHT:
			return SoftwareState.RIGHT;
		case FORWARDS:
			return SoftwareState.FOREWARD;
		}
		return FOREWARD;
	}

	public static void setVSpeed(float secondsPerField) {
		DURATION = secondsPerField * 1000;
	}
}
