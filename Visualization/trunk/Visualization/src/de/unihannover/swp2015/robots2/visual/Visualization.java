package de.unihannover.swp2015.robots2.visual;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.effects.Bloom;
import com.bitfire.postprocessing.effects.Fxaa;
import com.bitfire.utils.ShaderLoader;

import de.unihannover.swp2015.robots2.controller.externalInterfaces.IVisualization;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.visual.core.IGameHandler;
import de.unihannover.swp2015.robots2.visual.core.RobotGameHandler;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.resource.ResourceConstants;
import de.unihannover.swp2015.robots2.visual.resource.ResourceHandler;
import de.unihannover.swp2015.robots2.visual.util.TestApp;
import de.unihannover.swp2015.robots2.visual.util.TestUtil;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;
import de.unihannover.swp2015.robots2.visual.util.pref.FlexPreferences;

/**
 * Main entry point.
 * 
 * @author Rico Schrage
 */
public class Visualization extends ApplicationAdapter implements IVisualization {

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
	
	private PostProcessor pp;
	
	/**
	 * Constructs a Visualization object.
	 * 
	 * Important: Don't do OpenGL related things here! Use {@link create}
	 * instead.
	 */
	public Visualization() {
		this.gameHandlerList = new ArrayList<>();
	}

	@Override
	public void create() {

		int appWidth = Gdx.graphics.getWidth();
		int appHeight = Gdx.graphics.getHeight();
		
		ShaderLoader.BasePath = "resources/shaders/";
		
		this.pp = new PostProcessor(false, false, true);
		this.pp.addEffect(new Fxaa(appWidth, appHeight));
		this.prefs = new FlexPreferences("prefs");
		this.cam = new OrthographicCamera();
		this.cam.setToOrtho(true, appWidth, appHeight);
		this.fitViewport = new FitViewport(appWidth, appHeight, cam);
		
		final IResourceHandler resHandler = new ResourceHandler(ResourceConstants.ATLAS_PATH + ResourceConstants.ATLAS_NAME + ".atlas");
		
		//obv. just for testing
		final IGame testGame = TestUtil.createRandomTestGame(2, 12, 9);
		new TestApp(testGame);	
		
		// TODO create RobotGameHandler properly
		this.gameHandlerList.add(new RobotGameHandler(testGame, resHandler, cam, prefs));
	}

	@Override
	public void dispose() {
		for (int i = 0; i < gameHandlerList.size(); ++i) {
			gameHandlerList.get(i).dispose();
		}
	}

	@Override
	public void render() {

		// sets the clear color to rgba(0, 0, 0, 1)
		Gdx.gl.glClearColor(0, 0, 0, 1);
		// clears the scene
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		pp.capture();
		
		for (int i = 0; i < gameHandlerList.size(); ++i) {
			gameHandlerList.get(i).update();
		}

		for (int i = 0; i < gameHandlerList.size(); ++i) {
			gameHandlerList.get(i).render();
		}
		
		pp.render();
	}
	
	@Override
	public void resize(final int width, final int height) {
		this.fitViewport.update(width, height, true);
		
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
