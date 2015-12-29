package de.unihannover.swp2015.robots2.visual.desktop;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
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
 * Starter class for desktop systems. Accepts three different arguments, use -h | --help for usage information.
 * 
 * @author Rico Schrage
 */
public class DesktopLauncher {
	
	private static final Logger log = LogManager.getLogger();
	
	private static final String DEBUG_SHORT = "d";
	private static final String DEBUG_LONG = "debug";
	
	private static final String HELP_SHORT = "h";
	private static final String HELP_LONG = "help";
	
	private static final String IP_SHORT = "ip";
	private static final String IP_LONG = "ipv4";
	private static final String DEFAULT_IP = "127.0.0.1";
	
	private static final String IP_REGEX = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
	private static final String HEADER = "Starts a Visualization of the robot game.";
	private static final String FOOTER = "Part of the robot project.";
	private static final String APP = "Visualization";
	
	private static final String SHADER_PATH = "resources/shaders/";
	
	private DesktopLauncher() {
		//pure static class
	}
	
	/**
	 * Entry point. 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			//parsing arguments
			Options opts = createOptions();
			CommandLineParser clp = new DefaultParser();
			CommandLine cl = clp.parse(opts, args);
			
			//prints usage information if the help flag is set
			if (cl.hasOption(HELP_SHORT)) {
				HelpFormatter form = new HelpFormatter();
				form.printHelp(APP, HEADER, opts, FOOTER, true);
				return;
			}
			
			//sets debug to true if the debug flag has been set, default: false
			boolean debugArg = cl.hasOption(DEBUG_SHORT);
			log.info("Debug: {}", String.valueOf(debugArg));
			
			//sets broker ip to the given argument, if the argument has not been set: localhost (127.0.0.1)
			String brokerIp = cl.getOptionValue(IP_SHORT, DEFAULT_IP);
			if (!brokerIp.matches(IP_REGEX)) {
				brokerIp = DEFAULT_IP;
				log.warn("Given broker ip doesn't have the correct format. {} will be used instead", brokerIp);	
			}
			log.info("Broker ip: {}", brokerIp);
			
			//finally starts the 'real' app
			startApp(debugArg, brokerIp);
		}
		catch (ParseException e) {
			log.error("A syntax error occured:");
			log.error(e);
		}
	}
	
	/**
	 * Creates options used for argument parsing.
	 * 
	 * @return {@link Options}
	 */
	private static Options createOptions() {
		 Option help = Option.builder(HELP_SHORT)
				 .required(false)
			     .longOpt(HELP_LONG)
				 .desc("Prints usage information.")
			     .build();
		 Option debug = Option.builder(DEBUG_SHORT)
				 .required(false)
			     .longOpt(DEBUG_LONG)
				 .desc("Indicates if you want to enable the debug features, also affects the log level.")
			     .build();
		 Option ip = Option.builder(IP_SHORT)
				 .required(false)
				 .longOpt(IP_LONG)
				 .type(String.class)
				 .hasArg()
				 .desc("The broker ip you want to use. Only works without debug flag")
				 .build();
		 
		return new Options().addOption(help)
				.addOption(debug)
				.addOption(ip);
	}
	
	/**
	 * Starts the application with the parsed arguments.
	 * 
	 * @param debug debug flag
	 * @param brokerIp ip of the MQTT broker
	 */
	private static void startApp(final boolean debug, final String brokerIp) {
	
		ShaderLoader.BasePath = SHADER_PATH;
		
		if (debug) {
			// Will be replaced by a standalone packer app.
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
		config.width = 800;
		config.height = 800;
		config.foregroundFPS = 120;
		config.backgroundFPS = 60;
		config.vSyncEnabled = false;
		config.fullscreen = false;
		new LwjglApplication(new Visualization(debug, brokerIp), config).setLogLevel(Application.LOG_NONE);
	}

}
