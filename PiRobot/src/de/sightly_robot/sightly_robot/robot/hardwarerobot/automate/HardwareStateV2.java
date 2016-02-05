package de.sightly_robot.sightly_robot.robot.hardwarerobot.automate;

import java.awt.Color;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.sightly_robot.sightly_robot.robot.abstractrobot.Direction;
import de.sightly_robot.sightly_robot.robot.abstractrobot.automate.IState;
import de.sightly_robot.sightly_robot.robot.abstractrobot.automate.IStateEvent;
import de.sightly_robot.sightly_robot.robot.hardwarerobot.pi2gocontroller.LEDAndServoController;
import de.sightly_robot.sightly_robot.robot.hardwarerobot.pi2gocontroller.MotorController;
import de.sightly_robot.sightly_robot.robot.hardwarerobot.pi2gocontroller.Pi2GoGPIOController;

/**
 * The states of the control automate of a hardware robot (Pi2Go).
 * 
 * @author Philipp Rohde & Lenard
 */
public enum HardwareStateV2 implements IState {

	/**
	 * The robot follows a line until he reaches the next junction.
	 */
	FOLLOW_LINE {

		@Override
		public IState execute() {
			boolean left = GPIO.isLineLeft();
			boolean right = GPIO.isLineRight();

			if (left && right) {
				return nextState(DRIVE_ON_CELL);
			} else if (!left && !right) {
				MOTORS.go(calcProgress() < 0.7 ? FASTER : NORMAL);
			} else if (right) {
				MOTORS.go(NORMAL, SLOW);
			} else if (left) {
				MOTORS.go(SLOW, NORMAL);
			}
			setCarLeds(nextButOneDirection);
			return nextState(this);
		}
	},
	/**
	 * The robot drives fully onto the cell.
	 */
	DRIVE_ON_CELL {
		private static final double DRIVE_DURATION = 280.0;
		@Override
		public IState execute() {
			boolean left = GPIO.isLineLeft();
			boolean right = GPIO.isLineRight();

			if (System.currentTimeMillis() - startTime >= DRIVE_DURATION) {
				return nextState(WAIT);
			} else if (left && right) {
				MOTORS.go(FASTER);
			} else if (!right && !left) {
				MOTORS.go(FASTER);
			} else if (right) {
				MOTORS.go(NORMAL, SLOW);
			} else if (left) {
				MOTORS.go(SLOW, NORMAL);
			}
			setCarLeds(HardwareStateV2.nextButOneDirection);
			return nextState(this);
		}
	},
	/**
	 * The robot is in setupState.
	 */
	SETUP {
		@Override
		public IState execute() {
			MOTORS.stop();
			// LEDS:
			double x = (System.currentTimeMillis() - startTime) % 4000;
			double y1 = Math.max(0, 1 - Math.pow((x - 600) / 600, 2));
			double y2 = Math.max(0, 1 - Math.pow((x - 1600) / 600, 2));
			double y3 = Math.max(0, 1 - Math.pow((x - 2600) / 600, 2));
			double y4 = Math.max(0, 1 - Math.pow((x - 3600) / 600, 2)) + Math.max(0, 1 - Math.pow((x + 400) / 600, 2)); 
			LEDS.setLED(LEDS_FRONT, new Color((int) (LEDS.getAccentColor().getRed() * y1),
					(int) (LEDS.getAccentColor().getGreen() * y1), (int) (LEDS.getAccentColor().getBlue() * y1)));
			LEDS.setLED(LEDS_RIGHT, new Color((int) (LEDS.getAccentColor().getRed() * y2),
					(int) (LEDS.getAccentColor().getGreen() * y2), (int) (LEDS.getAccentColor().getBlue() * y2)));
			LEDS.setLED(LEDS_REAR, new Color((int) (LEDS.getAccentColor().getRed() * y3),
					(int) (LEDS.getAccentColor().getGreen() * y3), (int) (LEDS.getAccentColor().getBlue() * y3)));
			LEDS.setLED(LEDS_LEFT, new Color((int) (LEDS.getAccentColor().getRed() * y4),
					(int) (LEDS.getAccentColor().getGreen() * y4), (int) (LEDS.getAccentColor().getBlue() * y4)));
			return nextState(this);
		}
	},
	/**
	 * The robot is disabled
	 */
	DISABLED {
		@Override
		public IState execute() {
			MOTORS.stop();
			// LEDS:
			double x = (System.currentTimeMillis() - startTime) % 3000;
			double y1 = Math.max(0, 1 - Math.pow((x - 200) / 200, 2));
			double y2 = Math.max(0, 1 - Math.pow((x - 400) / 200, 2));
			LEDS.setLED(LEDS_LEFT, new Color((int) (LEDS.getAccentColor().getRed() * y1),
					(int) (LEDS.getAccentColor().getGreen() * y1), (int) (LEDS.getAccentColor().getBlue() * y1)));
			LEDS.setLED(LEDS_RIGHT, new Color((int) (LEDS.getAccentColor().getRed() * y1),
					(int) (LEDS.getAccentColor().getGreen() * y1), (int) (LEDS.getAccentColor().getBlue() * y1)));
			LEDS.setLED(LEDS_FRONT, new Color((int) (LEDS.getAccentColor().getRed() * y2),
					(int) (LEDS.getAccentColor().getGreen() * y2), (int) (LEDS.getAccentColor().getBlue() * y2)));
			LEDS.setLED(LEDS_REAR, new Color((int) (LEDS.getAccentColor().getRed() * y2),
					(int) (LEDS.getAccentColor().getGreen() * y2), (int) (LEDS.getAccentColor().getBlue() * y2)));
			return nextState(this);
		}
	},
	/**
	 * The robot awaits a new command.
	 */
	WAIT {
		@Override
		public IState execute() {
			MOTORS.stop();
			setCarLeds(nextButOneDirection);
			//RESET NextDirection after a while:
			if(System.currentTimeMillis()-startTime > 3000)
				HardwareStateV2.nextButOneDirection = Direction.FORWARDS;
			return nextState(this);
		}
	},
	/**
	 * The robot awaits a new command.
	 */
	CONNECTED {
		@Override
		public IState execute() {
			MOTORS.stop();
			// LEDs:
			double x = (System.currentTimeMillis() - startTime) % 8000;
			double i = Math.max(0, 1 - Math.pow((x - 700) / 700, 2));

			LEDS.setAllLEDs(new Color((int) (LEDS.getAccentColor().getRed() * i),
					(int) (LEDS.getAccentColor().getGreen() * i), (int) (LEDS.getAccentColor().getBlue() * i)));
			return nextState(this);
		}
	},
	/**
	 * First part of turning left.
	 */
	TURN_LEFT_1 {
		@Override
		public IState execute() {
			if (GPIO.isLineLeft()) {
				setCarLeds(Direction.LEFT);
				MOTORS.spinRight(FASTER);
				return nextState(this);
			} else {
				return nextState(TURN_LEFT_2);
			}
		}
	},
	/**
	 * Second part of turning left.
	 */
	TURN_LEFT_2 {
		@Override
		public IState execute() {
			if (!GPIO.isLineLeft()) {
				setCarLeds(Direction.LEFT);
				MOTORS.spinRight(calcProgress() < 0.4 ? NORMAL : SLOW);
				return nextState(this);
			} else {
				return nextState(FOLLOW_LINE);
			}
		}
	},
	/**
	 * First part of turning right.
	 */
	TURN_RIGHT_1 {
		@Override
		public IState execute() {
			if (GPIO.isLineRight()) {
				setCarLeds(Direction.RIGHT);
				MOTORS.spinLeft(FASTER);
				return nextState(this);
			} else {
				return nextState(TURN_RIGHT_2);
			}
		}
	},
	/**
	 * Second part of turning right.
	 */
	TURN_RIGHT_2 {
		@Override
		public IState execute() {
			if (!GPIO.isLineRight()) {
				setCarLeds(Direction.RIGHT);
				MOTORS.spinLeft(calcProgress() < 0.4 ? NORMAL : SLOW);
				return nextState(this);
			} else {
				return nextState(FOLLOW_LINE);
			}
		}
	},
	/**
	 * First part of turning 180 degrees.
	 */
	TURN_180_1 {
		@Override
		public IState execute() {
			if (GPIO.isLineLeft()) {
				setCarLeds(Direction.LEFT);
				MOTORS.spinRight(FASTER);
				return nextState(this);
			} else {
				return nextState(TURN_180_2);
			}
		}
	},
	/**
	 * Second part of turning 180 degrees.
	 */
	TURN_180_2 {
		@Override
		public IState execute() {
			if (!GPIO.isLineLeft()) {
				setCarLeds(Direction.LEFT);
				MOTORS.spinRight(calcProgress() < 0.4 ? NORMAL : SLOW);
				return nextState(this);
			} else {
				return nextState(TURN_180_3);
			}
		}
	},
	/**
	 * Third part of turning 180 degrees.
	 */
	TURN_180_3 {
		@Override
		public IState execute() {
			if (!GPIO.isLineRight()) {
				setCarLeds(Direction.BACKWARDS);
				MOTORS.go(-NORMAL);
				return nextState(this);
			} else {
				return nextState(TURN_180_4);
			}
		}
	},
	/**
	 * Fourth part of turning 180 degrees.
	 */
	TURN_180_4 {
		@Override
		public IState execute() {
			if (!GPIO.isLineLeft()) {
				setCarLeds(Direction.LEFT);
				MOTORS.spinRight(calcProgress() < 0.7 ? (calcProgress() < 0.4 ? FASTER : NORMAL) : SLOW);
				return nextState(this);
			} else {
				return nextState(FOLLOW_LINE);
			}
		}
	};

