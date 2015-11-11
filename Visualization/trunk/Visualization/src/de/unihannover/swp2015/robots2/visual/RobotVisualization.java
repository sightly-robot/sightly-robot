package de.unihannover.swp2015.robots2.visual;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class RobotVisualization extends ApplicationAdapter {
	
	@Override 
	public void dispose() {
	}
	
	@Override
	public void create () {
		
	}	

	@Override
	public void render () {
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
	}

}
