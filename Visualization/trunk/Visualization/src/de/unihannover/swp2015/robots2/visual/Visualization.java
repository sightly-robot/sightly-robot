package de.unihannover.swp2015.robots2.visual;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.unihannover.swp2015.robots2.visual.core.PrefKey;
import de.unihannover.swp2015.robots2.visual.core.handler.IGameHandler;
import de.unihannover.swp2015.robots2.visual.core.handler.MqttHandler;
import de.unihannover.swp2015.robots2.visual.core.handler.PreferenceHandler;
import de.unihannover.swp2015.robots2.visual.game.RobotGameHandler;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.resource.ResConst;
import de.unihannover.swp2015.robots2.visual.resource.ResourceHandler;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;
import de.unihannover.swp2015.robots2.visual.util.test.TestApp;
import de.unihannover.swp2015.robots2.visual.util.LoopedTask;
import de.unihannover.swp2015.robots2.visual.util.Task;
import de.unihannover.swp2015.robots2.visual.util.pref.FlexPreferences;

/**
 * Main entry point.
 * 
 * @author Rico Schrage
 */
public class Visualization extends ApplicationAdapter {
	
	/** Logger (log4j) */
	private static final Logger log = LogManager.getLogger();
	
	/** Broker-IP */
	private final String ip;
	
	/** Indicates whether this build is a debug build. Have to be set before the application will be created. */
	public final boolean debug;
	
	/** List of all {@link IGameHandler}. */
	private final List<IGameHandler> gameHandlerList;

	/** MqttHandler, handles connection fails. */
	private MqttHandler mqttHandler;
	
	/** Settings received via MQTT */
	private IPreferences<PrefKey> prefs;
	
	/** Information about the system */
	private GraphicsDevice device;
	
	/** For the timing of the debug output. */
	private LoopedTask fpsLogger;
	
	/**
	 * Constructs a Visualization object.
	 * 
	 * Important: Don't do OpenGL related things here! Use {@link #create()}
	 * instead.
	 */
	public Visualization(final boolean debugFlag, final String brokerIp) {
		this.device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		this.gameHandlerList = new ArrayList<>();
		this.ip = brokerIp;
		this.debug = debugFlag;
		
		if (debug) {
			fpsLogger = new LoopedTask(5f, new Task() {
				@Override
				public void work() {
					log.info("Current fps: " + Gdx.graphics.getFramesPerSecond());
				}
			});
		}
	}

	@Override
	public void create() {

		prefs = new FlexPreferences<PrefKey>("prefs");
		mqttHandler = new MqttHandler(ip, new PreferenceHandler(prefs));
		
		final int appWidth = Gdx.graphics.getWidth();
		final int appHeight = Gdx.graphics.getHeight();
		
		final OrthographicCamera cam = new OrthographicCamera();
		cam.setToOrtho(true);
		final Viewport fitViewport = new FitViewport(appWidth, appHeight, cam);
		fitViewport.update(appWidth, appHeight, true);

		prefs.putFloat(PrefKey.VIEW_WIDTH, appWidth);
		prefs.putFloat(PrefKey.VIEW_HEIGHT, appHeight);
		prefs.putFloat(PrefKey.DEVICE_WIDTH, device.getDisplayMode().getWidth());
		prefs.putFloat(PrefKey.DEVICE_HEIGHT, device.getDisplayMode().getHeight());
		
		final IResourceHandler resHandler = new ResourceHandler(ResConst.ATLAS_PATH.getName());
		gameHandlerList.add(new RobotGameHandler(mqttHandler.getGame(), fitViewport, resHandler, prefs));
		
		if (debug) {
			new TestApp(mqttHandler.getGame());
		}
		else {
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
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		for (int i = 0; i < gameHandlerList.size(); ++i) {
			gameHandlerList.get(i).update();
		}
				
		for (int i = 0; i < gameHandlerList.size(); ++i) {
			gameHandlerList.get(i).render();
		}

		if (debug) {
			fpsLogger.update();
		}
	}
	
	@Override
	public void resize(final int width, final int height) {
		for (int i = 0; i < gameHandlerList.size(); ++i) {
			gameHandlerList.get(i).resize(width, height);
		}
	}

}
