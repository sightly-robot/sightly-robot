package de.unihannover.swp2015.robots2.robot.softwarerobot.automate;

import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.robot.abstractrobot.automate.AbstractAutomate;

/**
 * Sample SoftwareAutomate as child of {@link AbstractAutomate}.
 * 
 * It Configures the SoftwareState when GAME_PARAMETER occurs.
 * 
 * @author Lenard Spiecker
 */
public class SoftwareAutomate extends AbstractAutomate {

	/**
	 * For a new instance of SoftwareAutomate. 
	 * It accesses the robotcontroller for getting the vSpeed.
	 * @param robotController
	 */
	public SoftwareAutomate(IRobotController robotController) {
		super(robotController, SoftwareState.WAIT, SoftwareState.SETUP, SoftwareState.DISABLED,
				SoftwareState.CONNECTED);
		robotController.getGame().observe(new IModelObserver() {
			@Override
			public void onModelUpdate(IEvent event) {
				switch (event.getType()) {
				case GAME_PARAMETER:
					SoftwareState.setVSpeed(SoftwareAutomate.this.robotController.getGame().getVRobotSpeed());
					for(int i=0;i<progressMeasurements.length;i++)
					{
						progressMeasurements[i] = SoftwareAutomate.this.robotController.getGame().getVRobotSpeed()*1000;
					}
					break;
				default:
					break;
				}
			}
		});
	}

}
