package de.unihannover.swp2015.robots2.visual.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.bitfire.utils.ShaderLoader;

import de.unihannover.swp2015.robots2.visual.Visualization;
import de.unihannover.swp2015.robots2.visual.resource.ResConst;

/**
 * Starter class for desktop systems.
 * 
 * @author Rico Schrage
 */
public class DesktopLauncher {
	
	public static final float viewWidth = 800;
	public static final float viewHeight = 800;
	
	public static void main (String[] arg) {

		ShaderLoader.BasePath = "resources/shaders/";

		Settings packSettings = new Settings();
		packSettings.maxWidth = 1024*4;
		packSettings.maxHeight = 1024*4;
		packSettings.pot = true;
		packSettings.duplicatePadding = true;
		TexturePacker.process(packSettings, "assets/tex", ResConst.ATLAS_PATH.getName(), ResConst.ATLAS_NAME.getName());
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) viewWidth;
		config.height = (int) viewHeight;
		config.foregroundFPS = 0;
		config.backgroundFPS = 0;
		config.vSyncEnabled = false;
		config.fullscreen = false;
		new LwjglApplication(new Visualization(), config);
	}

}
