/**
 * 
 */
package de.unihannover.swp2015.robots2.controller.main;

import java.util.Arrays;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttException;

import de.unihannover.swp2015.robots2.controller.externalInterfaces.IVisualization;
import de.unihannover.swp2015.robots2.controller.interfaces.IVisualizationController;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttController;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttTopic;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent.UpdateType;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IRobotWriteable;

/**
 * 
 * @author Michael Thies
 */
public class VisualizationMainController extends AbstractMainController implements IVisualizationController {

	private IVisualization visualization;

	public VisualizationMainController() {
		super();

		this.infoComponent = "visualization";
	}

	@Override
	public boolean startMqtt(String brokerUrl) {
		if (this.mqttController == null) {
			String clientId = "visu_" + UUID.randomUUID().toString();
			// TODO subscription list
			String[] subscribeTopics = { "robot/#", "extension/2/robot/#",
					"map/walls", "map/food", "map/food/+",
					"map/occupied/+/set", "event/error/robot/#",
					"extension/2/settings/visualization/#", "control/state"};

			try {
				this.mqttController = new MqttController(brokerUrl, clientId, this, Arrays.asList(subscribeTopics));
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
		if (mqtttopic == null)
			return;

		String key = mqtttopic.getKey(topic);

		switch (mqtttopic) {

		case ROBOT_TYPE:
			this.gameModelController.mqttAddRobot(key, message);
			break;

		case ROBOT_POSITION:
		case ROBOT_SETPOSITION:
			this.robotModelController.mqttRobotPosition(key, message, mqtttopic == MqttTopic.ROBOT_SETPOSITION);
			break;

		case ROBOT_PROGRESS:
			this.robotModelController.mqttRobotProgress(key, message);

		case ROBOT_SCORE:
			this.robotModelController.mqttScoreUpdate(key, message);

		case CONTROL_VIRTUALSPEED:
			this.gameModelController.mqttSetRobotVirtualspeed(Float.parseFloat(message));
			break;

		case CONTROL_HESITATIONTIME:
			this.gameModelController.mqttSetRobotHesitationTime(message);

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

		case SETTINGS_VISU_REQUEST:
			if (this.visualization != null)
				this.sendMqttMessage(MqttTopic.SETTINGS_VISU_RESPONSE, null, this.visualization.getSettings());
			break;

		case SETTINGS_VISU_SET:
			if (this.visualization != null)
				this.visualization.setSettings(message);
			break;

		default:
			break;
		}

	}

	@Override
	public void registerVisualization(IVisualization visualization) {
		this.visualization = visualization;
	}

}
