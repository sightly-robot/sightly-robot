package de.unihannover.swp2015.robots2.robot.softwarerobot;

/**
 * Launch this class to get a full working SoftwareRobot.
 * @author Lenard Spiecker
 *
 */
public class Main {

	public static void main(String[] args) {
		new SoftwareRobot(args.length>1?args[1]:null);
	}
}
