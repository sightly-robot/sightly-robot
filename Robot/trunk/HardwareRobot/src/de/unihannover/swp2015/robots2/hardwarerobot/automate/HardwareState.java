package de.unihannover.swp2015.robots2.hardwarerobot.automate;

import java.awt.Color;

import de.unihannover.swp2015.robots2.abstractrobot.Direction;
import de.unihannover.swp2015.robots2.abstractrobot.automate.IState;
import de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller.LEDAndServoController;
import de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller.MotorController;
import de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller.Pi2GoGPIOController;

/**
 * The states of the control automate of a hardware robot (Pi2Go).
 * 
 * @author Philipp Rohde
 */
public enum HardwareState implements IState {

	/**
	 * The robot follows a line until he reaches the next junction.
	 */
	FOLLOW_LINE {
		@Override
		public void start() {
			resetLED();
			setServo(0);
			this.execute();
		}

		@Override
		public IState execute() {
			boolean left = GPIO.isLineLeft();
			boolean right = GPIO.isLineRight();

			if (left && right) {
				return DRIVE_ON_CELL;
			} else if (!left && !right) {
				setServo(0);
				MOTORS.go(FAST);
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
			setLED(LEDS_REAR, COLOR_BREAK);
			setServo(0);
			setProgress(0.0);
			startTime = System.currentTimeMillis();
			this.execute();
		}

		@Override
		public IState execute() {
			boolean left = GPIO.isLineLeft();
			boolean right = GPIO.isLineRight();

			setProgress((System.currentTimeMillis() - startTime) / DRIVE_DURATION);
			if (getProgress() >= 1.0) {
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
	 * The robot awaits a new command.
	 */
	WAIT {
		@Override
		public void start() {
			resetLED();
			this.execute();
		}

		@Override
		public IState execute() {
			MOTORS.go(STOP);
			return this;
		}
	},
	/**
	 * First part of turning left.
	 */
	TURN_LEFT_1 {
		@Override
		public void start() {
			setLED(LEDS_LEFT, COLOR_DEFAULT);
			setServo(-50);
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
			setLED(LEDS_LEFT, COLOR_DEFAULT);
			setServo(-50);
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
			setLED(LEDS_RIGHT, COLOR_DEFAULT);
			setServo(50);
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
			setLED(LEDS_RIGHT, COLOR_DEFAULT);
			setServo(50);
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
			setLED(LEDS_LEFT, COLOR_DEFAULT);
			setLED(LEDS_REAR, COLOR_BREAK);
			setServo(-50);
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
			setLED(LEDS_LEFT, COLOR_DEFAULT);
			setLED(LEDS_REAR, COLOR_BREAK);
			setServo(-50);
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
			setLED(LEDS_LEFT, COLOR_DEFAULT);
			setLED(LEDS_REAR, COLOR_BREAK);
			setServo(-50);
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
			setLED(LEDS_LEFT, COLOR_DEFAULT);
			setLED(LEDS_REAR, COLOR_BREAK);
			setServo(-50);
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

	// speed configuration
	private final static int FAST = 40;
	private final static int TURN = 20;
	private final static int SLOW = 15;
	private final static int STOP = 0;

	// Pi2GO controller
	private final static LEDAndServoController LEDS = LEDAndServoController.getInstance();
	private final static Pi2GoGPIOController GPIO = Pi2GoGPIOController.getInstance();
	private final static MotorController MOTORS = MotorController.getInstance();

	// LEDs
	private final static int LEDS_FRONT = LEDAndServoController.FRONT;
	private final static int LEDS_REAR = LEDAndServoController.REAR;
	private final static int LEDS_LEFT = LEDAndServoController.LEFT;
	private final static int LEDS_RIGHT = LEDAndServoController.RIGHT;

	// LED colors
	private final static Color COLOR_BREAK = Color.RED;
	private final static Color COLOR_DEFAULT = Color.WHITE;

	protected long startTime;
	private double progress = 0.0;

	@Override
	public boolean isWait() {
		return this == WAIT;
	}

	/**
	 * Gets the progress of the current state.
	 * 
	 * @return 0.0 <= progress <= 1.0
	 */
	@Override
	public double getProgress() {
		if (progress > 1.0) {
			progress = 1.0;
		}
		return progress;
	}

	/**
	 * Sets the progress of the current state.
	 * 
	 * @param progress
	 *            0.0 <= progress
	 */
	protected void setProgress(double progress) {
		this.progress = progress;
	}

	/**
	 * Resets all LEDs to black.
	 */
	protected void resetLED() {
		LEDS.setAllLEDs(Color.BLACK);
		// LEDS.setLED(LEDS_FRONT, Color.WHITE);
	}

	/**
	 * Sets the specified LEDs to the specified color.
	 * 
	 * @param led
	 *            the LEDs to color
	 * @param color
	 *            the color of the LEDs
	 */
	protected void setLED(int led, Color color) {
		resetLED();
		LEDS.setLED(led, color);
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
	 * Returns the state for driving into the specified direction.
	 * 
	 * @param direction
	 *            the direction to get the according state for
	 * @return the state for driving into the direction
	 */
	@Override
	public IState getStateForDirection(Direction direction) {
		switch (direction) {
		case FOREWARD:
			return FOLLOW_LINE;
		case RIGHT:
			return TURN_RIGHT_1;
		case BACKWARD:
			return TURN_180_1;
		case LEFT:
			return TURN_LEFT_1;
		}
		return FOLLOW_LINE;
	}
}
