package de.unihannover.swp2015.robots2.visual.desktop;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handles arguments passed through the console. Uses Apache-commons-CLI to parse and store options received via command line.
 * 
 * @author Rico Schrage
 */
public class CommandLineHandler {
	
	/** Logger (log4j) */
	private static final Logger log = LogManager.getLogger();
	
	/** Short console argument to enable the debug mode */
	private static final String DEBUG_SHORT = "d";
	/** Long console argument to enable the debug mode*/
	private static final String DEBUG_LONG = "debug";
	
	/** Short console argument to print usage informations */
	private static final String HELP_SHORT = "h";
	/** Long console argument to print usage informations */
	private static final String HELP_LONG = "help";

	/** Short console argument to pass the IP of the broker */
	private static final String IP_SHORT = "ip";
	/** Long console argument to pass the IP of the broker */
	private static final String IP_LONG = "ipv4";
	/** Default ip, will be used when the IP flag is not set */
	private static final String DEFAULT_IP = "127.0.0.1";
	
	/** Regex, which checks if the passed IP is valid */
	private static final String IP_REGEX = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
	/** Description of the app; will appear as header when printing usage information */
	private static final String HEADER = "Starts a Visualization of the robot game.";
	/** Description of the app; will appear as footer when printing usage information */
	private static final String FOOTER = "Part of the robot project.";
	/** Name of the app */
	private static final String APP = "Visualization";
	
	/** Contains all passed flags */
	private final Map<FlagKey, Boolean> flags;
	/** Contains all passed options */
	private final Map<OptionKey, String> options;
	
	/**
	 * Construct a {@link CommandLineHandler}.
	 */
	public CommandLineHandler() {
		this.options = new HashMap<>();
		this.flags = new HashMap<>();
	}
	
	/**
	 * Parses the command line arguments. Cancels parsing when finding the help flag.
	 * 
	 * @param args arguments as array
	 * @return true if help flag has been detected
	 */
	public boolean parse(String[] args) {
		try {
			//parsing arguments
			Options opts = createOptions();
			CommandLineParser clp = new DefaultParser();
			CommandLine cl = clp.parse(opts, args);
			
			//prints usage information if the help flag is set
			if (cl.hasOption(HELP_SHORT)) {
				HelpFormatter form = new HelpFormatter();
				form.printHelp(APP, HEADER, opts, FOOTER, true);
				return true;
			}
			
			//sets debug to true if the debug flag has been set, default: false
			boolean debugArg = cl.hasOption(DEBUG_SHORT);
			flags.put(FlagKey.DEBUG, debugArg);
			log.info("Debug: {}", String.valueOf(debugArg));
			
			//sets broker ip to the given argument, if the argument has not been set: localhost (127.0.0.1)
			String brokerIp = cl.getOptionValue(IP_SHORT, DEFAULT_IP);
			if (!brokerIp.matches(IP_REGEX)) {
				brokerIp = DEFAULT_IP;
				log.warn("Given broker ip doesn't have the correct format. {} will be used instead", brokerIp);	
			}
			options.put(OptionKey.IP, brokerIp);
			
			log.info("Broker ip: {}", brokerIp);
		}
		catch (ParseException e) {
			log.error("A syntax error occured:");
			log.error(e);
		}
		return false;
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
	 * @param key key of the option you want to retrieve
	 * @return requested option or null if the option does not exist
	 */
	public String getOption(OptionKey key) {
		return options.get(key);
	}
	
	/**
	 * @param key key of the flag you want to retrieve
	 * @return requested flag or null if the flag does not exist
	 */
	public boolean getFlag(FlagKey key) {
		return flags.get(key);
	}
	
	/**
	 * Contains all possible flags.
	 * 
	 * @author Rico Schrage
	 */
	public enum FlagKey {
		HELP, DEBUG
	} 
	
	/**
	 * Contains all possible options.
	 * 
	 * @author Rico Schrage
	 *
	 */
	public enum OptionKey {
		IP
	}
	
}