package de.unihannover.swp2015.robots2.robot.softwarerobot;

/**
 * Launch this class to get a full working SoftwareRobot.
 * @author Lenard Spiecker
 *
 */
public class Main {

	public static void main(String[] args) {
		System.out.println("Found Argument: "+args[0]);
		new SoftwareRobot(args.length>0?args[0]:null);
	}
}
