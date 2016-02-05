package de.sightly_robot.sightly_robot.robot.hardwarerobot.pi2gocontroller;

import java.awt.Color;
import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

/**
 * The LEDAndServoController is a controller class for the PCA9685 LED and Servo
 * Controller of the Pi2Go.<br>
 * It is not instantiable because it uses the Singleton pattern. Use the
 * instance instead.<br>
 * <br>
 * Code converted and functionality added from Python example. See:
 * http://4tronix.co.uk/blog/?p=475
 * 
 * @author Lenard Spiecker
 */
public class LEDAndServoController {

	/** The Singleton instance of the LEDAndServoController. */
	protected static LEDAndServoController instance;

	private static final boolean DEBUG = false;

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
	protected static final int BLUE = 0;
	protected static final int GREEN = 1;
	protected static final int RED = 2;

	// LEDs
	public static final int LEFT = 0;
	public static final int REAR = 1;
	public static final int RIGHT = 2;
	public static final int FRONT = 3;

	// Servos
	public static final int S12 = 0;
	public static final int S13 = 1;
	public static final int S14 = 2;
	public static final int S15 = 3;
	public static final double DEG_CENTER_CORRECTURE[] = new double[] { 25, 25, 25, 25 };

	public static final int PWM_MAX = 4095;
	private static final double FREQUENCY = 50; // HZ
	private static final double SERVO_CENTER_PWM = PWM_MAX / (1000 / FREQUENCY);
	public static final double PWM_PER_DEGREE = SERVO_CENTER_PWM / 180;

	// AccentColor
	private Color accentColor = Color.MAGENTA;

	/**
	 * Initializes the Singleton instance.<br>
	 * Therefore connects to the I2C Device and initializes the frequency.
	 * 
	 * @throws IOException
	 *             thrown if the I2C Device connection or initialization failed
	 */
	protected LEDAndServoController() throws IOException {
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
	 * Gets the instance of the LEDAndServoController.<br>
	 * If the instance is {@code null} a new one will be created.
	 * 
	 * @return the LEDAndServoController instance
	 */
	public static LEDAndServoController getInstance() {
		if (instance == null) {
			try {
				instance = new LEDAndServoController();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	/**
	 * Sets the PWM frequency.
	 * 
	 * @param frequency
	 *            the PWM frequency to set
	 * @throws IOException
	 *             thrown if device not available or writable
	 * @throws InterruptedException
	 *             thrown if sleep is interrupted
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
	 * Sets single PWM channel.
	 * 
	 * @param channel
	 *            the PWM channel to set
	 * @param on
	 *            the value to set for high
	 * @param off
	 *            the value to set for low
	 */
	protected void setPWM(int channel, int on, int off) {
		try {
			device.write(__LED0_ON_L + 4 * channel, (byte) (on & 0xFF));
			device.write(__LED0_ON_H + 4 * channel, (byte) (on >> 8));
			device.write(__LED0_OFF_L + 4 * channel, (byte) (off & 0xFF));
			device.write(__LED0_OFF_H + 4 * channel, (byte) (off >> 8));
		} catch (IOException e) {
		}
	}

	/**
	 * Control a LED on a Servo connection.
	 * 
	 * @param servo
	 *            the Servo channel to set
	 * @param pwm
	 *            pwm <= 4095
	 */
	public void setServoOutputAsLED(int servo, int pwm) {
		setPWM(servo + 12, 0, pwm);
	}

	/**
	 * Sets the specified Servo to the specified degrees.
	 * 
	 * @param servo
	 *            the Servo to control
	 * @param degree
	 *            -80 (left) <= degree <= 80 (right)
	 */
	public void setServo(int servo, double degree) {
		int pwm = (int) (SERVO_CENTER_PWM - (degree + DEG_CENTER_CORRECTURE[servo]) * PWM_PER_DEGREE);
		setPWM(servo + 12, PWM_MAX - pwm, pwm);
	}

	/**
	 * Sets the specified LED to the specified RGB value.
	 * 
	 * @param LED
	 *            the LED to color
	 * @param red
	 *            0 <= red <= 4095
	 * @param green
	 *            0 <= green <= 4095
	 * @param blue
	 *            0 <= blue <= 4095
	 */
	protected void setLED(int LED, int red, int green, int blue) {
		setPWM(LED * 3 + RED, 0, red);
		setPWM(LED * 3 + GREEN, 0, green);
		setPWM(LED * 3 + BLUE, 0, blue);
	}

	/**
	 * Sets the specified LED to the specified {@link Color}.
	 * 
	 * @param LED
	 *            the LED to color
	 * @param color
	 *            the color to set
	 */
	public void setLED(int LED, Color color) {
		setLED(LED, color.getRed() * 16, color.getGreen() * 16, color.getBlue() * 16);
	}

	/**
	 * Sets all LEDs to the specified RGB value.
	 * 
	 * @param red
	 *            0 <= red <= 4095
	 * @param green
	 *            0 <= green <= 4095
	 * @param blue
	 *            0 <= blue <= 4095
	 */
	public void setAllLEDs(int red, int green, int blue) {
		for (int i = 0; i < 4; i++) {
			setLED(i, red, green, blue);
		}
	}

	/**
	 * Sets all LEDs to the specified {@link Color}.
	 * 
	 * @param color
	 *            the color to set
	 */
	public void setAllLEDs(Color color) {
		setAllLEDs(color.getRed() * 16, color.getGreen() * 16, color.getBlue() * 16);
	}

	/**
	 * Closes the I2C bus and resets all LEDs.
	 * 
	 * @throws IOException
	 *             thrown if the bus could not be closed.
	 */
	public void shutdown() {
		setAllLEDs(0, 0, 0);
		try {
			bus.close();
		} catch (IOException e) {
			// only catch
		}
		if (DEBUG) {
			System.out.println("I2CRGBLEDController stopped!");
		}
	}

	public Color getAccentColor() {
		return accentColor;
	}

	public void setAccentColor(Color accentColor) {
		this.accentColor = accentColor;
	}
}
