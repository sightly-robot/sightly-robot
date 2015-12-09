package de.unihannover.swp2015.robots2.visual;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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

	/**
	 * Indicates whether this build is a debug build.
	 * Have to be set before the application will be created.
	 */
	public static boolean debug = false;
	
	/**
	 * Broker-IP
	 */
	private static final String CONNECTION_IP = "192.168.1.115";
	
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
	 * Constructs a Visualization object.
	 * 
	 * Important: Don't do OpenGL related things here! Use {@link #create()}
	 * instead.
	 */
	public Visualization() {
		this.gameHandlerList = new ArrayList<>();
		this.mqttHandler = new MqttHandler();
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
		
		final IResourceHandler resHandler = new ResourceHandler(ResConst.ATLAS_PATH.getName() + ResConst.ATLAS_NAME.getName() + ".atlas");
		
		if (debug) {
			new TestApp(mqttHandler.getGame());
		}
		else {
			this.mqttHandler.startMqtt(CONNECTION_IP);
		}
		
		this.gameHandlerList.add(new RobotGameHandler(mqttHandler.getGame(), fitViewport, resHandler, prefs));
	}

	@Override
	public void dispose() {
		for (int i = 0; i < gameHandlerList.size(); ++i) {
			gameHandlerList.get(i).dispose();
		}
	}
	float c = 5;
	@Override
	public void render() {
			
		if (Gdx.input.isKeyPressed(Keys.F))
			Gdx.graphics.setDisplayMode(1920, 1080, true);
		else if (Gdx.input.isKeyPressed(Keys.E))
			Gdx.graphics.setDisplayMode((int) fitViewport.getWorldWidth(), (int) fitViewport.getWorldHeight(), false);	
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		for (int i = 0; i < gameHandlerList.size(); ++i) {
			gameHandlerList.get(i).update();
		}
				
		for (int i = 0; i < gameHandlerList.size(); ++i) {
			gameHandlerList.get(i).render();
		}

		c-=Gdx.graphics.getDeltaTime();
		if (c <0) {
			c = 5;
			System.out.println(Gdx.graphics.getFramesPerSecond());
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
