package de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

/**
 * Singleton Pi2Go Robot SoundController for hard-coded sound effects.
 * @author Lenard Spiecker
 *
 */
public class SoundController {

	private static SoundController instance;
	
	private AudioClip gameMusic;
	private AudioClip startSound;

	private SoundController() {
		// getClass().getResource("/simson.wav")
		try {
			gameMusic = Applet.newAudioClip(new URL("file:/home/pi/pacman.wav"));
			startSound = Applet.newAudioClip(new URL("file:/home/pi/simson.wav"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static SoundController getInstance()
	{
		if(instance == null)
		{
			instance = new SoundController();
		}
		return instance;
	}

	public void playStartSound() {
		try {
			startSound.play();
		} catch (Exception e) {
		}
	}

	public void playGameSound() {
		try {
			gameMusic.play();
		} catch (Exception e) {
		}
	}

	public void playMusic() {
		gameMusic.loop();
	}

	public void stopMusic() {
		gameMusic.stop();
	}

}
