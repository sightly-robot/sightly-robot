package de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller;

import java.awt.Color;
import java.io.IOException;

public class BlinkLEDAndServoController extends LEDAndServoController {

	private boolean blinking = false;
	private long blinkUntil = 0;
	private static final long BLINK_MIN_DURATION = 2000;

	private BlinkLEDAndServoController() throws IOException {
		super();
	}

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

	public void startBlinking(Color color) {
		blinkUntil = System.currentTimeMillis() + BLINK_MIN_DURATION;
		if (!blinking) {
			blinking = true;
			new Thread() {
				int currentBlink = 0;
				static final int STEPS_PER_HALFSECOND = 15;
				Color c;
				
				public void run() {
					try {
						// Savety delay
						sleep(50);
						while (System.currentTimeMillis() < blinkUntil && currentBlink > 3) {
							for (int i = 0; i < 255; i += 255 / STEPS_PER_HALFSECOND) {
								c = new Color(color.getRed(), color.getGreen(), color.getBlue(), i);
								setBlinkingLEDs(c);
								sleep(33);
							}
							for (int i = 255; i >= 0; i -= 255 / STEPS_PER_HALFSECOND) {
								c = new Color(color.getRed(), color.getGreen(), color.getBlue(), i);
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
	
	private void setBlinkingLEDs(Color color)
	{
		setBlinkingLEDs(color.getRed() * 16, color.getGreen() * 16, color.getBlue() * 16);
	}
	
	private void setBlinkingLEDs(int red, int green, int blue) {
		for (int i = 0; i < 4; i++) {
			super.setLED(i, red, green, blue);
		}
	}
	
	@Override
	protected void setLED(int LED, int red, int green, int blue) {
		if(!blinking)
		{
			super.setLED(LED, red, green, blue);
		}
	}
	
	@Override
	public void setAllLEDs(int red, int green, int blue) {
		if(!blinking)
		{
			super.setAllLEDs(red, green, blue);
		}
	}
	
	@Override
	public void setServo(int servo, double degree) {
		if(!blinking)
		{
			super.setServo(servo, degree);
		}
	}
}
