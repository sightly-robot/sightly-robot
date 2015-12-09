package de.unihannover.swp2015.robots2.robot.softwarerobot;

import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.robot.abstractrobot.AbstractRobot;
import de.unihannover.swp2015.robots2.robot.abstractrobot.automate.AbstractAutomate;
import de.unihannover.swp2015.robots2.robot.softwarerobot.automate.SoftwareAutomate;

/**
 * SoftwareRobot is a child of {@link AbstractRobot}.<br>
 * It uses an automate without hardware sensors to drive at the virtual map.
 * 
 * @author Lenard Spiecker
 */
public class SoftwareRobot extends AbstractRobot {

	AbstractAutomate automate;
	

	public SoftwareRobot(String brokerIP) {
		super(false,brokerIP);

		robotController.getMyself().observe(new IModelObserver() {
			
			@Override
			public void onModelUpdate(IEvent event) {
				switch(event.getType())
				{
					case ROBOT_STATE:
						if(robotController.getMyself().isSetupState())
						{
							System.out.println("READY!");
							robotController.setRobotReady();
						}
					break;
				}
			}
		});
		
		automate = new SoftwareAutomate(robotController);
		automate.start();

		ai.setAiEventObserver(automate);
	}
}
