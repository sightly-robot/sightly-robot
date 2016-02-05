package de.sightly_robot.sightly_robot.robot.softwarerobot.automate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.sightly_robot.sightly_robot.robot.abstractrobot.Direction;
import de.sightly_robot.sightly_robot.robot.abstractrobot.automate.IState;
import de.sightly_robot.sightly_robot.robot.abstractrobot.automate.IStateEvent;

/**
 * The states of the automate of the Pi2Go controls.
 * 
 * @author Lenard Spiecker
 */
public enum SoftwareState implements IState {

	/** WAIT STATE while waiting */
	WAIT,
	/** CONNECTED STATE while connecting */
	CONNECTED,
	/** SETUP STATE while setup */
	SETUP,
	/** DISABLED STATE while disabling */
	DISABLED, FOREWARD {
		@Override
		public IState execute() {
			return timedExecute();
		}
	},
	RIGHT {
		@Override
		public IState execute() {
			return timedExecute();
		}
	},
	BACKWARD {
		@Override
		public IState execute() {
			return timedExecute();
		}
	},
	LEFT {
		@Override
		public IState execute() {
			return timedExecute();
		}
	};

	/** LOGGER: */
	private static Logger LOGGER = LogManager.getLogger(SoftwareState.class.getName());

	@SuppressWarnings("unused")
	private static IStateEvent iStateEvent;

	private long startTime;

	@SuppressWarnings("unused")
	private Direction nextButOneDirection;

	private float duration;

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
		return this;
	}

	public IState timedExecute() {
		return (System.currentTimeMillis() - getStartTime()) >= getDuration() ? SoftwareState.WAIT : this;
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
	public static void setVSpeed(float secondsPerField, float secondsPer360) {
		float moveDuration = secondsPerField * 1000f;
		float rotationDuration = secondsPer360 * 1000f / 4.0f;
		LEFT.setDuration(moveDuration + rotationDuration);
		RIGHT.setDuration(moveDuration + rotationDuration);
		FOREWARD.setDuration(moveDuration);
		BACKWARD.setDuration(moveDuration + rotationDuration * 2);
	}

	public void setNextButOneDirection(Direction direction) {
		nextButOneDirection = direction;
	}

	public void setIStateEventObserver(IStateEvent iStateEvent) {
		SoftwareState.iStateEvent = iStateEvent;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}
}
