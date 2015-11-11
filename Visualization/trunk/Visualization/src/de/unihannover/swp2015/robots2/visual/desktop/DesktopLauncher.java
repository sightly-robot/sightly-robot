package de.unihannover.swp2015.robots2.visual.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.unihannover.swp2015.robots2.visual.RobotVisualization;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 800;
		config.foregroundFPS = 0;
		config.vSyncEnabled = false;
		config.fullscreen = false;
		new LwjglApplication(new RobotVisualization(), config);
	}
}
