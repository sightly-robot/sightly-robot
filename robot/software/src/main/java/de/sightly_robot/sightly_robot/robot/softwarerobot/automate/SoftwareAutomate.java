package de.sightly_robot.sightly_robot.robot.softwarerobot.automate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.sightly_robot.sightly_robot.controller.interfaces.IRobotController;
import de.sightly_robot.sightly_robot.model.externalInterfaces.IModelObserver;
import de.sightly_robot.sightly_robot.model.interfaces.IEvent;
import de.sightly_robot.sightly_robot.robot.abstractrobot.Direction;
import de.sightly_robot.sightly_robot.robot.abstractrobot.automate.AbstractAutomate;

/**
 * Sample SoftwareAutomate as child of {@link AbstractAutomate}.
 * 
 * It Configures the SoftwareState when GAME_PARAMETER occurs.
 * 
 * @author Lenard Spiecker
 */
public class SoftwareAutomate extends AbstractAutomate {

	/** LOGGER: */
	private static Logger LOGGER = LogManager.getLogger(SoftwareAutomate.class.getName());

	/**
	 * For a new instance of SoftwareAutomate. It accesses the robotcontroller
	 * for getting the vSpeed.
	 * 
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
					LOGGER.info("VSpeed changed!");
					updateVSpeed();
					break;
				default:
					break;
				}
			}
		});
		updateVSpeed();
	}

	private void updateVSpeed() {
		SoftwareState.setVSpeed(SoftwareAutomate.this.robotController.getGame().getVRobotSpeed(),
				SoftwareAutomate.this.robotController.getGame().getVRobotRotationSpeed());
		progressMeasurements[Direction.FORWARDS.ordinal()] = SoftwareState.FOREWARD.getDuration();
		progressMeasurements[Direction.BACKWARDS.ordinal()] = SoftwareState.BACKWARD.getDuration();
		progressMeasurements[Direction.LEFT.ordinal()] = SoftwareState.LEFT.getDuration();
		progressMeasurements[Direction.RIGHT.ordinal()] = SoftwareState.RIGHT.getDuration();
	}

}
