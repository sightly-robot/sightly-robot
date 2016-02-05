package de.sightly_robot.sightly_robot.ai.core;

import de.sightly_robot.sightly_robot.controller.interfaces.IRobotController;
import de.sightly_robot.sightly_robot.controller.main.RobotMainController;

public class Main {
	public static void main(String[] args) {
		IRobotController controller = new RobotMainController(false);
		AI ai = new AI(controller);
	}
}
