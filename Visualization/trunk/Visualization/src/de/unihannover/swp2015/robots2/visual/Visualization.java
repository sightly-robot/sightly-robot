package de.unihannover.swp2015.robots2.visual;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import de.unihannover.swp2015.robots2.visual.core.IGameHandler;
import de.unihannover.swp2015.robots2.visual.core.RobotGameHandler;

/**
 * Main entry point.
 * 
 * @author Rico Schrage
 */
public class Visualization extends ApplicationAdapter {
	
	/**
	 * List of all {@link IGameHandler}.
	 */
	private final List<IGameHandler> gameHandlerList;
	
	/**
	 * Camera, which should be used as view.
	 * {@link com.badlogic.gdx.graphics.OrthographicCamera}
	 */
	private OrthographicCamera cam;
	
	/**
	 * Constructs a Visualization object. 
	 * 
	 * Important: Don't do OpenGL related things here! Use {@link create} instead.
	 */
	public Visualization() {
		this.gameHandlerList = new ArrayList<>();
	}
	
	@Override
	public void create () {
		
		int appWidth = Gdx.graphics.getWidth();
		int appHeight = Gdx.graphics.getHeight();
		
		this.cam = new OrthographicCamera();
		//TODO investigate which coordinate-system the model will use
		this.cam.setToOrtho(false, appWidth, appHeight);

		//TODO create RobotGameHandler properly
		this.gameHandlerList.add(new RobotGameHandler(null, null, cam));
	}
	
	@Override 
	public void dispose() {
		for (int i = 0; i < gameHandlerList.size(); ++i) {
			gameHandlerList.get(i).dispose();
		}
	}

	@Override
	public void render () {
		
		// sets the clear color to rgba(0, 0, 0, 1)
		Gdx.gl.glClearColor(1, 1, 1, 1);
		// clears the scene
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		for (int i = 0; i < gameHandlerList.size(); ++i) {
			gameHandlerList.get(i).update();
		}
		
		for (int i = 0; i < gameHandlerList.size(); ++i) {
			gameHandlerList.get(i).render();
		}
	}

}
