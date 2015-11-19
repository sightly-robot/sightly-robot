package de.unihannover.swp2015.robots2.visual.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.unihannover.swp2015.robots2.visual.Visualization;

public class DesktopLauncher {
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = 800;
		config.height = 600;
		config.foregroundFPS = 60;
		config.backgroundFPS = 60;
		config.vSyncEnabled = false;
		config.fullscreen = false;
		
		new LwjglApplication(new Visualization(), config);
	}

}
