package de.unihannover.swp2015.robots2.robot.softwarerobot;

/**
 * Launch this class to get a full working SoftwareRobot.
 * 
 * @author Lenard Spiecker
 *
 */
public class Main {

	/** LOGGER: */
	// private static Logger LOGGER =
	// LogManager.getLogger(Main.class.getName());
	private static final boolean USE_YETANOTHERAI = false;

	/**
	 * Starts a new SoftwareRobot. 
	 * Arguments are brokerIP(String) and useYetAnotherAi(Boolean)
	 * 
	 * Example:
	 * 
	 * main(); reads brokerip from properties useYetAnotherAi = false
	 * main(localhost); useYetAnotherAi = false
	 * main(localhost true)
	 * @param args
	 */
	public static void main(String[] args) {
		new SoftwareRobot(args.length > 0 ? args[0] : null,args.length > 1 ? Boolean.parseBoolean(args[1]):USE_YETANOTHERAI);
	}
}
