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
	
	boolean ready = false;

	public SoftwareRobot() {
		super(false);

		robotController.getMyself().observe(new IModelObserver() {
			
			@Override
			public void onModelUpdate(IEvent event) {
				switch(event.getType())
				{
					case ROBOT_POSITION:
						if(!ready)
						{
							robotController.setRobotReady();
							ready = true;
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
