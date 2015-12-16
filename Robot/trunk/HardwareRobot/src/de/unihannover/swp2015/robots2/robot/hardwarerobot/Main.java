package de.unihannover.swp2015.robots2.robot.hardwarerobot;


/**
 * The main class only creates a new {@link HardwareRobot}.
 * 
 * @author Philipp Rohde
 */
public class Main {

	/**
	 * Starts the Pi2Go.
	 * 
	 * @param args
	 *            the command-line arguments
	 */
	public static void main(String[] args) {
		System.out.println("Found Argument: "+args[0]);
		new HardwareRobot(args.length>0?args[0]:null);
	}
}
