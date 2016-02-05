package de.sightly_robot.sightly_robot.robot.hardwarerobot.pi2gocontroller;

import java.awt.Color;
import java.io.IOException;

/**
 * The BlinkLEDAndServoController extends the {@link LEDAndServoController} with
 * the capability of blinking.
 * 
 * @author Lenard Spiecker
 */
public class BlinkLEDAndServoController extends LEDAndServoController {

	private boolean blinking = false;
	private long blinkUntil = 0;
	private static final long BLINK_MIN_DURATION = 2000;

	/**
	 * Initializes the Singleton instance.<br>
	 * 
	 * @throws IOException
	 *             thrown if the I2C Device connection or initialization failed
	 */
	private BlinkLEDAndServoController() throws IOException {
		super();
	}

	/**
	 * Gets the instance of the BlinkLEDAndServoController.<br>
	 * If the instance is {@code null} a new one will be created.
	 * 
	 * @return the BlinkLEDAndServoController instance as LEDAndServoController
	 */
	public static LEDAndServoController getInstance() {
		if (instance == null) {
			try {
				instance = new BlinkLEDAndServoController();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	/**
	 * Starts blinking in the specified color.
	 * 
	 * @param color
	 *            the color to use for the blinking
	 */
	public void startBlinking(final Color color) {
		blinkUntil = System.currentTimeMillis() + BLINK_MIN_DURATION;
		if (!blinking) {
			blinking = true;
			new Thread() {
				int currentBlink = 0;
				static final int STEPS_PER_HALFSECOND = 15;
				Color c;

				public void run() {
					try {
						// Safety delay
						sleep(50);
						while (System.currentTimeMillis() < blinkUntil || currentBlink < 3) {
							for (double i = 0; i < 1; i += 1.0 / STEPS_PER_HALFSECOND) {
								c = new Color((int) (color.getRed() * i), (int) (color.getGreen() * i),
										(int) (color.getBlue() * i));
								setBlinkingLEDs(c);
								sleep(33);
							}
							for (double i = 1; i >= 0; i -= 1.0 / STEPS_PER_HALFSECOND) {
								c = new Color((int) (color.getRed() * i), (int) (color.getGreen() * i),
										(int) (color.getBlue() * i));
								setBlinkingLEDs(c);
								sleep(33);
							}
							currentBlink++;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					blinking = false;
				};
			}.start();
		}
	}

	/**
	 * Sets all LEDs to the specified {@link Color}.
	 * 
	 * @param color
	 *            the color to set
	 */
	private void setBlinkingLEDs(Color color) {
		setBlinkingLEDs(color.getRed() * 16, color.getGreen() * 16, color.getBlue() * 16);
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
	private void setBlinkingLEDs(int red, int green, int blue) {
		for (int i = 0; i < 4; i++) {
			super.setLED(i, red, green, blue);
		}
	}

	@Override
	protected void setLED(int LED, int red, int green, int blue) {
		if (!blinking) {
			super.setLED(LED, red, green, blue);
		}
	}

	@Override
	public void setAllLEDs(int red, int green, int blue) {
		if (!blinking) {
			super.setAllLEDs(red, green, blue);
		}
	}

	@Override
	public void setServo(int servo, double degree) {
		if (!blinking) {
			super.setServo(servo, degree);
		}
	}
}
