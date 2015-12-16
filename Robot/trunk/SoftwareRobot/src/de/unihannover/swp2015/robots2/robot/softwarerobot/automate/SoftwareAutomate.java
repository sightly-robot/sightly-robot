package de.unihannover.swp2015.robots2.robot.softwarerobot.automate;

import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.robot.abstractrobot.automate.AbstractAutomate;

/**
 * Sample SoftwareAutomate as child of {@link AbstractAutomate}.
 * 
 * @author Lenard Spiecker
 */
public class SoftwareAutomate extends AbstractAutomate {

	public SoftwareAutomate(IRobotController robotController) {
		super(robotController, SoftwareState.WAIT, SoftwareState.SETUP, SoftwareState.DISABLED,
				SoftwareState.CONNECTED);
		robotController.getGame().observe(new IModelObserver() {
			@Override
			public void onModelUpdate(IEvent event) {
				switch (event.getType()) {
				case GAME_PARAMETER:
					// SoftwareState.setVSpeed(SoftwareAutomate.this.robotController.getGame().getVRobotSpeed());
					break;
				default:
					break;
				}
			}
		});
	}

}
