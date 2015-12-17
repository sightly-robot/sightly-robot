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
	
	public static final int initialWidth = 800;
	public static final int initialHeight = 800;
	
	public static void main(String[] arg) {

		boolean debug = false;
		if (arg.length > 0 && arg[0].equals("debug")) {
				debug = true;
				System.out.println("Debug: " + String.valueOf(debug));
		}
		
		String brokerIp = "localhost";
		if (!debug && arg.length > 1 && arg[1].matches("[0-2]?[0-5]?[0-5].{3}[0-2]?[0-5]?[0-5]")) {
				brokerIp = arg[1];
				System.out.println("Broker ip: " + brokerIp);
		}
		
		startApp(debug, brokerIp);
	}
	
	private static void startApp(final boolean debug, final String brokerIp) {
	
		ShaderLoader.BasePath = "resources/shaders/";

		/* 
		* Will be replaced by a standalone packer app.
		*/
		
		// packs all textures of the default theme in a texture atlas
		// name of the theme "default"
		Settings packSettings = new Settings();
		packSettings.maxWidth = 1024*4;
		packSettings.maxHeight = 1024*4;
		packSettings.pot = true;
		packSettings.duplicatePadding = true;
		TexturePacker.process(packSettings, "assets/tex/default_theme_src", ResConst.ATLAS_PATH.getName()+ "/default", ResConst.ATLAS_NAME.getName());
		TexturePacker.process(packSettings, "assets/tex/earth_theme_src", ResConst.ATLAS_PATH.getName()+ "/earth", ResConst.ATLAS_NAME.getName());
		
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = initialWidth;
		config.height = initialHeight;
		config.foregroundFPS = 120;
		config.backgroundFPS = 60;
		config.vSyncEnabled = false;
		config.fullscreen = false;
		new LwjglApplication(new Visualization(debug, brokerIp), config);
	}

}
