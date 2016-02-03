package de.unihannover.swp2015.robots2.visual;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import de.unihannover.swp2015.robots2.visual.core.PrefKey;
import de.unihannover.swp2015.robots2.visual.core.handler.IGameHandler;
import de.unihannover.swp2015.robots2.visual.core.handler.MqttHandler;
import de.unihannover.swp2015.robots2.visual.core.handler.PreferenceHandler;
import de.unihannover.swp2015.robots2.visual.core.handler.MqttHandler.MqttProtocol;
import de.unihannover.swp2015.robots2.visual.game.RobotGameHandler;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.resource.ResConst;
import de.unihannover.swp2015.robots2.visual.resource.ResourceHandler;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;
import de.unihannover.swp2015.robots2.visual.util.test.TestApp;
import de.unihannover.swp2015.robots2.visual.util.FlexibleFitViewport;
import de.unihannover.swp2015.robots2.visual.util.LoopedTask;
import de.unihannover.swp2015.robots2.visual.util.Task;
import de.unihannover.swp2015.robots2.visual.util.pref.FlexPreferences;

/**
 * main entry point
 * 
 * @author Rico Schrage
 */
public class Visualization extends ApplicationAdapter {

	/** logger (log4j) */
	private static final Logger LOGGER = LogManager.getLogger();

	/** broker IP */
	private final String ip;
	/** transfer protocol */
	private final String protocol;

	/**
	 * Indicates whether this build is a debug build.
	 * 
	 * Has to be set before the application will be created.
	 */
	public final boolean debug;

	/** list of all {@link IGameHandler} */
	private final List<IGameHandler> gameHandlerList;

	/** MqttHandler which handles connection fails */
	private MqttHandler mqttHandler;

	/** settings received via MQTT */
	private IPreferences<PrefKey> prefs;

	/** for the timing of the debug output */
	private LoopedTask fpsLogger;

	/** ID of the visualization which is used for the communication with the GUI */
	private UUID id;

	/**
	 * Constructs a Visualization object.
	 * 
	 * Important: Don't do OpenGL related things here! Use {@link #create()}
	 * instead.
	 */
	public Visualization(final boolean debugFlag, final String brokerIp,
			String protocol) {
		this.gameHandlerList = new ArrayList<>();
		this.ip = brokerIp;
		this.protocol = protocol;
		this.debug = debugFlag;
		this.id = UUID.randomUUID();

		this.fpsLogger = new LoopedTask(5f, new Task() {
			@Override
			public void work() {
				LOGGER.info("Current FPS: " + Gdx.graphics.getFramesPerSecond());
			}
		});
	}

	@Override
	public void create() {

		LOGGER.debug("Id of the visualization: {}", id);

		prefs = new FlexPreferences<PrefKey>("prefs");
		mqttHandler = new MqttHandler(MqttProtocol.searchMatching(protocol),
				ip, new PreferenceHandler(prefs, id));

		final int appWidth = Gdx.graphics.getWidth();
		final int appHeight = Gdx.graphics.getHeight();
		final int nativeWidth = Gdx.graphics.getDisplayMode().width;
		final int nativeHeight = Gdx.graphics.getDisplayMode().height;

		final OrthographicCamera cam = new OrthographicCamera();
		cam.setToOrtho(true);
		final FlexibleFitViewport fitViewport = new FlexibleFitViewport(
				appWidth, appHeight, cam);
		fitViewport.update(appWidth, appHeight, true);

		prefs.putFloat(PrefKey.VIEW_WIDTH, appWidth);
		prefs.putFloat(PrefKey.VIEW_HEIGHT, appHeight);

		prefs.putFloat(PrefKey.DEVICE_WIDTH, nativeWidth);
		prefs.putFloat(PrefKey.DEVICE_HEIGHT, nativeHeight);

		LOGGER.debug("Native resolution is set to: {}x{}", nativeWidth,
				nativeHeight);

		final IResourceHandler resHandler = new ResourceHandler(
				ResConst.ATLAS_PATH.getName());
		gameHandlerList.add(new RobotGameHandler(mqttHandler.getGame(),
				fitViewport, resHandler, prefs));

		if (debug) {
			new TestApp(mqttHandler.getGame());
		} else {
			final Thread mqttThread = new Thread(mqttHandler, "MQTT");
			mqttThread.start();
		}
	}

	@Override
	public void dispose() {
		for (int i = 0; i < gameHandlerList.size(); ++i) {
			gameHandlerList.get(i).dispose();
		}
	}

	@Override
	public void render() {

		if (Gdx.input.isKeyPressed(Keys.F)) {
			DisplayMode mode = Gdx.graphics.getDisplayMode();
			String undecorated = "org.lwjgl.opengl.Window.undecorated";
			if (System.getProperty(undecorated) != null
					&& Boolean.parseBoolean(undecorated)) {
				int deviceWidth = (int) prefs.getFloat(PrefKey.DEVICE_WIDTH);
				int deviceHeight = (int) prefs.getFloat(PrefKey.DEVICE_HEIGHT);
				if (mode.width == deviceWidth && mode.height == deviceHeight) {
					Gdx.graphics.setWindowedMode(mode.width / 2,
							mode.height / 2);
				} else {
					Gdx.graphics.setWindowedMode(deviceWidth, deviceHeight);
				}
			} else {
				if (Gdx.graphics.isFullscreen()) {
					Gdx.graphics.setWindowedMode(mode.width / 2,
							mode.height / 2);
				} else {
					Gdx.graphics.setFullscreenMode(mode);
				}
			}
		}

		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		for (int i = 0; i < gameHandlerList.size(); ++i) {
			gameHandlerList.get(i).update();
		}

		for (int i = 0; i < gameHandlerList.size(); ++i) {
			gameHandlerList.get(i).render();
		}

		fpsLogger.update();
	}

	@Override
	public void resize(final int width, final int height) {
		for (int i = 0; i < gameHandlerList.size(); ++i) {
			gameHandlerList.get(i).resize(width, height);
		}
	}

}
