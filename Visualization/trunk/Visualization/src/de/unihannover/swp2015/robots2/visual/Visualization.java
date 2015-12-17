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

import de.unihannover.swp2015.robots2.visual.core.MqttHandler;
import de.unihannover.swp2015.robots2.visual.core.PrefConst;
import de.unihannover.swp2015.robots2.visual.core.RobotGameHandler;
import de.unihannover.swp2015.robots2.visual.core.base.IGameHandler;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.resource.ResConst;
import de.unihannover.swp2015.robots2.visual.resource.ResourceHandler;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;
import de.unihannover.swp2015.robots2.visual.util.TestApp;
import de.unihannover.swp2015.robots2.visual.util.pref.FlexPreferences;

/**
 * Main entry point.
 * 
 * @author Rico Schrage
 */
public class Visualization extends ApplicationAdapter {
	
	private final Logger log = LogManager.getLogger();
	
	/**
	 * Broker-IP
	 */
	private final String brokerIp;
	
	/**
	 * Indicates whether this build is a debug build.
	 * Have to be set before the application will be created.
	 */
	public final boolean debug;
	
	/**
	 * List of all {@link IGameHandler}.
	 */
	private final List<IGameHandler> gameHandlerList;

	/**
	 * MqttHandler, handles connection fails.
	 */
	private final MqttHandler mqttHandler;
	
	/**
	 * Settings received via MQTT
	 */
	private IPreferences prefs;

	/**
	 * Camera, which should be used as view.
	 * {@link com.badlogic.gdx.graphics.OrthographicCamera}
	 */
	private OrthographicCamera cam;

	/**
	 * Handles virtual display. Furthermore it makes the application keep the aspect ratio.
	 */
	private Viewport fitViewport;
	
	/**
	 * Information about the system
	 */
	private GraphicsDevice device;
	
	/**
	 * For the timing of the debug output.
	 */
	float c = 5;
		
	/**
	 * Constructs a Visualization object.
	 * 
	 * Important: Don't do OpenGL related things here! Use {@link #create()}
	 * instead.
	 */
	public Visualization(final boolean debug, final String brokerIp) {
		this.device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		this.gameHandlerList = new ArrayList<>();
		this.brokerIp = brokerIp;
		this.mqttHandler = new MqttHandler(this.brokerIp);
		this.debug = debug;
	}

	@Override
	public void create() {

		int appWidth = Gdx.graphics.getWidth();
		int appHeight = Gdx.graphics.getHeight();
			
		this.prefs = new FlexPreferences("prefs");
		this.cam = new OrthographicCamera();
		this.cam.setToOrtho(true, appWidth, appHeight);
		this.fitViewport = new FitViewport(appWidth, appHeight, cam);
		this.fitViewport.update(appWidth, appHeight, true);

		this.prefs.putFloat(PrefConst.VIEW_WIDTH, appWidth);
		this.prefs.putFloat(PrefConst.VIEW_HEIGHT, appHeight);
		this.prefs.putFloat(PrefConst.DEVICE_WIDTH, device.getDisplayMode().getWidth());
		this.prefs.putFloat(PrefConst.DEVICE_HEIGHT, device.getDisplayMode().getHeight());
		
		final IResourceHandler resHandler = new ResourceHandler(ResConst.ATLAS_PATH.getName());
		
		if (debug) {
			new TestApp(mqttHandler.getGame());
		}
		else {
			final Thread mqttThread = new Thread(mqttHandler);
			mqttThread.start();
		}
		
		this.gameHandlerList.add(new RobotGameHandler(mqttHandler.getGame(), fitViewport, resHandler, prefs));
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
			c -= Gdx.graphics.getDeltaTime();
			if (c < 0) {
				c = 5;
				log.info(Gdx.graphics.getFramesPerSecond());
			}
		}
	}
	
	@Override
	public void resize(final int width, final int height) {
		this.fitViewport.update(width, height);

		for (int i = 0; i < gameHandlerList.size(); ++i) {
			gameHandlerList.get(i).resize(width, height);
		}
	}

}