	/** LOGGER: */
	private static Logger LOGGER = LogManager.getLogger(HardwareStateV2.class.getName());
	// speed configuration
	private static final int FASTER = 60;
	private static final int NORMAL = 44;
	private static final int SLOW = 25;

	// Pi2GO controller
	private static final LEDAndServoController LEDS = LEDAndServoController.getInstance();
	private static final Pi2GoGPIOController GPIO = Pi2GoGPIOController.getInstance();
	private static final MotorController MOTORS = MotorController.getInstance();

	// LEDs
	private static final int LEDS_FRONT = LEDAndServoController.FRONT;
	private static final int LEDS_REAR = LEDAndServoController.REAR;
	private static final int LEDS_LEFT = LEDAndServoController.LEFT;
	private static final int LEDS_RIGHT = LEDAndServoController.RIGHT;

	// LED colors
	private static final Color COLOR_BREAK = Color.RED;
	private static final Color COLOR_REAR = Color.RED.darker().darker();
	private static final Color COLOR_FRONT = Color.WHITE;
	private static final Color COLOR_FRONT_DARKER = Color.WHITE.darker().darker();
	private static final Color COLOR_BLINK = Color.ORANGE;

	private static long[] measurements = new long[] { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
			100, 100, 100, 100 };
	protected long startTime = 0;
	protected static Direction nextButOneDirection = Direction.FORWARDS;
	private static IStateEvent iStateEvent;

