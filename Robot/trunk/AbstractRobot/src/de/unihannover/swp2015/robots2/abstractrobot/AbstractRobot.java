package de.unihannover.swp2015.robots2.abstractrobot;

import de.unihannover.swp2015.robots2.aiinterface.AbstractAi;
import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;

/**
 * AbstractRobot as parent of Hardware and Software Robot. (Virtual and Real)
 * @author Lenard
 */
public class AbstractRobot {

	protected IRobotController iRobotController;
	
	protected AbstractAi abstractAi;
	
	public AbstractRobot() {
		
//		TODO Init IRobotController:
//		iRobotController = new IRobotController();
		
//		TODO Init AbstractAI
//		abstractAi = new AbstractAi(iRobotController);
		abstractAi.setRelativeSpeed(1, 1, 1);
		
	}
}
