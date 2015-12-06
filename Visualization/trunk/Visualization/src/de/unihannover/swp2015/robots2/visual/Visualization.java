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

import de.unihannover.swp2015.robots2.controller.externalInterfaces.IVisualization;
import de.unihannover.swp2015.robots2.controller.main.VisualizationMainController;
import de.unihannover.swp2015.robots2.visual.core.IGameHandler;
import de.unihannover.swp2015.robots2.visual.core.RobotGameHandler;
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
public class Visualization extends ApplicationAdapter implements IVisualization {

	/**
	 * Broker-IP
	 */
	private static final String CONNECTION_IP = "tcp://192.168.1.66";
	
	/**
	 * List of all {@link IGameHandler}.
	 */
	private final List<IGameHandler> gameHandlerList;

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
	 * Handles all MQTT stuff. Contains main model.
	 */
	private final VisualizationMainController mainController;
		
	/**
	 * Constructs a Visualization object.
	 * 
	 * Important: Don't do OpenGL related things here! Use {@link #create()}
	 * instead.
	 */
	public Visualization() {
		this.gameHandlerList = new ArrayList<>();
		this.mainController = new VisualizationMainController();
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
		
		final IResourceHandler resHandler = new ResourceHandler(ResConst.ATLAS_PATH.getName() + ResConst.ATLAS_NAME.getName() + ".atlas");
		
		/*
		 * //obv. just for testing
		 * final IGame testGame = TestUtil.createRandomTestGame(2, 4, 3);
		 * new TestApp(testGame);	
		 */

		//TODO handle connect exceptions
		//this.mainController.startMqtt(CONNECTION_IP);
		
		new TestApp(mainController.getGame());
		this.gameHandlerList.add(new RobotGameHandler(mainController.getGame(), resHandler, fitViewport, prefs));
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

	@Override
	public void setSettings(String settings) {
		//TODO json->preferences
	}

	@Override
	public String getSettings() {
		//TODO preferences->json
		return null;
	}

}
