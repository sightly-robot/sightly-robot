package de.unihannover.swp2015.robots2.visual.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

import de.unihannover.swp2015.robots2.visual.Visualization;
import de.unihannover.swp2015.robots2.visual.resource.ResourceConstants;

public class DesktopLauncher {
	
	public static void main (String[] arg) {

		Settings packSettings = new Settings();
		packSettings.maxWidth = 2048;
		packSettings.maxHeight = 2048;
		TexturePacker.process(packSettings, "assets/tex", ResourceConstants.ATLAS_PATH, ResourceConstants.ATLAS_NAME);
		
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