	/**
	 * Should be called at start to reset the measurements. The super-start
	 * should be called in state's start-methods for correct startTime and
	 * nextButOneDirection.
	 */
	@Override
	public void start() {
		nextButOneDirection = Direction.FORWARDS;
		startIntern();
	}
	
	/**
	 * Intern start Method. Is not reseting the nextButOneDirection.
	 */
	private void startIntern() {
		LOGGER.trace("Start State " + toString());
		startTime = System.currentTimeMillis();
		execute();
	}

	/**
	 * Should be called at return of a driving-state lifecicle.</br>
	 * <b>Measurement:</b> Measures the state-duration and returns the
	 * nextState.</br>
	 * <b>Error Detection:</b> Returns next State or WAIT for a robotic error.
	 * Fires the robotic-error EVENT.
	 * 
	 * @param nextState
	 * @return nextState or WAIT
	 */
	protected HardwareStateV2 nextState(HardwareStateV2 nextState) {
		// Detect Drive Error:
		if (isDriving() && System.currentTimeMillis() - startTime > 4000) {
			if (iStateEvent != null)
				iStateEvent.iStateErrorOccured();
			WAIT.startIntern();
			return WAIT;
		}
		//On Stateswitch:
		if (nextState != this) {
			//Measure:
			measurements[this.ordinal()] = (long) (measurements[this.ordinal()] * 0.8
					+ 0.2 * (System.currentTimeMillis() - startTime));
			//Start new State:
			nextState.startIntern();
		}
			
		return nextState;
	}

	/**
	 * Can be called while executing to calculate the current measured Progress of a state.
	 * 
	 * @return progress double 0 to endless (After learning it should be in range of 1)
	 */
	protected double calcProgress() {
		return ((double) (System.currentTimeMillis() - startTime)) / measurements[this.ordinal()];
	}

	/**
	 * Returns whether the current state is the wait state or not.
	 * 
	 * @return {@code true} if the current state is the wait state,
	 *         {@code false} otherwise
	 */
	@Override
	public boolean isWait() {
		return this == WAIT;
	}

	@Override
	public boolean isDriving() {
		return this != WAIT && this != SETUP && this != DISABLED && this != CONNECTED;
	}

	/**
	 * Sets the Car Led style and is blinking in direction.
	 */
	protected void setCarLeds(Direction direction) {
		boolean right = direction == Direction.RIGHT || direction == Direction.BACKWARDS;
		boolean left = direction == Direction.LEFT || direction == Direction.BACKWARDS;

		boolean blink = (System.currentTimeMillis() - FOLLOW_LINE.startTime) % 800 < 400;

		right = blink && right;
		left = blink && left;

		LEDS.setLED(LEDS_FRONT, this == HardwareStateV2.WAIT ? COLOR_FRONT_DARKER : COLOR_FRONT);
		LEDS.setLED(LEDS_REAR, this == DRIVE_ON_CELL ? COLOR_BREAK : COLOR_REAR);
		if (right) {
			LEDS.setLED(LEDS_RIGHT, COLOR_BLINK);
		} else {
			LEDS.setLED(LEDS_RIGHT, Color.BLACK);
		}
		if (left) {
			LEDS.setLED(LEDS_LEFT, COLOR_BLINK);
		} else {
			LEDS.setLED(LEDS_LEFT, Color.BLACK);
		}
	}

	/**
	 * Returns the state for driving into the specified direction.
	 * 
	 * @param direction
	 *            the direction to get the according state for
	 * @return the state for driving into the direction
	 */
	@Override
	public IState getStateForDirection(Direction direction) {
		switch (direction) {
		case FORWARDS:
			return FOLLOW_LINE;
		case RIGHT:
			return TURN_RIGHT_1;
		case BACKWARDS:
			return TURN_180_1;
		case LEFT:
			return TURN_LEFT_1;
		}
		return FOLLOW_LINE;
	}

	public void setNextButOneDirection(Direction direction) {
		HardwareStateV2.nextButOneDirection = direction;
	}

	public void setIStateEventObserver(IStateEvent iStateEvent) {
		HardwareStateV2.iStateEvent = iStateEvent;
	}
}
