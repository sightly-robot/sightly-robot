package de.unihannover.swp2015.robots2.abstractrobot;

import de.unihannover.swp2015.robots2.abstractrobot.automate.AbstractAutomate;
import de.unihannover.swp2015.robots2.aiinterface.AbstractAi;
import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.controller.main.RobotMainController;
import de.unihannover.swp2015.robots2.core.AI;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

/**
 * The AbstractRobot is the base class for all hardware (real) and software
 * (virtual) robots.
 * 
 * @author Lenard Spiecker
 * @author Philipp Rohde
 */
public abstract class AbstractRobot {

	/** The controller of the robot. */
	protected IRobotController robotController;

	/** The AI of the robot. */
	protected AbstractAi ai;

	/** The automate of the robot. */
	protected AbstractAutomate automate;

	/**
	 * Initializes the AbstractRobot instance by initializing the robot
	 * controller and AI.
	 */
	public AbstractRobot() {
		// TODO Init IRobotController:
		// robotController = new
		// TestRobotController(true,2,2,Orientation.NORTH);
		robotController = new RobotMainController(true);
		robotController.updatePosition(2, 2, Orientation.NORTH);

		// TODO Init AbstractAI
		// ai = new TestAi(robotController);
		ai = new AI(robotController);
		ai.setRelativeSpeed(1, 1, 1);

	}
}
