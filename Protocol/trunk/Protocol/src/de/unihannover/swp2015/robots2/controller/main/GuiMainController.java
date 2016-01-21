package de.unihannover.swp2015.robots2.controller.main;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttException;

import de.unihannover.swp2015.robots2.controller.externalInterfaces.IHardwareRobotControl;
import de.unihannover.swp2015.robots2.controller.externalInterfaces.IVisualizationControl;
import de.unihannover.swp2015.robots2.controller.interfaces.IGuiController;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttController;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttTopic;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IField.State;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot.RobotState;

/**
 * Main controller specialized for control guis.
 * 
 * @version 0.2
 * @author Michael Thies
 */
public class GuiMainController extends AbstractMainController implements
		IGuiController {

	IVisualizationControl visualizationControl;
	IHardwareRobotControl hardwareRobotControl;

	public GuiMainController() {
		super();

		// Start MQTTController
		try {
			String clientId = "gui_"
					+ UUID.randomUUID().toString().substring(0, 8);

			MqttTopic[] extendedTopics = { MqttTopic.MAP_STARTPOSITIONS,
					MqttTopic.SETTINGS_ROBOT_RESPONSE,
					MqttTopic.SETTINGS_ROBOT_SET,
					MqttTopic.SETTINGS_VISU_RESPONSE,
					MqttTopic.SETTINGS_VISU_SET };
			String[] subscribeTopics = this.getSubscribeTopcis(extendedTopics);

			this.mqttController = new MqttController(clientId, this,
					Arrays.asList(subscribeTopics));
		} catch (MqttException e) {
			log.fatal("Error constructing MqttController:", e);
		}
	}

	@Override
	public void handleMqttMessage(String topic, String message) {
		MqttTopic mqtttopic = MqttTopic.getBy(topic);
		if (mqtttopic == null)
			return;

		String key = mqtttopic.getKey(topic);

		switch (mqtttopic) {

		case MAP_STARTPOSITIONS:
			this.stageModelController.mqttSetStartpositions(message);
			break;

		case SETTINGS_ROBOT_RESPONSE:
		case SETTINGS_ROBOT_SET:
			if (this.hardwareRobotControl != null)
				this.hardwareRobotControl.receiveSettings(message, key);
			break;

		case SETTINGS_VISU_RESPONSE:
		case SETTINGS_VISU_SET:
			if (this.visualizationControl != null)
				this.visualizationControl.receiveSettings(message);
			break;

		default:
			this.processGeneralMessage(mqtttopic, key, message);
			break;
		}

	}

	@Override
	public void sendWalls(List<List<Set<Orientation>>> walls) {
		int height = walls.size();
		if (height == 0)
			throw new IllegalArgumentException(
					"Height of stage must be greater than 0.");
		int width = walls.get(0).size();
		if (width == 0)
			throw new IllegalArgumentException(
					"Width of sagte must be greater than 0.");

		StringBuilder sb = new StringBuilder();
		sb.append(Integer.toString(width));
		sb.append(',');
		sb.append(Integer.toString(height));
		for (List<Set<Orientation>> row : walls) {
			if (row.size() != width)
				throw new IllegalArgumentException(
						"All rows of stage must have equal length.");
			for (Set<Orientation> field : row) {
				sb.append(',');
				for (Orientation wall : field) {
					sb.append(wall.toString());
				}
			}
		}

		this.sendMqttMessage(MqttTopic.MAP_WALLS, null, sb.toString());
	}

	@Override
	public void sendStartPositions(List<IPosition> positions) {
		StringBuilder sb = new StringBuilder();
		String prefix = "";
		for (IPosition p : positions) {
			sb.append(prefix);
			prefix = ",";
			sb.append(p.getX() + "," + p.getY() + ","
					+ p.getOrientation().toString());
		}

		this.sendMqttMessage(MqttTopic.MAP_STARTPOSITIONS, null, sb.toString());
	}

	@Override
	public void sendFood(List<List<Integer>> food) {
		this.sendMqttMessage(MqttTopic.MAP_INIT_FOOD, null,
				food2String(food));
	}

	@Override
	public void sendGrowingRates(List<List<Integer>> growingRates) {
		this.sendMqttMessage(MqttTopic.MAP_INIT_GROWINGRATE, null,
				food2String(growingRates));
	}

	@Override
	@Deprecated
	public void sendGameParameters(float robotSpeed, int hesitationTime) {
		this.sendMqttMessage(MqttTopic.CONTROL_VIRTUALSPEED, null,
				Float.toString(robotSpeed));
	}
	
	@Override
	public void sendVRobotSpeed(float robotSpeed, float robotRotationSpeed) {
		this.sendMqttMessage(MqttTopic.CONTROL_VIRTUALSPEED, null,
				Float.toString(robotSpeed) + "," + Float.toString(robotRotationSpeed));
	}

	@Override
	@Deprecated
	public void setRobotPosition(int x, int y, Orientation orientation,
			IRobot robot) {
		this.setRobotPosition(x, y, orientation, robot.getId());
	}

	@Override
	public void setRobotPosition(int x, int y, Orientation orientation,
			String robot) {
		String message = Integer.toString(x) + "," + Integer.toString(y) + ","
				+ orientation.toString();
		this.sendMqttMessage(MqttTopic.ROBOT_SETPOSITION, robot,
				message);
	}

	@Override
	public void startGame() {
		this.sendMqttMessage(MqttTopic.CONTROL_STATE, null, "running");
	}

	@Override
	public void stopGame() {
		this.sendMqttMessage(MqttTopic.CONTROL_STATE, null, "stopped");
	}

	@Override
	public void resetGame() {
		this.sendMqttMessage(MqttTopic.CONTROL_RESET, null, "reset");
	}

	@Override
	public void setRobotSettings(String id, String settings) {
		this.sendMqttMessage(MqttTopic.SETTINGS_ROBOT_SET, id, settings);
	}

	@Override
	public void getRobotSettings(String id) {
		this.sendMqttMessage(MqttTopic.SETTINGS_ROBOT_REQUEST, id, "");
	}

	@Override
	public void setVisualizationSettings(String settings) {
		this.sendMqttMessage(MqttTopic.SETTINGS_VISU_SET, null, settings);
	}

	@Override
	public void getVisualizationSettings() {
		this.sendMqttMessage(MqttTopic.SETTINGS_VISU_REQUEST, null, "");
	}

	@Override
	public void letRobotBlink(String id) {
		this.sendMqttMessage(MqttTopic.ROBOT_BLINK, id, "");
	}

	@Override
	public void deleteRobot(String id) {
		// Delete robot and retained messages concerning it
		this.sendMqttMessage(MqttTopic.ROBOT_TYPE, id, null);
		this.sendMqttMessage(MqttTopic.ROBOT_POSITION, id, null);
		this.sendMqttMessage(MqttTopic.ROBOT_STATE, id, null);

		// Release all fields occupied by this robot
		for (int x = 0; x < this.game.getStage().getWidth(); x++) {
			for (int y = 0; y < this.game.getStage().getHeight(); y++) {
				IField f = this.game.getStage().getField(x, y);
				if (f.getState() == State.OCCUPIED
						&& f.getLockedBy().equals(id))
					this.sendMqttMessage(MqttTopic.FIELD_OCCUPIED_RELEASE, x
							+ "-" + y, "");
			}
		}
	}

	@Override
	public void disableRobot(String id) {
		this.sendMqttMessage(MqttTopic.ROBOT_STATE, id,
				RobotState.MANUAL_DISABLED_GUI.toString());
	}

	@Override
	public void registerVisualizationControl(IVisualizationControl control) {
		this.visualizationControl = control;
	}

	@Override
	public void registerHardwareRobotControl(IHardwareRobotControl control) {
		this.hardwareRobotControl = control;
	}

	/**
	 * Convert a two dimensional list of integers to a string to be sent via
	 * MQTT as current food or growing rate.
	 * 
	 * @param food
	 *            List
	 * @return String: <width>,<height>,<f>,<f>,...
	 */
	private static String food2String(List<List<Integer>> food) {
		int height = food.size();
		if (height == 0)
			throw new IllegalArgumentException(
					"Height of stage must be greater than 0.");
		int width = food.get(0).size();
		if (width == 0)
			throw new IllegalArgumentException(
					"Width of stage must be greater than 0.");

		StringBuilder sb = new StringBuilder();
		sb.append(Integer.toString(width));
		sb.append(',');
		sb.append(Integer.toString(height));
		for (List<Integer> row : food) {
			if (row.size() != width)
				throw new IllegalArgumentException(
						"All rows of stage must have equal length.");
			for (Integer field : row) {
				sb.append(',');
				sb.append(field.toString());
			}
		}

		return sb.toString();
	}

}
