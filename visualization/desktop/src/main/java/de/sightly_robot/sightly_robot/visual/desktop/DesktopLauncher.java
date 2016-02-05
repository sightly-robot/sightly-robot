package de.sightly_robot.sightly_robot.visual.desktop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.bitfire.utils.ShaderLoader;

import de.sightly_robot.sightly_robot.visual.Visualization;
import de.sightly_robot.sightly_robot.visual.desktop.CommandLineHandler.FlagKey;
import de.sightly_robot.sightly_robot.visual.desktop.CommandLineHandler.OptionKey;
import de.sightly_robot.sightly_robot.visual.resource.ResConst;

/**
 * starter class for de.sightly_robot.sightly_robot.visual.desktop systems
 * 
 * Accepts three different arguments, use -h | --help for usage information or
 * see {@link CommandLineHandler}.
 * 
 * @author Rico Schrage
 */
public class DesktopLauncher {

	/** logger (log4j) */
	private static final Logger LOGGER = LogManager.getLogger();
	/** path to the shaders of the PP-library */
	private static final String SHADER_PATH = "resources/shaders/";

	private DesktopLauncher() {
		// pure static class
	}

	/**
	 * main entry point
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

		startApp(clh.getFlag(FlagKey.DEBUG), clh.getOption(OptionKey.IP),
				clh.getOption(OptionKey.PROTOCOL),
				clh.getFlag(FlagKey.WINDOWED), clh.getFlag(FlagKey.FULLSCREEN));
	}

	/**
	 * Starts the application with the parsed arguments.
	 * 
	 * @param debug
	 *            debug flag
	 * @param brokerIp
	 *            IP of the MQTT broker
	 * @param protocol
	 *            used protocol
	 * @param windowed
	 *            true if application starts in windowed mode
	 * @param fullscreen
	 *            true if application starts in fullscreen mode
	 */
	private static void startApp(boolean debug, String brokerIp,
			String protocol, boolean windowed, boolean fullscreen) {

		ShaderLoader.BasePath = SHADER_PATH;

		if (debug) {
			// packs all textures of the default theme in a texture atlas
			// name of the theme "default"
			LOGGER.info("TexturePacker has been started:");
			final Settings packSettings = new Settings();
			packSettings.maxWidth = 1024 * 4;
			packSettings.maxHeight = 1024 * 4;
			packSettings.pot = true;
			packSettings.duplicatePadding = true;
			packSettings.paddingX = 4;
			packSettings.paddingY = 4;
			packSettings.filterMag = TextureFilter.MipMapNearestNearest;
			packSettings.filterMin = TextureFilter.MipMapNearestNearest;
			TexturePacker.process(packSettings,
					"tex/default_theme_src", ResConst.ATLAS_PATH.getName() + "/default",
					ResConst.ATLAS_NAME.getName());
			TexturePacker.process(packSettings,
					"tex/earth_theme_src", ResConst.ATLAS_PATH.getName() + "/earth",
					ResConst.ATLAS_NAME.getName());
			TexturePacker.process(packSettings,
					"tex/home_theme_src", ResConst.ATLAS_PATH.getName() + "/home",
					ResConst.ATLAS_NAME.getName());
			TexturePacker.process(packSettings,
					"tex/one_theme_src", ResConst.ATLAS_PATH.getName() + "/one",
					ResConst.ATLAS_NAME.getName());
			TexturePacker.process(packSettings,
					"tex/glow_theme_src", ResConst.ATLAS_PATH.getName() + "/glow",
					ResConst.ATLAS_NAME.getName());
		}

		final DisplayMode dm = LwjglApplicationConfiguration
				.getDesktopDisplayMode();
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = dm.width / 2;
		config.height = dm.height / 2;
		config.foregroundFPS = 120;
		config.backgroundFPS = 120;
		config.vSyncEnabled = false;
		config.addIcon("icon/rIcon.png", FileType.Internal);
		config.addIcon("icon/rIcon_32.png", FileType.Internal);
		config.addIcon("icon/rIcon_128.png", FileType.Internal);
		config.fullscreen = fullscreen;

		if (windowed) {
			System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
			config.resizable = false;
		}

		new LwjglApplication(new Visualization(debug, brokerIp, protocol),
				config).setLogLevel(Application.LOG_NONE);
	}

}
