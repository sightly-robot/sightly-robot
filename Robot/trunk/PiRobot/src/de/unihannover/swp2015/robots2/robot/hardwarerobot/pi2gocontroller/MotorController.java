package de.unihannover.swp2015.robots2.robot.hardwarerobot.pi2gocontroller;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

/**
 * The MotorController is used for controlling the speed of two motors.<br>
 * It is not instantiable because it uses the Singleton pattern. Use the
 * instance instead.
 * 
 * Code mainly from Prof. Dr. Joel Greenyer.
 * 
 * @author Lenard Spiecker
 */
public class MotorController {

	/** The Singleton instance of the MotorController. */
	private static MotorController instance;

	// L1 = 26 pi4j: 11
	// L2 = 24 pi4j: 10
	// R1 = 19 pi4j: 12
	// R2 = 21 pi4j: 13

	// irFL = 7 pi4j: 7
	// irFR = 11 pi4j: 0
	// lineRight = 13 pi4j: 2
	// lineLeft = 12 pi4j: 1

	// frontLED = 15 pi4j: 3
	// rearLED = 16 pi4j: 4

	// sonar = 8 pi4j: 15

	private static final int PIN_LEFT_FWD = 11;
	private static final int PIN_LEFT_BWD = 10;
	private static final int PIN_RIGHT_FWD = 12;
	private static final int PIN_RIGHT_BWD = 13;

	private int rightSpeed;
	private int leftSpeed;
	
	private boolean servoActivated = true;
	private static final int SERVO = BlinkLEDAndServoController.S15;

	/**
	 * Initializes the Singleton instance.<br>
	 * <br>
	 * Therefore the wiringPi library and the SoftPWM for controlling the
	 * engines are initialized.
	 */
	private MotorController() {
		// initialize wiringPi library
		Gpio.wiringPiSetup();

		SoftPwm.softPwmCreate(PIN_LEFT_FWD, 0, 100);
		SoftPwm.softPwmCreate(PIN_LEFT_BWD, 0, 100);
		SoftPwm.softPwmCreate(PIN_RIGHT_FWD, 0, 100);
		SoftPwm.softPwmCreate(PIN_RIGHT_BWD, 0, 100);
	}

	/**
	 * Gets the instance of the MotorController.<br>
	 * If the instance is {@code null} a new one will be created.
	 * 
	 * @return the MotorController instance
	 */
	public static MotorController getInstance() {
		if (instance == null) {
			instance = new MotorController();
		}
		return instance;
	}

	/**
	 * Gets the current speed of the right wheel.
	 * 
	 * @return -100 <= speed of right wheel <= 100
	 */
	public int getRightSpeed() {
		return rightSpeed;
	}

	/**
	 * Gets the current speed of the left wheel.
	 * 
	 * @return -100 <= speed of left wheel <= 100
	 */
	public int getLeftSpeed() {
		return leftSpeed;
	}

	/**
	 * Sets the speed of both wheels to the specified value.
	 * 
	 * @param speed
	 *            -100 <= speed <= 100
	 */
	public void go(int speed) {
		go(speed,speed);
	}

	/**
	 * Sets the speed of both wheels to the respectively specified value.
	 * 
	 * @param speedLeft
	 *            -100 <= speedLeft <= 100
	 * @param speedRight
	 *            -100 <= speedRight <= 100
	 */
	public void go(int speedLeft, int speedRight) {
		setLeftSpeed(speedLeft);
		setRightSpeed(speedRight);
		updateServo();
	}

	/**
	 * Sets the speed of both wheels to zero, so the Pi2Go stops.
	 */
	public void stop() {
		go(0);
	}

	/**
	 * Sets the speed of the left wheel to the specified value and the speed of
	 * the right wheel to its negative value, that is the Pi2Go spins left if
	 * speed was positive.
	 * 
	 * @param speed
	 *            -100 <= speed <= 100
	 */
	public void spinLeft(int speed) {
		go(speed,-speed);
	}

	/**
	 * Sets the speed of the right wheel to the specified value and the speed of
	 * the left wheel to its negative value, that is the Pi2Go spins right if
	 * speed was positive.
	 * 
	 * @param speed
	 *            -100 <= speed <= 100
	 */
	public void spinRight(int speed) {
		go(-speed,speed);
	}

	/**
	 * Sets the speed of the left wheel.
	 * 
	 * @param speed
	 *            -100 <= speed <= 100
	 * @throws IllegalArgumentException
	 *             thrown if the argument is not in the specified range
	 */
	private void setLeftSpeed(int speed) {
		if (speed < -100) {
			throw new IllegalArgumentException("Parameter 'speed' must not be smaller than -100.");
		}
		if (speed < -100 || speed > 100) {
			throw new IllegalArgumentException("Parameter 'speed' must not be greater than 100.");
		}
		if (speed > 0) {
			SoftPwm.softPwmWrite(PIN_LEFT_BWD, 0);
			SoftPwm.softPwmWrite(PIN_LEFT_FWD, speed);
		} else {
			SoftPwm.softPwmWrite(PIN_LEFT_FWD, 0);
			SoftPwm.softPwmWrite(PIN_LEFT_BWD, -speed);
		}
		leftSpeed = speed;
		// System.out.println("set left speed to : " + speed);
	}

	/**
	 * Sets the speed of the right wheel.
	 * 
	 * @param speed
	 *            -100 <= speed <= 100
	 * @throws IllegalArgumentException
	 *             thrown if the argument is not in the specified range
	 */
	private void setRightSpeed(int speed) throws IllegalArgumentException {
		if (speed < -100) {
			throw new IllegalArgumentException("Parameter 'speed' must not be smaller than -100.");
		}
		if (speed < -100 || speed > 100) {
			throw new IllegalArgumentException("Parameter 'speed' must not be greater than 100.");
		}
		if (speed > 0) {
			SoftPwm.softPwmWrite(PIN_RIGHT_BWD, 0);
			SoftPwm.softPwmWrite(PIN_RIGHT_FWD, speed);
		} else {
			SoftPwm.softPwmWrite(PIN_RIGHT_FWD, 0);
			SoftPwm.softPwmWrite(PIN_RIGHT_BWD, -speed);
		}
		rightSpeed = speed;
		// System.out.println("set right speed to : " + speed);
	}
	
	private void updateServo()
	{
		if(servoActivated)
		{
			double degree = Math.max(Math.min((leftSpeed-rightSpeed),50),-50);
			BlinkLEDAndServoController.getInstance().setServo(SERVO, degree);
		}
	}

	/**
	 * Processes a shutdown of the engine controller.
	 */
	public void shutdown() {
		stop();
	}

	public boolean isServoActivated() {
		return servoActivated;
	}

	public void setServoActivated(boolean servoActivated) {
		this.servoActivated = servoActivated;
	}
}
