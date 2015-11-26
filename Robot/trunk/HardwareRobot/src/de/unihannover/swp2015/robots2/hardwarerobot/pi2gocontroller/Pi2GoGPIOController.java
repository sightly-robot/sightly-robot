package de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Simplified version of the GpioController for the relevant GPIOs of the Pi2Go.
 * <br>
 * It is not instantiable because it uses the Singleton pattern. Use the
 * instance instead.
 * 
 * @author Lenard Spiecker
 */
public class Pi2GoGPIOController {

	/** The Singleton instance of the GpioController. */
	private static Pi2GoGPIOController instance;

	// GPIOs:
	private GpioController gpio;
	private GpioPinDigitalInput rightLineSensor;
	private GpioPinDigitalInput leftLineSensor;
	private GpioPinDigitalInput buttonSensor;

	// Pins:
	private static final Pin LINE_RIGHT = RaspiPin.GPIO_03;
	private static final Pin LINE_LEFT = RaspiPin.GPIO_01;
	private static final Pin BUTTON = RaspiPin.GPIO_04;

	// Debouncing:
	private static final int DEBOUNCE_MS = 5;

	/**
	 * Initializes the Singleton instance.<br>
	 * Therefore initializes the GPIOs and especially the line sensors.
	 */
	private Pi2GoGPIOController() {
		gpio = GpioFactory.getInstance();

		rightLineSensor = gpio.provisionDigitalInputPin(LINE_RIGHT, "lineRight", PinPullResistance.PULL_UP);
		leftLineSensor = gpio.provisionDigitalInputPin(LINE_LEFT, "lineLeft", PinPullResistance.PULL_UP);
		buttonSensor = gpio.provisionDigitalInputPin(BUTTON, "buttonPressed", PinPullResistance.PULL_UP);

		// Debouncing:
		rightLineSensor.setDebounce(DEBOUNCE_MS);
		leftLineSensor.setDebounce(DEBOUNCE_MS);
	}

	/**
	 * Gets the instance of the Pi2GoGPIOController.<br>
	 * If the instance is {@code null} a new one will be created.
	 * 
	 * @return the GpioController instance
	 */
	public static Pi2GoGPIOController getInstance() {
		if (instance == null) {
			instance = new Pi2GoGPIOController();
		}
		return instance;
	}

	/**
	 * Gets the left line sensor.
	 * 
	 * @return the left line sensor
	 */
	public GpioPinDigitalInput getLineLeft() {
		return leftLineSensor;
	}

	/**
	 * Gets the right line sensor.
	 * 
	 * @return the right line sensor
	 */
	public GpioPinDigitalInput getLineRight() {
		return rightLineSensor;
	}

	/**
	 * Gets the button of the Pi2Go.
	 * 
	 * @return the button of the Pi2Go
	 */
	public GpioPinDigitalInput getButton() {
		return buttonSensor;
	}

	/**
	 * Shuts the Pi2GoGPIOController down by reseting all listeners.
	 */
	public void shutdown() {
		leftLineSensor.removeAllListeners();
		rightLineSensor.removeAllListeners();
		buttonSensor.removeAllListeners();

		gpio.shutdown();
	}

	/**
	 * Returns the {@code isHigh} property of the right line sensor.
	 * 
	 * @return {@code true} if the right line sensor is high, {@code false}
	 *         otherwise
	 */
	public boolean isLineRight() {
		return rightLineSensor.isHigh();
	}

	/**
	 * Returns the {@code isHigh} property of the left line sensor.
	 * 
	 * @return {@code true} if the left line sensor is high, {@code false}
	 *         otherwise
	 */
	public boolean isLineLeft() {
		return leftLineSensor.isHigh();
	}

	/**
	 * Returns the {@code isHigh} property of the button.
	 * 
	 * @return {@code true} if the button is high, {@code false} otherwise
	 */
	public boolean isButtonPressed() {
		return buttonSensor.isHigh();
	}
}
