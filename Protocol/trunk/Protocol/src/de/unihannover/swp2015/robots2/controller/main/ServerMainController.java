package de.unihannover.swp2015.robots2.controller.main;

import java.util.Arrays;

import org.eclipse.paho.client.mqttv3.MqttException;

import de.unihannover.swp2015.robots2.controller.interfaces.IServerController;
import de.unihannover.swp2015.robots2.controller.interfaces.InfoType;
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
public class ServerMainController extends AbstractMainController implements
		IServerController {

	public ServerMainController() {
		super();

		this.infoComponent = "server";
	}

	@Override
	public boolean startMqtt(String brokerUrl) {
		if (this.mqttController == null) {
			String clientId = "server";
			String[] subscribeTopics = { "robot/#", "map/walls",
					"extension/2/map/setfood", "extension/2/map/setgrowrate",
					"map/occupied/#", "control/state",
					"extension/2/robot/hesitationtime", "event/error/robot/#" };

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
		if (mqtttopic == null)
			return;

		String key = mqtttopic.getKey(topic);

		switch (mqtttopic) {
		case ROBOT_TYPE:
			this.gameModelController.mqttAddRobot(key, message);
			if (message.equals(""))
				this.mqttController.deleteRetainedMessage(MqttTopic.ROBOT_SCORE
						.toString(key));
			break;

		case ROBOT_POSITION:
		case ROBOT_SETPOSITION:
			this.robotModelController.mqttRobotPosition(key, message,
					mqtttopic == MqttTopic.ROBOT_SETPOSITION);
			break;

		case MAP_WALLS:
			int[] oldSize = { this.game.getStage().getWidth(),
					this.game.getStage().getHeight() };
			int[] newSize = this.stageModelController.mqttSetWalls(message);

			// If received message is valid and stage has enlarged send current
			// food state (=0) for new fields
			if (newSize != null) {
				this.sendInfoMessage(InfoType.INFO, "food",
						"New walls received - sending current food.");
				this.echoNewFood(oldSize[0], oldSize[1], newSize[0], newSize[1]);
			}
			break;

		case MAP_INIT_FOOD:
			int[] size = this.stageModelController.mqttSetFood(message);

			// If received message is valid echo received food for all fields
			if (size != null) {
				this.sendInfoMessage(InfoType.INFO, "food",
						"New initial food received - echoing.");
				this.echoCompleteFood(size[0], size[1]);
			}
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
			this.gameModelController.mqttSetRobotVirtualspeed(Float
					.parseFloat(message));
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
		this.sendMqttMessage(MqttTopic.ROBOT_SCORE, robotId,
				Integer.toString(score));
	}

	@Override
	public void increaseScore(String robotId, int points) {
		int newScore = this.game.getRobotsWriteable().get(robotId)
				.addScore(points);
		this.sendMqttMessage(MqttTopic.ROBOT_SCORE, robotId,
				Integer.toString(newScore));
	}

	@Override
	public void updateFood(int x, int y, int value) {
		IFieldWriteable f = this.game.getStageWriteable().getFieldWriteable(x,
				y);
		f.setFood(value);
		this.sendMqttMessage(MqttTopic.FIELD_FOOD, (x + "-" + y),
				Integer.toString(value));
		f.emitEvent(UpdateType.FIELD_FOOD);
	}

	@Override
	public void increaseFood(int x, int y, int value) {
		IFieldWriteable f = this.game.getStageWriteable().getFieldWriteable(x,
				y);
		int newFood = f.incrementFood(value);
		this.sendMqttMessage(MqttTopic.FIELD_FOOD, (x + "-" + y),
				Integer.toString(newFood));
		f.emitEvent(UpdateType.FIELD_FOOD);
	}

	/**
	 * Sends current food state for each single field as mqtt message to
	 * FIELD_FOOD. The size of the transmitted part of the Stage can be
	 * restricted by the parameter.
	 * 
	 * @param maxX
	 *            Last column that will be transmitted
	 * @param maxY
	 *            Last row that will be transmitted
	 */
	private void echoCompleteFood(int maxX, int maxY) {
		for (int y = 0; y < Math.min(this.game.getStage().getHeight(), maxY); y++) {
			for (int x = 0; x < Math.min(this.game.getStage().getWidth(), maxX); x++) {
				this.sendMqttMessage(
						MqttTopic.FIELD_FOOD,
						(x + "-" + y),
						Integer.toString(this.game.getStage().getField(x, y)
								.getFood()));
			}
		}
	}

	/**
	 * Sends current food state for each single field that as added while
	 * changing map size and delete retained food messages for fields that were
	 * delete while changing map size.
	 * 
	 * @param oldWidth
	 *            Width of the map before resize
	 * @param oldHeight
	 *            Height of the map before resize
	 * @param newWidth
	 *            Width of the map after resize
	 * @param newHeight
	 *            Height of the map after resize
	 * 
	 */
	private void echoNewFood(int oldWidth, int oldHeight, int newWidth,
			int newHeight) {

		// Delete retained food of obsolete rows
		if (newHeight < oldHeight) {
			for (int y = oldHeight - 1; y > newHeight - 1; y--) {
				for (int x = 0; x < oldWidth; x++) {
					this.mqttController
							.deleteRetainedMessage(MqttTopic.FIELD_FOOD
									.toString(x + "-" + y));
				}
			}
			// ... or send food for new rows
		} else if (newHeight > oldHeight) {
			for (int y = oldHeight; y < newHeight; y++) {
				for (int x = 0; x < newWidth; x++) {
					this.sendMqttMessage(
							MqttTopic.FIELD_FOOD,
							(x + "-" + y),
							Integer.toString(this.game.getStage()
									.getField(x, y).getFood()));
				}
			}
		}

		// For all rows still existing from before
		for (int y = 0; y < Math.min(oldHeight, newHeight); y++) {
			// Delete retained food for obsolete fields at the end
			if (newWidth < oldWidth) {
				for (int x = oldWidth - 1; x > newWidth - 1; x--) {
					this.mqttController
							.deleteRetainedMessage(MqttTopic.FIELD_FOOD
									.toString(x + "-" + y));
				}
				// Send food for new fields
			} else if (newWidth > oldWidth) {
				for (int x = oldWidth; x < newWidth; x++) {
					this.sendMqttMessage(
							MqttTopic.FIELD_FOOD,
							(x + "-" + y),
							Integer.toString(this.game.getStage()
									.getField(x, y).getFood()));
				}
			}
		}
	}
}
