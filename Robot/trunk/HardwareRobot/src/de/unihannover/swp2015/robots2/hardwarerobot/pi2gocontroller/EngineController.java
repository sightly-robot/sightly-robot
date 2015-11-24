package de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

/**
 * Singleton MotorController for controlling the speed of two motors.
 * 
 * Code mainly from Prof. Dr. Joel Greenyer.
 * 
 * @author Lenard Spiecker
 *
 */
public class EngineController {

	private static EngineController instance;

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

	private EngineController() {
		// initialize wiringPi library
		Gpio.wiringPiSetup();
		
		SoftPwm.softPwmCreate(PIN_LEFT_FWD, 0, 100);
		SoftPwm.softPwmCreate(PIN_LEFT_BWD, 0, 100);
		SoftPwm.softPwmCreate(PIN_RIGHT_FWD, 0, 100);
		SoftPwm.softPwmCreate(PIN_RIGHT_BWD, 0, 100);
	}
	
	public static EngineController getInstance() {
		if (instance == null) {
			instance = new EngineController();
		}
		return instance;
	}

	public int getRightSpeed() {
		return rightSpeed;
	}

	public int getLeftSpeed() {
		return leftSpeed;
	}

	public void go(int speed) {
		setLeftSpeed(speed);
		setRightSpeed(speed);
	}
	
	/**
	 * Sets the speed of both engines to zero.
	 */
	public void stop() {
		go(0);
	}
	
	public void go(int speedLeft, int speedRight) {
		setLeftSpeed(speedLeft);
		setRightSpeed(speedRight);
	}

	public void spinLeft(int speed) {
		setLeftSpeed(speed);
		setRightSpeed(-speed);
	}

	public void spinRight(int speed) {
		setLeftSpeed(-speed);
		setRightSpeed(speed);
	}

	/**
	 * Sets the speed of the left wheel
	 * 
	 * @param speed -100 <= speed <= 100
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
		}
		else {
			SoftPwm.softPwmWrite(PIN_LEFT_FWD, 0);
			SoftPwm.softPwmWrite(PIN_LEFT_BWD, -speed);
		}
		leftSpeed = speed;
		// System.out.println("set left speed to : " + speed);
	}

	/**
	 * Sets the speed of the right wheel
	 * 
	 * @param speed
	 *            -100 <= speed <= 100
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
		}
		else {
			SoftPwm.softPwmWrite(PIN_RIGHT_FWD, 0);
			SoftPwm.softPwmWrite(PIN_RIGHT_BWD, -speed);
		}
		rightSpeed = speed;
		// System.out.println("set right speed to : " + speed);
	}

	public void shutdown() {
		stop();
	}
}
