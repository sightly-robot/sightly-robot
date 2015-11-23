package de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller;

import java.awt.Color;
import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

/**
 * PCA9685 LED and Servo Controller. 
 * Is a Singleton.
 * Code converted and functionality added from Python Example. 
 * See: http://4tronix.co.uk/blog/?p=475
 * 
 * @author Lenard Spiecker
 */
public class LEDAndServoController {

	//Singleton
	private static LEDAndServoController instance;
	
	private static final boolean DEBUG = true;

	// Registers/etc.
	private static final int __SUBADR1 = 0x02;
	private static final int __SUBADR2 = 0x03;
	private static final int __SUBADR3 = 0x04;
	private static final int __MODE1 = 0x00;
	private static final int __PRESCALE = 0xFE;
	private static final int __LED0_ON_L = 0x06;
	private static final int __LED0_ON_H = 0x07;
	private static final int __LED0_OFF_L = 0x08;
	private static final int __LED0_OFF_H = 0x09;
	private static final int __ALLLED_ON_L = 0xFA;
	private static final int __ALLLED_ON_H = 0xFB;
	private static final int __ALLLED_OFF_L = 0xFC;
	private static final int __ALLLED_OFF_H = 0xFD;

	private static final int ADDRESS = 0x40;

	private I2CBus bus;
	private I2CDevice device;

	// Define Colour IDs for the RGB LEDs
	private static final int BLUE = 0;
	private static final int GREEN = 1;
	private static final int RED = 2;

	// LEDs
	public static final int LEFT = 0;
	public static final int REAR = 1;
	public static final int RIGHT = 2;
	public static final int FRONT = 3;
	
	//Servos
	public static final int S12 = 0;
	public static final int S13 = 1;
	public static final int S14 = 2;
	public static final int S15 = 3;
	public static final double DEG_CENTER_CORRECTURE[] = new double[]{25,25,25,25};
	
	public static final int PWM_MAX = 4095;
	private static final double FREQUENCY = 50; // HZ
	private static final double SERVO_CENTER_PWM =  PWM_MAX/(1000/FREQUENCY);
	public static final double PWM_PER_DEGREE = SERVO_CENTER_PWM/180;

	/**
	 * Connects to the I2C Device and initializes the frequency.
	 * @throws IOException
	 */
	private LEDAndServoController() throws IOException {
		bus = I2CFactory.getInstance(I2CBus.BUS_1);

		// Init PCA9685
		device = bus.getDevice(ADDRESS);

		// Resetting PCA9685
		device.write(__MODE1, (byte) 0x00);

		setPWMFrequency(FREQUENCY);

		if (DEBUG) {
			System.out.println("I2CRGBLEDController is running!");
		}
	}
	
	/**
	 * Eventually creates an new Instance of {@link LEDAndServoController} and returns it.
	 * @return {@link LEDAndServoController}
	 */
	public static LEDAndServoController getInstance()
	{
		if(instance == null)
		{
			try {
				instance = new LEDAndServoController();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	/**
	 * Sets the PWM Frequency
	 * 
	 * @param frequency
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void setPWMFrequency(double frequency) throws IOException {
		double prescaleval = 25000000.0; // 25MHz
		prescaleval /= 4096.0; // 12-bit
		prescaleval /= frequency;
		prescaleval -= 1.0;

		double prescale = Math.floor(prescaleval + 0.5);
		if (DEBUG) {
			System.out.format("Setting PWM frequency to %f Hz\n", frequency);
			System.out.format("Estimated pre-scale: %f\n", prescaleval);
			System.out.format("Final pre-scale: %f\n", prescale);
		}
		int oldmode = device.read(__MODE1);
		int newmode = (oldmode & 0x7F) | 0x10; // sleep
		device.write(__MODE1, (byte) newmode); // go to sleep
		device.write(__PRESCALE, (byte) Math.floor(prescale));
		device.write(__MODE1, (byte) oldmode);
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		device.write(__MODE1, (byte) (oldmode | 0x80));
	}

	/**
	 * Sets single PWM Channel
	 * 
	 * @param channel
	 * @param on
	 * @param off
	 */
	private void setPWM(int channel, int on, int off) {
		try {
			device.write(__LED0_ON_L + 4 * channel, (byte) (on & 0xFF));
			device.write(__LED0_ON_H + 4 * channel, (byte) (on >> 8));
			device.write(__LED0_OFF_L + 4 * channel, (byte) (off & 0xFF));
			device.write(__LED0_OFF_H + 4 * channel, (byte) (off >> 8));
		} catch (IOException e) {
		}
	}
	
	/**
	 * Control a LED on a Servo connection-
	 * pwm up to 4095
	 * @param servo
	 * @param pwm
	 */
	public void setServoOutputAsLED(int servo, int pwm) {
		setPWM(servo + 12, 0, pwm);
	}
	
	/**
	 * Control a Servo in degrees from -80 (left) to 80 (right) degree. 0 =
	 * Center
	 * 
	 * @param servo
	 * @param grad
	 */
	public void setServo(int servo, double degree) {
		int pwm = (int) (SERVO_CENTER_PWM - (degree + DEG_CENTER_CORRECTURE[servo]) * PWM_PER_DEGREE);
		setPWM(servo + 12, PWM_MAX - pwm, pwm);
	}

	/**
	 * setLED(LED, Red, Green, Blue): Sets the LED specified to required RGB
	 * value. 0 >= LED <= 3; 0 <= R,G,B <= 4095
	 */
	public void setLED(int LED, int red, int green, int blue) {
		setPWM(LED * 3 + RED, 0, red);
		setPWM(LED * 3 + GREEN, 0, green);
		setPWM(LED * 3 + BLUE, 0, blue);
	}

	/**
	 * setLED(LED, Red, Green, Blue): Sets the LED specified to required RGB
	 * value. 0 >= LED <= 3; 0 <= R,G,B <= 4095
	 */
	public void setLED(int LED, Color color) {
		setLED(LED, color.getRed()*16, color.getGreen()*16, color.getBlue()*16);
	}

	/**
	 * setAllLEDs(Red, Green, Blue): Sets all LEDs to required RGB. 0 <= R,G,B
	 * <= 4095
	 * 
	 * @return
	 */
	public void setAllLEDs(int red, int green, int blue) {
		for (int i = 0; i < 4; i++) {
			setLED(i, red, green, blue);
		}
	}

	/**
	 * setAllLEDs(Red, Green, Blue): Sets all LEDs to required RGB. 0 <= R,G,B
	 * <= 4095
	 * 
	 * @return
	 */
	public void setAllLEDs(Color color) {
		setAllLEDs(color.getRed()*16, color.getGreen()*16, color.getBlue()*16);
	}

	/**
	 * Closes the I2C Bus.
	 * 
	 * @throws IOException
	 */
	public void shutdown(){
		setAllLEDs(0, 0, 0);
		try {
			bus.close();
		} catch (IOException e) {
		}
		if (DEBUG) {
			System.out.println("I2CRGBLEDController stopped!");
		}
	}
}
