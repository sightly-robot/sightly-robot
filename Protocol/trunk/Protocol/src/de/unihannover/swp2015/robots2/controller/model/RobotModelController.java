package de.unihannover.swp2015.robots2.controller.model;

import java.util.Map;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent.UpdateType;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IRobotWriteable;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class RobotModelController {

	private Map<String, IRobotWriteable> robots;

	public RobotModelController(Map<String, IRobotWriteable> robots) {
		this.robots = robots;
	}

	/**
	 * 
	 * @param key
	 *            ID of the robot extracted from MQTT topic
	 * @param message
	 *            MQTT payload
	 * @param setPosition
	 *            true if the MQTT topic was ROBOT_SETPOSITION
	 */
	public void mqttRobotPosition(String key, String message,
			boolean setPosition) {
		IRobotWriteable r = this.robots.get(key);

		// Only update position if other robot or explicit external command to
		// reset position.
		if (r != null && (setPosition || !r.isMyself())) {

			String[] positionParts = message.split(",");

			r.setPosition(Integer.valueOf(positionParts[0]),
					Integer.valueOf(positionParts[1]),
					Orientation.getBy(positionParts[2]));
			r.emitEvent(UpdateType.ROBOT_POSITION);

			if (setPosition) {
				r.setSetupState(true);
				r.emitEvent(UpdateType.ROBOT_STATE);
			}

		}
	}
}
