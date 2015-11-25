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
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;

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
			String[] subscribeTopics = {};

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

		// TODO message handling

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
		// TODO Auto-generated method stub

	}

	@Override
	public void sendFood(List<List<Integer>> food) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendGrowingRates(List<List<Integer>> growingRates) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendGameParameters(float robotSpeed, int hesitationTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRobotPosition(int x, int y, Orientation orientation,
			IRobot robot) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startGame() {
		// TODO
	}

	@Override
	public void stopGame() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetGame() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRobotSettings(String id, String settings) {
		this.sendMqttMessage(MqttTopic.SETTINGS_ROBOT_SET.toString(id),
				settings);
	}

	@Override
	public void getRobotSettings(String id) {
		this.sendMqttMessage(MqttTopic.SETTINGS_ROBOT_REQUEST.toString(id), "");
	}

	@Override
	public void setVisualizationSettings(String settings) {
		this.sendMqttMessage(MqttTopic.SETTINGS_VISU_SET.toString(), settings);
	}

	@Override
	public void getVisualizationSettings() {
		this.sendMqttMessage(MqttTopic.SETTINGS_VISU_REQUEST.toString(), "");
	}

	@Override
	public void letRobotBlink(String id) {
		this.sendMqttMessage(MqttTopic.ROBOT_BLINK.toString(id), "");
	}

	@Override
	public void registerVisualizationControl(IVisualizationControl control) {
		this.visualizationControl = control;
	}

	@Override
	public void registerHardwareRobotControl(IHardwareRobotControl control) {
		this.hardwareRobotControl = control;
	}

}
