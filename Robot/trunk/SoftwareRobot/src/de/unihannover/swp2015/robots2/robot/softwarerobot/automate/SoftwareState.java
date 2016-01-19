package de.unihannover.swp2015.robots2.robot.softwarerobot.automate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.robot.abstractrobot.Direction;
import de.unihannover.swp2015.robots2.robot.abstractrobot.automate.IState;
import de.unihannover.swp2015.robots2.robot.abstractrobot.automate.IStateEvent;

/**
 * The states of the automate of the Pi2Go controls.
 * 
 * @author Lenard Spiecker
 */
public enum SoftwareState implements IState {

	/** WAIT STATE while waiting */
	WAIT {
		@Override
		public IState execute() {
			return this;
		}
	},
	/** CONNECTED STATE while connecting */
	CONNECTED {
		@Override
		public IState execute() {
			return this;
		}
	},
	/** SETUP STATE while setup */
	SETUP {
		@Override
		public IState execute() {
			return this;
		}
	},
	/** DISABLED STATE while disabling */
	DISABLED {
		@Override
		public IState execute() {
			return this;
		}
	},
	FOREWARD, RIGHT, BACKWARD, LEFT;

	/** LOGGER: */
	private static Logger LOGGER = LogManager.getLogger(SoftwareState.class.getName());

	private static double DURATION = 2000;

	@SuppressWarnings("unused")
	private static IStateEvent iStateEvent;

	private long startTime;

	@SuppressWarnings("unused")
	private Direction nextButOneDirection;

	private SoftwareState() {
		// Nothing to do here
	}

	@Override
	public void start() {
		LOGGER.trace("Start State: " + toString());
		startTime = System.currentTimeMillis();
	}

	@Override
	public IState execute() {
		return (System.currentTimeMillis() - getStartTime()) >= DURATION ? SoftwareState.WAIT : this;
	}

	/**
	 * Returns the time of the last state-swicth.
	 * 
	 * @return
	 */
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

	/**
	 * Sets the Speed of the Stateswitch in SecondsPerField.
	 * 
	 * @param secondsPerField
	 */
	public static void setVSpeed(float secondsPerField) {
		DURATION = secondsPerField * 1000;
	}

	public void setNextButOneDirection(Direction direction) {
		nextButOneDirection = direction;
	}
	
	public void setIStateEventObserver(IStateEvent iStateEvent) {
		SoftwareState.iStateEvent = iStateEvent;
	}
}
