package de.unihannover.swp2015.robots2.hardwarerobot.automate;

import java.awt.Color;

import de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller.EngineController;
import de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller.LEDAndServoController;
import de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller.Pi2GoGPIOController;

/**
 * The states of the automate of the Pi2Go controls.
 * 
 * @author Philipp Rohde
 */
public enum State {

	/**
	 * The robot follows a line until he reaches the next junction.
	 */
	FOLLOW_LINE {
		@Override
		public State getNextState() {
			if (GPIO.isLineLeft() && GPIO.isLineRight()) {
				DRIVE_ON_CELL.execute();
				return DRIVE_ON_CELL;
			} else {
				this.execute();
				return this;
			}
		}

		@Override
		protected void execute() {
			boolean left = GPIO.isLineLeft();
			boolean right = GPIO.isLineRight();

			if (!left && !right) {
				setServo(0);
				ENGINE.go(FAST);
			} else if (right) {
				setServo(20);
				ENGINE.go(FAST, SLOW);
			} else if (left) {
				setServo(-20);
				ENGINE.go(SLOW, FAST);
			}
		}

		@Override
		public void start() {
			resetLED();
			setServo(0);
			this.execute();
		}
	},
	/**
	 * The robot drives fully onto the cell.
	 */
	DRIVE_ON_CELL {

		private long startTime;
		private static final double DRIVE_DURATION = 320;

		@Override
		public State getNextState() {
			setProgress(((System.currentTimeMillis() - startTime) / DRIVE_DURATION));
			if (getProgress() >= 1.0) {
				WAIT.execute();
				return WAIT;
			} else {
				this.execute();
				return this;
			}
		}

		@Override
		protected void execute() {
			boolean left = GPIO.isLineLeft();
			boolean right = GPIO.isLineRight();

			if (left && right) {
				ENGINE.go(FAST);
				setServo(0);
			} else if (!right && !left) {
				ENGINE.go(FAST);
				setServo(0);
			} else if (right) {
				ENGINE.go(FAST, SLOW);
				setServo(20);
			} else if (left) {
				ENGINE.go(SLOW, FAST);
				setServo(-20);
			}
		}

		@Override
		public void start() {
			startTime = System.currentTimeMillis();
			setLED(LEDS_REAR, COLOR_BREAK);
			setServo(0);
			this.execute();
		}
	},
	/**
	 * The robot awaits a new command.
	 */
	WAIT {
		@Override
		public State getNextState() {
			this.execute();
			return this;
		}

		@Override
		protected void execute() {
			ENGINE.go(STOP);
		}

		@Override
		public void start() {
			resetLED();
			this.execute();
		}
	},
	/**
	 * First part of turning left.
	 */
	TURN_LEFT_1 {
		@Override
		public State getNextState() {
			if (GPIO.isLineLeft()) {
				this.execute();
				return this;
			} else {
				TURN_LEFT_2.execute();
				return TURN_LEFT_2;
			}
		}

		@Override
		protected void execute() {
			setServo(-50);
			ENGINE.spinRight(TURN);
		}

		@Override
		public void start() {
			setLED(LEDS_LEFT, COLOR_DEFAULT);
			setServo(-50);
			this.execute();
		}
	},
	/**
	 * Second part of turning left.
	 */
	TURN_LEFT_2 {
		@Override
		public State getNextState() {
			if (!GPIO.isLineLeft()) {
				this.execute();
				return this;
			} else {
				FOLLOW_LINE.execute();
				return FOLLOW_LINE;
			}
		}

		@Override
		protected void execute() {
			setServo(-50);
			ENGINE.spinRight(TURN);
		}

		@Override
		public void start() {
			setLED(LEDS_LEFT, COLOR_DEFAULT);
			setServo(-50);
			this.execute();
		}
	},
	/**
	 * First part of turning right.
	 */
	TURN_RIGHT_1 {
		@Override
		public State getNextState() {
			if (GPIO.isLineRight()) {
				this.execute();
				return this;
			} else {
				TURN_RIGHT_2.execute();
				return TURN_RIGHT_2;
			}
		}

		@Override
		protected void execute() {
			setServo(50);
			ENGINE.spinLeft(TURN);
		}

		@Override
		public void start() {
			setLED(LEDS_RIGHT, COLOR_DEFAULT);
			setServo(50);
			this.execute();
		}
	},
	/**
	 * Second part of turning right.
	 */
	TURN_RIGHT_2 {
		@Override
		public State getNextState() {
			if (!GPIO.isLineRight()) {
				this.execute();
				return this;
			} else {
				FOLLOW_LINE.execute();
				return FOLLOW_LINE;
			}
		}

		@Override
		protected void execute() {
			setServo(50);
			ENGINE.spinLeft(TURN);
		}

		@Override
		public void start() {
			setLED(LEDS_RIGHT, COLOR_DEFAULT);
			setServo(50);
			this.execute();
		}
	},
	/**
	 * First part of turning 180 degrees.
	 */
	TURN_180_1 {
		@Override
		public State getNextState() {
			if (GPIO.isLineLeft()) {
				this.execute();
				return this;
			} else {
				TURN_180_2.execute();
				return TURN_180_2;
			}
		}

		@Override
		protected void execute() {
			setServo(-50);
			ENGINE.spinRight(TURN);
		}

		@Override
		public void start() {
			setLED(LEDS_LEFT, COLOR_DEFAULT);
			setLED(LEDS_REAR, COLOR_BREAK);
			setServo(-50);
			this.execute();
		}
	},
	/**
	 * Second part of turning 180 degrees.
	 */
	TURN_180_2 {
		@Override
		public State getNextState() {
			if (!GPIO.isLineLeft()) {
				this.execute();
				return this;
			} else {
				TURN_180_3.execute();
				return TURN_180_3;
			}
		}

		@Override
		protected void execute() {
			setServo(-50);
			ENGINE.spinRight(TURN);
		}

		@Override
		public void start() {
			setLED(LEDS_LEFT, COLOR_DEFAULT);
			setLED(LEDS_REAR, COLOR_BREAK);
			setServo(-50);
			this.execute();
		}
	},
	/**
	 * Third part of turning 180 degrees.
	 */
	TURN_180_3 {
		@Override
		public State getNextState() {
			if (!GPIO.isLineRight()) {
				this.execute();
				return this;
			} else {
				TURN_180_4.execute();
				return TURN_180_4;
			}
		}

		@Override
		protected void execute() {
			setServo(0);
			ENGINE.go(-FAST);
		}

		@Override
		public void start() {
			setLED(LEDS_LEFT, COLOR_DEFAULT);
			setLED(LEDS_REAR, COLOR_BREAK);
			setServo(0);
			this.execute();
		}
	},
	/**
	 * Fourth part of turning 180 degrees.
	 */
	TURN_180_4 {
		@Override
		public State getNextState() {
			if (!GPIO.isLineLeft()) {
				this.execute();
				return this;
			} else {
				FOLLOW_LINE.execute();
				return FOLLOW_LINE;
			}
		}

		@Override
		protected void execute() {
			setServo(-50);
			ENGINE.spinRight(TURN);
		}

		@Override
		public void start() {
			setLED(LEDS_LEFT, COLOR_DEFAULT);
			setLED(LEDS_REAR, COLOR_BREAK);
			setServo(-50);
			this.execute();
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
	private final static EngineController ENGINE = EngineController.getInstance();

	// LEDs
	private final static int LEDS_FRONT = LEDAndServoController.FRONT;
	private final static int LEDS_REAR = LEDAndServoController.REAR;
	private final static int LEDS_LEFT = LEDAndServoController.LEFT;
	private final static int LEDS_RIGHT = LEDAndServoController.RIGHT;

	// LED colors
	private final static Color COLOR_BREAK = Color.RED;
	private final static Color COLOR_DEFAULT = Color.WHITE;

	private double progress = 0.0;

	/**
	 * Gets the next state according to the sensor values.
	 * 
	 * @return the next state of the automate
	 */
	public abstract State getNextState();

	/**
	 * Controls the LEDs and engine according to the current state.
	 */
	protected abstract void execute();

	/**
	 * Gets the progress of the current state.
	 * 
	 * @return 0.0 <= progress <= 1.0
	 */
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
	 * Starts the current state.
	 */
	public abstract void start();

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

}
