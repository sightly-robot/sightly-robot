package de.unihannover.swp2015.robots2.controller.main;

import java.util.Arrays;

import org.eclipse.paho.client.mqttv3.MqttException;

import de.unihannover.swp2015.robots2.controller.interfaces.IServerController;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttController;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttTopic;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent.UpdateType;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IFieldWriteable;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IRobotWriteable;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class ServerMainController extends AbstractMainController implements IServerController {

	public ServerMainController() {
		super();

		this.infoComponent = "server";
	}

	@Override
	public boolean startMqtt(String brokerUrl) {
		if (this.mqttController == null) {
			String clientId = "server";
			String[] subscribeTopics = { "robot/#", "map/walls", "extension/2/map/setfood",
					"extension/2/map/setgrowrate", "map/occupied/#", "control/state",
					"extension/2/robot/hesitationtime", "event/error/robot/#" };

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

		case MAP_WALLS:
			this.stageModelController.mqttSetWalls(message);
			break;

		case MAP_INIT_FOOD:
			this.stageModelController.mqttSetFood(message);
			break;

		case MAP_INIT_GROWINGRATE:
			this.stageModelController.mqttSetGrowingrate(message);
			break;

		case FIELD_OCCUPIED_LOCK:
			this.fieldStateModelController.mqttFieldLock(key, message);
			break;

		case FIELD_OCCUPIED_SET:
			this.fieldStateModelController.mqttFieldOccupy(key, message);
			break;

		case FIELD_OCCUPIED_RELEASE:
			this.fieldStateModelController.mqttFieldRelease(key, message);
			break;

		case CONTROL_STATE:
			this.gameModelController.mqttSetGameState(message);
			break;

		case CONTROL_VIRTUALSPEED:
			this.gameModelController.mqttSetRobotVirtualspeed(Float.parseFloat(message));
			break;

		case CONTROL_HESITATIONTIME:
			this.gameModelController.mqttSetRobotHesitationTime(message);
			break;

		case EVENT_ERROR_ROBOT_CONNECTION:
		case EVENT_ERROR_ROBOT_ROBOTICS:
			IRobotWriteable r = this.game.getRobotsWriteable().get(key);
			r.setErrorState(true);
			r.emitEvent(UpdateType.ROBOT_STATE);
			break;

		default:
			break;
		}

	}

	@Override
	public void updateScore(String robotId, int score) {
		this.game.getRobotsWriteable().get(robotId).addScore(score);
	}

	@Override
	public void increaseScore(String robotId, int points) {
		int newScore = this.game.getRobotsWriteable().get(robotId).addScore(points);
		this.sendMqttMessage(MqttTopic.ROBOT_SCORE, robotId, Integer.toString(newScore));
	}

	@Override
	public void updateFood(int x, int y, int value) {
		IFieldWriteable f = this.game.getStageWriteable().getFieldWriteable(x, y);
		f.setFood(value);
		this.sendMqttMessage(MqttTopic.MAP_FOOD, (x + "-" + y), Integer.toString(value));
		f.emitEvent(UpdateType.FIELD_FOOD);
	}

	@Override
	public void increaseFood(int x, int y, int value) {
		IFieldWriteable f = this.game.getStageWriteable().getFieldWriteable(x, y);
		int newFood = f.incrementFood(value);
		this.sendMqttMessage(MqttTopic.MAP_FOOD, (x + "-" + y), Integer.toString(newFood));
		f.emitEvent(UpdateType.FIELD_FOOD);		
	}
}
