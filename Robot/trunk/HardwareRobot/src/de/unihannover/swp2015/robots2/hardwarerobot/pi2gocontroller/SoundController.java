package de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

/**
 * Controller for hard-coded sound effects.<br>
 * It is not instantiable because it uses the Singleton pattern. Use the
 * instance instead.
 * 
 * @author Lenard Spiecker
 */
public class SoundController {

	/** The Singleton instance of the SoundController. */
	private static SoundController instance;

	private AudioClip gameMusic;
	private AudioClip startSound;

	/**
	 * Initializes the Singleton instance.<br>
	 * Reads the hard-coded sound effects from file.
	 */
	private SoundController() {
		try {
			gameMusic = Applet.newAudioClip(new URL("file:/home/pi/pacman.wav"));
			startSound = Applet.newAudioClip(new URL("file:/home/pi/simson.wav"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the instance of the SoundController.<br>
	 * If the instance is {@code null} a new one will be created.
	 * 
	 * @return the SoundController instance
	 */
	public static SoundController getInstance() {
		if (instance == null) {
			instance = new SoundController();
		}
		return instance;
	}

	/**
	 * Plays the startup sound effect.
	 */
	public void playStartSound() {
		try {
			startSound.play();
		} catch (Exception e) {
			// only catch
		}
	}

	/**
	 * Plays the game sound effect.
	 */
	public void playGameSound() {
		try {
			gameMusic.play();
		} catch (Exception e) {
			// only catch
		}
	}

	/**
	 * Plays the game sound effect in a loop.
	 */
	public void playMusic() {
		gameMusic.loop();
	}

	/**
	 * Stops playing the game sound effect.
	 */
	public void stopMusic() {
		gameMusic.stop();
	}

}
