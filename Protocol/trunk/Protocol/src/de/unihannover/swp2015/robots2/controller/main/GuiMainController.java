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
import de.unihannover.swp2015.robots2.model.interfaces.IEvent.UpdateType;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IRobotWriteable;

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
		this.infoComponent = "gui";
	}

	@Override
	public boolean startMqtt(String brokerUrl) {
		if (this.mqttController == null) {
			String clientId = "gui_" + UUID.randomUUID().toString();
			// TODO subscription list
			String[] subscribeTopics = {
					"robot/#",
					"map/walls",
					"map/food",
					"map/food/+",
					"event/error/robot/#",
					"extension/2/settings/#",
			};

			try {
				this.mqttController = new MqttController(brokerUrl, clientId,
						this, Arrays.asList(subscribeTopics));
				return true;
			} catch (MqttException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return true;
		}
	}

	@Override
	public void handleMqttMessage(String topic, String message) {
		MqttTopic mqtttopic = MqttTopic.getBy(topic);
		String key = mqtttopic.getKey(topic);

		switch (mqtttopic) {

		case ROBOT_TYPE:
			this.gameModelController.mqttAddRobot(key, message);
			break;

		case ROBOT_POSITION:
		case ROBOT_SETPOSITION:
			this.robotModelController.mqttRobotPosition(key, message,
					mqtttopic == MqttTopic.ROBOT_SETPOSITION);
			break;
		
		case ROBOT_SCORE:
			this.robotModelController.mqttScoreUpdate(key,message);

		case CONTROL_VIRTUALSPEED:
			this.gameModelController.mqttSetRobotVirtualspeed(Float
					.valueOf(message));
			break;

		case MAP_WALLS:
			this.stageModelController.mqttSetWalls(message);
			break;

		case MAP_FOOD:
			this.stageModelController.mqttSetFood(message);
			break;

		case FIELD_FOOD:
			this.stageModelController.mqttSetFieldFood(key, message);
			break;

		case CONTROL_STATE:
			this.gameModelController.mqttSetGameState(message);
			break;

		case EVENT_ERROR_ROBOT_CONNECTION:
		case EVENT_ERROR_ROBOT_ROBOTICS:
			IRobotWriteable r = this.game.getRobotsWriteable().get(key);
			r.setErrorState(true);
			r.emitEvent(UpdateType.ROBOT_STATE);
			break;

		// TODO complete message handling

		case SETTINGS_ROBOT_RESPONSE:
		case SETTINGS_ROBOT_SET:
			if (this.hardwareRobotControl != null)
				this.hardwareRobotControl.receiveSettings(message, key);
			break;

		case SETTINGS_VISU_RESPONSE:
		case SETTINGS_VISU_SET:
			if (this.visualizationControl != null)
				this.visualizationControl.receiveSettings(message);

		default:
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
	public void sendFood(List<List<Integer>> food) {
		this.sendMqttMessage(MqttTopic.MAP_INIT_FOOD, null,
				this.food2String(food));
	}

	@Override
	public void sendGrowingRates(List<List<Integer>> growingRates) {
		this.sendMqttMessage(MqttTopic.MAP_INIT_FOOD, null,
				this.food2String(growingRates));
	}

	@Override
	public void sendGameParameters(float robotSpeed, int hesitationTime) {
		this.sendMqttMessage(MqttTopic.CONTROL_VIRTUALSPEED, null,
				Float.toString(robotSpeed));
		this.sendMqttMessage(MqttTopic.CONTROL_HESITATIONTIME, null,
				Integer.toString(hesitationTime));
	}

	@Override
	public void setRobotPosition(int x, int y, Orientation orientation,
			IRobot robot) {
		String message = Integer.toString(x) + "," + Integer.toString(y) + ","
				+ orientation.toString();
		this.sendMqttMessage(MqttTopic.ROBOT_SETPOSITION, robot.getId(),
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
		// Reset robots' scores
		for (IRobot r : this.game.getRobots().values())
			this.sendMqttMessage(MqttTopic.ROBOT_SCORE, r.getId(), "0");
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
	private String food2String(List<List<Integer>> food) {
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
