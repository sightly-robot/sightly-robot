package de.unihannover.swp2015.robots2.robot.abstractrobot;

import de.unihannover.swp2015.robots2.ai.core.AI;
import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.controller.main.RobotMainController;
import de.unihannover.swp2015.robots2.robot.abstractrobot.automate.AbstractAutomate;
import de.unihannover.swp2015.robots2.robot.interfaces.AbstractAI;

import org.eclipse.paho.client.mqttv3.MqttException;

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
	protected AbstractAI ai;

	/** The automate of the robot. */
	protected AbstractAutomate automate;
	
	private boolean connected = false;

	/**
	 * Initializes the AbstractRobot instance by initializing the robot
	 * controller and AI.
	 */
	public AbstractRobot(boolean isHardware) {

		robotController = new RobotMainController(isHardware);
		
		while (!connected) {
			try {
				robotController.startMqtt("tcp://192.168.1.66");	
				connected = true;
			} catch (MqttException me) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
/*		
		while(!robotController.startMqtt("tcp://192.168.1.66"))
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
		
//		IModelObserver mo = new IModelObserver() {
//			@Override
//			public void onModelUpdate(IEvent event) {
//				System.out.println(event.getType().name());
//			}
//		};
//		
//		robotController.getGame().observe(mo);
//		robotController.getGame().getStage().observe(mo);

		// TODO Init AbstractAI
		ai = new AI(robotController);
		ai.setRelativeSpeed(1, 1, 1);

	}
}
