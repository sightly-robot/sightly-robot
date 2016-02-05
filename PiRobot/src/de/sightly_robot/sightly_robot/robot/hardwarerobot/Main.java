package de.sightly_robot.sightly_robot.robot.hardwarerobot;

/**
 * The main class only creates a new {@link HardwareRobot}.
 * 
 * @author Philipp Rohde
 */
public class Main {

	private static final boolean USE_YETANOTHERAI = false;
	/**
	 * Starts a new HardwareRobot. 
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
		new HardwareRobot(args.length > 0 ? args[0] : null,args.length > 1 ? Boolean.parseBoolean(args[1]):USE_YETANOTHERAI);
	}
}
