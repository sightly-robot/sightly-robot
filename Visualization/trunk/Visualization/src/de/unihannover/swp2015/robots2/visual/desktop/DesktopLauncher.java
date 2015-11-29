package de.unihannover.swp2015.robots2.visual.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

import de.unihannover.swp2015.robots2.visual.Visualization;
import de.unihannover.swp2015.robots2.visual.resource.ResConst;

public class DesktopLauncher {
	
	public static void main (String[] arg) {

		Settings packSettings = new Settings();
		packSettings.maxWidth = 1024*4;
		packSettings.maxHeight = 1024*4;
		packSettings.pot = true;
		TexturePacker.process(packSettings, "assets/tex", ResConst.ATLAS_PATH.getName(), ResConst.ATLAS_NAME.getName());
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 600;
		config.foregroundFPS = 0;
		config.backgroundFPS = 0;
		config.vSyncEnabled = false;
		config.fullscreen = false;
		new LwjglApplication(new Visualization(), config);
	}

}
