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

	public static void main(String[] args) {
		new SoftwareRobot(args.length > 0 ? args[0] : null);
	}
}
