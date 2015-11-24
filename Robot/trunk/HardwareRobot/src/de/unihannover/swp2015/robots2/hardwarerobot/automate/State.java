package de.unihannover.swp2015.robots2.hardwarerobot.automate;

import de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller.LEDAndServoController;
import de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller.MotorController;
import de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller.Pi2GoGPIOController;

/**
 * The states of the automate of the Pi2Go controls.
 * 
 * @author Philipp Rohde
 *
 */
public enum State {
	
	/** The robot pauses. */
	PAUSE_STATE(-1) {
		@Override
		public State getNextState() {
			// TODO
			this.executeState();
			return this;
		}

		@Override
		protected void executeState() {
			ENGINE.go(STOP);
		}
	},
	/** The robot follows a line until he reaches the next junction. */
	LINE_FOLLOW_STATE(0) {
		@Override
		public State getNextState() {
			if (GPIO.isLineLeft() && GPIO.isLineRight()) {
				DRIVE_ON_CELL_STATE.executeState();
				return DRIVE_ON_CELL_STATE;
			}
			else {
				this.executeState();
				return this;
			}
		}

		@Override
		protected void executeState() {
			boolean left = GPIO.isLineLeft();
			boolean right = GPIO.isLineRight();
			
			if (!left && !right) {
				LEDS.setServo(LEDAndServoController.S15, 0);
				ENGINE.go(FAST);
			}
			else if (right) {
				LEDS.setServo(LEDAndServoController.S15, 20);
				ENGINE.setLeftSpeed(FAST);
				ENGINE.setRightSpeed(SLOW);
			}
			else if (left) {
				LEDS.setServo(LEDAndServoController.S15, -20);
				ENGINE.setLeftSpeed(SLOW);
				ENGINE.setRightSpeed(FAST);				
			}
		}
	},
	/** The robot drives fully onto the cell. */
	DRIVE_ON_CELL_STATE(1) {
		@Override
		public State getNextState() {
			// TODO timer
			this.executeState();
			return this;
		}

		@Override
		protected void executeState() {
			boolean left = GPIO.isLineLeft();
			boolean right = GPIO.isLineRight();
			
			if (left && right) {
				ENGINE.go(FAST);
				LEDS.setServo(LEDAndServoController.S15, 0);
			}
			else if (!right && !left) {
				ENGINE.go(FAST);
				LEDS.setServo(LEDAndServoController.S15, 0);
			}
			else if (right) {
				ENGINE.setLeftSpeed(FAST);
				ENGINE.setRightSpeed(SLOW);
				LEDS.setServo(LEDAndServoController.S15, 20);
			}
			else if (left) {
				ENGINE.setLeftSpeed(SLOW);
				ENGINE.setRightSpeed(FAST);
				LEDS.setServo(LEDAndServoController.S15, -20);
			}			
		}
	},
	/** The robot awaits a new command. */
	WAIT_STATE(2) {
		@Override
		public State getNextState() {
			this.executeState();
			return this;
		}

		@Override
		protected void executeState() {
			ENGINE.go(STOP);
		}
	},
	/** First part of turning left. */
	TURN_LEFT_1_STATE(3) {
		@Override
		public State getNextState() {
			if (GPIO.isLineLeft()) {
				this.executeState();
				return this;
			}
			else {
				TURN_LEFT_2_STATE.executeState();
				return TURN_LEFT_2_STATE;
			}
		}

		@Override
		protected void executeState() {
			LEDS.setServo(LEDAndServoController.S15, -50);
			ENGINE.spinRight(TURN);
		}
	},
	/** Second part of turning left. */
	TURN_LEFT_2_STATE(4) {
		@Override
		public State getNextState() {
			if (!GPIO.isLineLeft()) {
				this.executeState();
				return this;
			}
			else {
				LINE_FOLLOW_STATE.executeState();
				return LINE_FOLLOW_STATE;
			}
		}

		@Override
		protected void executeState() {
			LEDS.setServo(LEDAndServoController.S15, -50);
			ENGINE.spinRight(TURN);
		}
	},
	/** First part of turning right. */
	TURN_RIGHT_1_STATE(5) {
		@Override
		public State getNextState() {
			if (GPIO.isLineRight()) {
				this.executeState();
				return this;
			}
			else {
				TURN_RIGHT_2_STATE.executeState();
				return TURN_RIGHT_2_STATE;
			}
		}

		@Override
		protected void executeState() {
			LEDS.setServo(LEDAndServoController.S15, 50);
			ENGINE.spinLeft(TURN);
		}
	},
	/** Second part of turning right. */
	TURN_RIGHT_2_STATE(6) {
		@Override
		public State getNextState() {
			if (!GPIO.isLineRight()) {
				this.executeState();
				return this;
			}
			else {
				LINE_FOLLOW_STATE.executeState();
				return LINE_FOLLOW_STATE;
			}
		}

		@Override
		protected void executeState() {
			LEDS.setServo(LEDAndServoController.S15, 50);
			ENGINE.spinLeft(TURN);
		}
	},
	/** First part of turning 180 degrees. */
	TURN_180_1_STATE(7) {
		@Override
		public State getNextState() {
			if (GPIO.isLineLeft()) {
				this.executeState();
				return this;
			}
			else {
				TURN_180_2_STATE.executeState();
				return TURN_180_2_STATE;
			}
		}

		@Override
		protected void executeState() {
			LEDS.setServo(LEDAndServoController.S15, -50);
			ENGINE.spinRight(TURN);
		}
	},
	/** Second part of turning 180 degrees. */
	TURN_180_2_STATE(8) {
		@Override
		public State getNextState() {
			if (!GPIO.isLineLeft()) {
				this.executeState();
				return this;
			}
			else {
				TURN_180_3_STATE.executeState();
				return TURN_180_3_STATE;
			}
		}

		@Override
		protected void executeState() {
			LEDS.setServo(LEDAndServoController.S15, -50);
			ENGINE.spinRight(TURN);
		}
	},
	/** Third part of turning 180 degrees. */
	TURN_180_3_STATE(9) {
		@Override
		public State getNextState() {
			if (!GPIO.isLineRight()) {
				this.executeState();
				return this;
			}
			else {
				TURN_180_4_STATE.executeState();
				return TURN_180_4_STATE;
			}
		}

		@Override
		protected void executeState() {
			LEDS.setServo(LEDAndServoController.S15, 0);
			ENGINE.go(-FAST);
		}
	},
	/** Fourth part of turning 180 degrees. */
	TURN_180_4_STATE(10) {
		@Override
		public State getNextState() {
			if (!GPIO.isLineLeft()) {
				this.executeState();
				return this;
			}
			else {
				LINE_FOLLOW_STATE.executeState();
				return LINE_FOLLOW_STATE;
			}
		}

		@Override
		protected void executeState() {
			LEDS.setServo(LEDAndServoController.S15, -50);
			ENGINE.spinRight(TURN);
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
	private final static MotorController ENGINE = MotorController.getInstance();
	
	public int id = -1;
	
	private State(int id) {
		this.id = id;
	}

	/**
	 * Gets the next state according to the sensor values.
	 * 
	 * @return the next state of the automate
	 */
	public abstract State getNextState();
	
	/**
	 * Controls the LEDs and engine according to the current state.
	 */
	protected abstract void executeState();
	
}
