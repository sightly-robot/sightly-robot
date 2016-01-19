package de.unihannover.swp2015.robots2.robot.hardwarerobot.automate;

import java.awt.Color;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.robot.abstractrobot.Direction;
import de.unihannover.swp2015.robots2.robot.abstractrobot.automate.IState;
import de.unihannover.swp2015.robots2.robot.abstractrobot.automate.IStateEvent;
import de.unihannover.swp2015.robots2.robot.hardwarerobot.pi2gocontroller.LEDAndServoController;
import de.unihannover.swp2015.robots2.robot.hardwarerobot.pi2gocontroller.MotorController;
import de.unihannover.swp2015.robots2.robot.hardwarerobot.pi2gocontroller.Pi2GoGPIOController;

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
		public void start() {
			super.start();
			this.execute();
		}

		@Override
		public IState execute() {
			// LEDS:
			boolean blink = (System.currentTimeMillis() - startTime) % 800 < 400;
			switch (nextButOneDirection) {
			case LEFT:
				setCarLeds(false, blink);
				break;
			case RIGHT:
				setCarLeds(blink, false);
				break;
			case BACKWARDS:
				setCarLeds(blink, blink);
			case FORWARDS:
				setCarLeds(false, false);
				break;
			}

			if (System.currentTimeMillis() - startTime > 5000) {
				if(iStateEvent != null)
					iStateEvent.iStateErrorOccured();
				return DISABLED;
			}
			boolean left = GPIO.isLineLeft();
			boolean right = GPIO.isLineRight();

			if (left && right) {
				measure();
				return DRIVE_ON_CELL;
			} else if (!left && !right) {
				setServo(0);
				MOTORS.go(calcProgress() < 0.7 ? FASTER : NORMAL);
			} else if (right) {
				setServo(20);
				MOTORS.go(FAST, SLOW);
			} else if (left) {
				setServo(-20);
				MOTORS.go(SLOW, FAST);
			}
			return this;
		}
	},
	/**
	 * The robot drives fully onto the cell.
	 */
	DRIVE_ON_CELL {

		private static final double DRIVE_DURATION = 310.0;

		@Override
		public void start() {
			super.start();
			this.execute();
		}

		@Override
		public IState execute() {
			// LEDS:
			boolean blink = (System.currentTimeMillis() - startTime) % 800 < 400;
			switch (nextButOneDirection) {
			case LEFT:
				setCarLeds(false, blink);
				break;
			case RIGHT:
				setCarLeds(blink, false);
				break;
			case BACKWARDS:
				setCarLeds(blink, blink);
			case FORWARDS:
				setCarLeds(false, false);
				break;
			}

			// MOTOR:
			boolean left = GPIO.isLineLeft();
			boolean right = GPIO.isLineRight();

			long runningTime = System.currentTimeMillis() - startTime;
			if (runningTime >= DRIVE_DURATION) {
				return WAIT;
			} else if (left && right) {
				MOTORS.go(FAST);
				setServo(0);
			} else if (!right && !left) {
				MOTORS.go(FAST);
				setServo(0);
			} else if (right) {
				MOTORS.go(FAST, SLOW);
				setServo(20);
			} else if (left) {
				MOTORS.go(SLOW, FAST);
				setServo(-20);
			}
			return this;
		}
	},
	/**
	 * The robot is in setupState.
	 */
	SETUP {
		@Override
		public void start() {
			super.start();
			MOTORS.go(STOP);
			this.execute();
		}

		@Override
		public IState execute() {
			double i = 0.85 + ((System.currentTimeMillis() - startTime) % 4000 - 2000) / 14000.0;
			LEDS.setAllLEDs(new Color((int) (LEDS.getAccentColor().getRed() * i),
					(int) (LEDS.getAccentColor().getGreen() * i), (int) (LEDS.getAccentColor().getBlue() * i)));
			return this;
		}
	},
	/**
	 * The robot is disabled
	 */
	DISABLED {
		@Override
		public void start() {
			super.start();
			MOTORS.go(STOP);
			this.execute();
		}

		@Override
		public IState execute() {
			LEDS.setAllLEDs((System.currentTimeMillis() - startTime) % 500 < 250 ? LEDS.getAccentColor() : Color.WHITE);
			return this;
		}
	},
	/**
	 * The robot awaits a new command.
	 */
	WAIT {
		@Override
		public void start() {
			super.start();
			setCarLeds(false, false);
			MOTORS.go(STOP);
			this.execute();
		}

		@Override
		public IState execute() {
			return this;
		}
	},
	/**
	 * The robot awaits a new command.
	 */
	CONNECTED {
		@Override
		public void start() {
			super.start();
			MOTORS.go(STOP);
			this.execute();
		}

		@Override
		public IState execute() {
			double i = 0.25 + ((System.currentTimeMillis() - startTime) % 4000 - 2000) / 14000.0;
			LEDS.setAllLEDs(new Color((int) (LEDS.getAccentColor().getRed() * i),
					(int) (LEDS.getAccentColor().getGreen() * i), (int) (LEDS.getAccentColor().getBlue() * i)));
			return this;
		}
	},
	/**
	 * First part of turning left.
	 */
	TURN_LEFT_1 {
		@Override
		public void start() {
			super.start();
			setCarLeds(false, true);
			LEDS.setLED(LEDS_LEFT, COLOR_BLINK);
			this.execute();
		}

		@Override
		public IState execute() {
			if (GPIO.isLineLeft()) {
				setServo(-50);
				MOTORS.spinRight(TURN);
				return this;
			} else {
				return TURN_LEFT_2;
			}
		}
	},
	/**
	 * Second part of turning left.
	 */
	TURN_LEFT_2 {
		@Override
		public void start() {
			super.start();
			setCarLeds(false, true);
			this.execute();
		}

		@Override
		public IState execute() {
			if (!GPIO.isLineLeft()) {
				setServo(-50);
				MOTORS.spinRight(TURN);
				return this;
			} else {
				return FOLLOW_LINE;
			}
		}
	},
	/**
	 * First part of turning right.
	 */
	TURN_RIGHT_1 {
		@Override
		public void start() {
			super.start();
			setCarLeds(true, false);
			this.execute();
		}

		@Override
		public IState execute() {
			if (GPIO.isLineRight()) {
				setServo(50);
				MOTORS.spinLeft(TURN);
				return this;
			} else {
				return TURN_RIGHT_2;
			}
		}
	},
	/**
	 * Second part of turning right.
	 */
	TURN_RIGHT_2 {
		@Override
		public void start() {
			super.start();
			setCarLeds(true, false);
			this.execute();
		}

		@Override
		public IState execute() {
			if (!GPIO.isLineRight()) {
				setServo(50);
				MOTORS.spinLeft(TURN);
				return this;
			} else {
				return FOLLOW_LINE;
			}
		}
	},
	/**
	 * First part of turning 180 degrees.
	 */
	TURN_180_1 {
		@Override
		public void start() {
			super.start();
			setCarLeds(false, true);
			this.execute();
		}

		@Override
		public IState execute() {
			if (GPIO.isLineLeft()) {
				setServo(-50);
				MOTORS.spinRight(TURN);
				return this;
			} else {
				return TURN_180_2;
			}
		}
	},
	/**
	 * Second part of turning 180 degrees.
	 */
	TURN_180_2 {
		@Override
		public void start() {
			super.start();
			setCarLeds(false, true);
			this.execute();
		}

		@Override
		public IState execute() {
			if (!GPIO.isLineLeft()) {
				setServo(-50);
				MOTORS.spinRight(TURN);
				return this;
			} else {
				return TURN_180_3;
			}
		}
	},
	/**
	 * Third part of turning 180 degrees.
	 */
	TURN_180_3 {
		@Override
		public void start() {
			super.start();
			setCarLeds(true, true);
			this.execute();
		}

		@Override
		public IState execute() {
			if (!GPIO.isLineRight()) {
				setServo(0);
				MOTORS.go(-FAST);
				return this;
			} else {
				return TURN_180_4;
			}
		}
	},
	/**
	 * Fourth part of turning 180 degrees.
	 */
	TURN_180_4 {
		@Override
		public void start() {
			super.start();
			setCarLeds(false, true);
			this.execute();
		}

		@Override
		public IState execute() {
			if (!GPIO.isLineLeft()) {
				setServo(-50);
				MOTORS.spinRight(TURN);
				return this;
			} else {
				return FOLLOW_LINE;
			}
		}
	};

	/** LOGGER: */
	private static Logger LOGGER = LogManager.getLogger(HardwareStateV2.class.getName());
	// speed configuration
	private static final int FASTER = 60;
	private static final int FAST = 40;
	private static final int NORMAL = 30;
	private static final int TURN = 20;
	private static final int SLOW = 15;
	private static final int STOP = 0;

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
	private static final Color COLOR_BLINK = Color.ORANGE;

	private static long[] measurements = new long[] { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 };
	protected static long startTime = 0;
	protected static Direction nextButOneDirection;
	private static IStateEvent iStateEvent;

	/**
	 * Should be called at start to reset the measurements.
	 */
	@Override
	public void start() {
		LOGGER.trace("Start State " + toString());
		if (!isDriving())
			nextButOneDirection = Direction.FORWARDS;
		startTime = System.currentTimeMillis();
	}

	/**
	 * Should be called at end of an State Lifecircle
	 */
	protected void measure() {
		measurements[this.ordinal()] = (long) (measurements[this.ordinal()] * 0.8
				+ 0.2 * (System.currentTimeMillis() - startTime));
	}

	/**
	 * Can be called while executing to calculate the current measured Progress.
	 * 
	 * @return progress double 0 to 1
	 */
	protected double calcProgress() {
		return Math.min(((double) (System.currentTimeMillis() - startTime)) / measurements[this.ordinal()], 1);
	}

	/**
	 * Returns whether the current state is the wait state or not.
	 * 
	 * @return {@code true} if the current state is the wait state,
	 *         {@code false} otherwise
	 */
	@Override
	public boolean isWait() {
		return this == WAIT; // TODO || this == SETUP || this == DISABLED
	}

	@Override
	public boolean isDriving() {
		return this != WAIT && this != SETUP && this != DISABLED && this != CONNECTED;
	}

	/**
	 * Sets the servo to the specified degree.
	 * 
	 * @param degree
	 *            the degrees to set the servo to
	 */
	protected void setServo(double degree) {
		LEDS.setServo(LEDAndServoController.S15, degree);
	}

	/**
	 * Sets the Car Led style.
	 */
	protected void setCarLeds(boolean right, boolean left) {
		LEDS.setLED(LEDS_FRONT, COLOR_FRONT);
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
