package de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

/**
 * Singleton MotorController for controlling the speed of two motors.
 * 
 * Code mainly from Prof. Dr. Joel Greenyer.
 * 
 * TODO Speed Funcitonality, Stop function
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

	static final int pinleftFwd = 11;
	static final int pinLeftBwd = 10;
	static final int pinRightFwd = 12;
	static final int pinRightBwd = 13;

	private int rightSpeed;
	private int leftSpeed;

	private EngineController() {
		// initialize wiringPi library
		Gpio.wiringPiSetup();
		
		SoftPwm.softPwmCreate(pinleftFwd, 0, 100);
		SoftPwm.softPwmCreate(pinLeftBwd, 0, 100);
		SoftPwm.softPwmCreate(pinRightFwd, 0, 100);
		SoftPwm.softPwmCreate(pinRightBwd, 0, 100);
	}
	
	public static EngineController getInstance()
	{
		if(instance == null)
		{
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
	 * @param speed
	 *            -100 <= speed <= 100
	 */
	private void setLeftSpeed(int speed) {
		if (speed < -100)
			throw new IllegalArgumentException("Parameter 'speed' must not be smaller than -100.");
		if (speed < -100 || speed > 100)
			throw new IllegalArgumentException("Parameter 'speed' must not be greater than 100.");
		if (speed > 0) {
			SoftPwm.softPwmWrite(pinLeftBwd, 0);
			SoftPwm.softPwmWrite(pinleftFwd, speed);
		} else {
			SoftPwm.softPwmWrite(pinleftFwd, 0);
			SoftPwm.softPwmWrite(pinLeftBwd, -speed);
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
		if (speed < -100)
			throw new IllegalArgumentException("Parameter 'speed' must not be smaller than -100.");
		if (speed < -100 || speed > 100)
			throw new IllegalArgumentException("Parameter 'speed' must not be greater than 100.");
		if (speed > 0) {
			SoftPwm.softPwmWrite(pinRightBwd, 0);
			SoftPwm.softPwmWrite(pinRightFwd, speed);
		} else {
			SoftPwm.softPwmWrite(pinRightFwd, 0);
			SoftPwm.softPwmWrite(pinRightBwd, -speed);
		}
		rightSpeed = speed;
		// System.out.println("set right speed to : " + speed);
	}

	public void shutdown() {
		stop();
	}
}
