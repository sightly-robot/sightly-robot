package de.unihannover.swp2015.robots2.visual.desktop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Application;
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
	
	private static final Logger log = LogManager.getLogger();
	
	private static final int INIT_WIDTH = 800;
	private static final int INIT_HEIGHT = 800;
	
	private static final String IP_REGEX = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
	private static final String DEBUG_SIGNAL = "debug";
	
	private DesktopLauncher() {
		//pure static class
	}
	
	public static void main(String[] arg) {

		boolean debug = false;
		if (arg.length > 0 && arg[0].equals(DEBUG_SIGNAL)) {
				debug = true;
				log.info("Debug: {}", String.valueOf(debug));
		}
		
		String brokerIp = "localhost";
		if (!debug && arg.length > 1 && arg[1].matches(IP_REGEX)) {
				brokerIp = arg[1];
				log.info("Broker ip: {}", brokerIp);
		}
		
		startApp(debug, brokerIp);
	}
	
	private static void startApp(final boolean debug, final String brokerIp) {
	
		ShaderLoader.BasePath = "resources/shaders/";
		
		if (debug) {
			/* 
			* Will be replaced by a standalone packer app.
			*/
			
			// packs all textures of the default theme in a texture atlas
			// name of the theme "default"

			log.info("TexturePacker has been started:");
			final Settings packSettings = new Settings();
			packSettings.maxWidth = 1024*4;
			packSettings.maxHeight = 1024*4;
			packSettings.pot = true;
			packSettings.duplicatePadding = true;
			TexturePacker.process(packSettings, "assets/tex/default_theme_src", ResConst.ATLAS_PATH.getName()+ "/default", ResConst.ATLAS_NAME.getName());
			TexturePacker.process(packSettings, "assets/tex/earth_theme_src", ResConst.ATLAS_PATH.getName()+ "/earth", ResConst.ATLAS_NAME.getName());
		}
		
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = INIT_WIDTH;
		config.height = INIT_HEIGHT;
		config.foregroundFPS = 120;
		config.backgroundFPS = 60;
		config.vSyncEnabled = false;
		config.fullscreen = false;
		new LwjglApplication(new Visualization(debug, brokerIp), config).setLogLevel(Application.LOG_NONE);
	}

}
