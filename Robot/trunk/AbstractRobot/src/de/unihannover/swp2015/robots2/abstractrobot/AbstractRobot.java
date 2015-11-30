package de.unihannover.swp2015.robots2.abstractrobot;

import de.unihannover.swp2015.robots2.aiinterface.AbstractAi;
import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.controller.main.RobotMainController;
import de.unihannover.swp2015.robots2.core.AI;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.robotcontroller.TestRobotController;
import de.unihannover.swp2015.robots2.testai.TestAi;

/**
 * AbstractRobot as parent of Hardware and Software Robot. (Virtual and Real).
 * 
 * @author Lenard Spiecker
 * @author Philipp Rohde
 */
public abstract class AbstractRobot {

	protected IRobotController robotController;
	
	protected AbstractAi ai;
	
	public AbstractRobot() {
		
//		TODO Init IRobotController:
		//robotController = new TestRobotController(true,2,2,Orientation.NORTH);
		robotController = new RobotMainController(true);
		robotController.updatePosition(2, 2, Orientation.NORTH);
		
//		TODO Init AbstractAI
		//ai = new TestAi(robotController);
		ai = new AI(robotController);
		ai.setRelativeSpeed(1, 1, 1);
		
	}
}
