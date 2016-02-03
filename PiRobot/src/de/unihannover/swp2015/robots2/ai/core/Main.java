package de.unihannover.swp2015.robots2.ai.core;

import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.controller.main.RobotMainController;

public class Main {
	public static void main(String[] args) {
		IRobotController controller = new RobotMainController(false);
		AI ai = new AI(controller);
	}
}
