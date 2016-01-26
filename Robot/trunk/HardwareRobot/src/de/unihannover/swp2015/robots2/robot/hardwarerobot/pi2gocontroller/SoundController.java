package de.unihannover.swp2015.robots2.robot.hardwarerobot.pi2gocontroller;

import java.applet.AudioClip;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;

/**
 * Controller for hard-coded sound effects.<br>
 * It is not instantiable because it uses the Singleton pattern. Use the
 * instance instead.
 * 
 * @author Lenard Spiecker
 */
public class SoundController implements IModelObserver {

	/** The Singleton instance of the SoundController. */
	private static SoundController instance;
	
	/** LOGGER: */
	private static Logger LOGGER = LogManager.getLogger(SoundController.class.getName());

	private AudioClip gameMusic;
	private AudioClip startSound;

	private IRobotController robotController;

	/**
	 * Initializes the Singleton instance.<br>
	 * Reads the hard-coded sound effects from file.
	 */
	private SoundController() {
		try {
			//gameMusic = Applet.newAudioClip(new URL("file:/home/pi/pacman.wav"));
			//startSound = Applet.newAudioClip(new URL("file:/home/pi/simson.wav"));
		} catch (Exception e) {
			LOGGER.error("Sounds could not be initialized",e);
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

	public void initAutoSound(IRobotController robotController) {
		this.robotController = robotController;

		robotController.getMyself().observe(this);
		robotController.getGame().observe(this);

		SoundController.getInstance().speak("Hello, My name is " + robotController.getMyself().getName()
				+ "! Do you like my color? Anyway... I am now online!");
	}

	public void speak(String message) {
		try {
			Runtime.getRuntime().exec("echo '" + message + "' | festival --tts");
		} catch (IOException e) {
			LOGGER.error("Speak "+message+" not worked",e);
		}
	}

	/**
	 * Plays the startup sound effect.
	 */
	public void playStartSound() {
		try {
			startSound.play();
		} catch (Exception e) {
			LOGGER.error("StartSound not working",e);
		}
	}

	/**
	 * Plays the game sound effect.
	 */
	public void playGameSound() {
		try {
			gameMusic.play();
		} catch (Exception e) {
			LOGGER.error("GameMusic not working",e);
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

	@Override
	public void onModelUpdate(IEvent event) {
		switch (event.getType()) {
		case ROBOT_STATE:
			switch (robotController.getMyself().getState()) {
			case CONNECTED:
				speak("I'm connected now!");
				break;
			case SETUPSTATE:
				speak("Take me to my place! Then click on my Button!");
				break;
			case ENABLED:
				speak("I'm ready now!");
				break;
			case ROBOTICS_ERROR:
				speak("An Error occured!");
				break;
			case MANUAL_DISABLED_GUI:
				speak("Why do you disable me?");
				break;
			case MANUAL_DISABLED_ROBOT:
				speak("Outch!");
				break;
			case DISCONNECTED:
				speak("Does anybody know, where the Broker is?");
				break;
			}
			break;
		case GAME_STATE:
			speak("Let's go!");
			break;
		case ROBOT_DELETE:
			speak("Good Bye, Sir!");
			break;
		default:
			break;
		}

	}

}
