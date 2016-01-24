package de.unihannover.swp2015.robots2.visual.desktop;

import java.awt.GraphicsEnvironment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.bitfire.utils.ShaderLoader;

import de.unihannover.swp2015.robots2.visual.Visualization;
import de.unihannover.swp2015.robots2.visual.desktop.CommandLineHandler.FlagKey;
import de.unihannover.swp2015.robots2.visual.desktop.CommandLineHandler.OptionKey;
import de.unihannover.swp2015.robots2.visual.resource.ResConst;

/**
 * Starter class for desktop systems. Accepts three different arguments, use -h
 * | --help for usage information.
 * 
 * @author Rico Schrage
 */
public class DesktopLauncher {

	/** Logger (log4j) */
	private static final Logger log = LogManager.getLogger();
	/** Path to the shaders of the PP-library */
	private static final String SHADER_PATH = "resources/shaders/";

	private DesktopLauncher() {
		// pure static class
	}

	/**
	 * Main entry point.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		final CommandLineHandler clh = new CommandLineHandler();

		// if this is true the help flag has been detected, so the user
		// did not intend to start the application.
		if (clh.parse(args)) {
			return;
		}

		startApp(clh.getFlag(FlagKey.DEBUG), clh.getOption(OptionKey.IP), clh.getOption(OptionKey.PROTOCOL));
	}

	/**
	 * Starts the application with the parsed arguments.
	 * 
	 * @param debug
	 *            debug flag
	 * @param brokerIp
	 *            ip of the MQTT broker
	 */
	private static void startApp(final boolean debug, final String brokerIp, final String protocol) {

		ShaderLoader.BasePath = SHADER_PATH;

		if (debug) {
			// packs all textures of the default theme in a texture atlas
			// name of the theme "default"
			log.info("TexturePacker has been started:");
			final Settings packSettings = new Settings();
			packSettings.maxWidth = 1024 * 4;
			packSettings.maxHeight = 1024 * 4;
			packSettings.pot = true;
			packSettings.duplicatePadding = true;
			packSettings.paddingX = 4;
			packSettings.paddingY = 4;
			packSettings.filterMag = TextureFilter.MipMapNearestNearest;
			packSettings.filterMin = TextureFilter.MipMapNearestNearest;
			TexturePacker.process(packSettings, "internal/assets/tex/default_theme_src",
					"internal/" + ResConst.ATLAS_PATH.getName() + "/default", ResConst.ATLAS_NAME.getName());
			TexturePacker.process(packSettings, "internal/assets/tex/earth_theme_src", "internal/" + ResConst.ATLAS_PATH.getName() + "/earth",
					ResConst.ATLAS_NAME.getName());
			TexturePacker.process(packSettings, "internal/assets/tex/home_theme_src", "internal/" + ResConst.ATLAS_PATH.getName() + "/home",
					ResConst.ATLAS_NAME.getName());
		}

		final java.awt.DisplayMode dm = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDisplayMode();
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = dm.getWidth();
		config.height = dm.getHeight();
		config.foregroundFPS = 120;
		config.backgroundFPS = 120;
		config.vSyncEnabled = false;
		config.fullscreen = true;
		config.addIcon("assets/icon/rIcon.png", FileType.Internal);
		config.addIcon("assets/icon/rIcon_32.png", FileType.Internal);
		config.addIcon("assets/icon/rIcon_128.png", FileType.Internal);
		new LwjglApplication(new Visualization(debug, brokerIp, protocol), config).setLogLevel(Application.LOG_NONE);
	}

}
