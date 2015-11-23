package de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;

/**
 * Simplified Version of GpioController for the relevant GPIOs of the Pi2Go.
 * @author Lenard Spiecker
 *
 */
public class Pi2GoGPIOController {

	private static Pi2GoGPIOController instance;
	
//	GPIOs:
	private GpioController gpio;
	private GpioPinDigitalInput rightLineSensor;
	private GpioPinDigitalInput leftLineSensor;
	private GpioPinDigitalInput buttonSensor;
	
//	Pins:
	private static final Pin LINE_RIGHT = RaspiPin.GPIO_03;
	private static final Pin LINE_LEFT = RaspiPin.GPIO_01;
	private static final Pin BUTTON = RaspiPin.GPIO_04;
	
//	Debouncing:
	private static final int DEBOUNCE_MS = 5;
	
	private Pi2GoGPIOController() {
		gpio = GpioFactory.getInstance();
		
		rightLineSensor = gpio.provisionDigitalInputPin(LINE_RIGHT, "lineRight", PinPullResistance.PULL_UP);
		leftLineSensor = gpio.provisionDigitalInputPin(LINE_LEFT, "lineLeft", PinPullResistance.PULL_UP);
		buttonSensor = gpio.provisionDigitalInputPin(BUTTON, "buttonPressed", PinPullResistance.PULL_UP);
		
		//Debouncing:
		rightLineSensor.setDebounce(DEBOUNCE_MS);
		leftLineSensor.setDebounce(DEBOUNCE_MS);
		
		// initialize wiringPi library
		Gpio.wiringPiSetup(); //TODO should this be done (here)?
	}
	
	public static Pi2GoGPIOController getInstance()
	{
		if(instance == null)
		{
			instance = new Pi2GoGPIOController();
		}
		return instance;
	}
	
	public GpioPinDigitalInput getLineLeft() {
		return leftLineSensor;
	}

	public GpioPinDigitalInput getLineRight() {
		return rightLineSensor;
	}
	
	public GpioPinDigitalInput getButton() {
		return buttonSensor;
	}

	public void shutdown()
	{
		leftLineSensor.removeAllListeners();
		rightLineSensor.removeAllListeners();
		buttonSensor.removeAllListeners();
		
		gpio.shutdown();
	}

	public boolean isLineRight() {
		return rightLineSensor.isHigh();
	}

	public boolean isLineLeft() {
		return leftLineSensor.isHigh();
	}

	public boolean isButtonPressed() {
		return buttonSensor.isHigh();
	}



}
