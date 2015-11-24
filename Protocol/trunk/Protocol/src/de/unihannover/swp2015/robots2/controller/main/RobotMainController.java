package de.unihannover.swp2015.robots2.controller.main;

import java.util.Arrays;

import org.eclipse.paho.client.mqttv3.MqttException;

import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttController;
import de.unihannover.swp2015.robots2.model.implementation.Robot;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IRobotWriteable;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class RobotMainController extends AbstractMainController implements
		IRobotController {

	private IRobotWriteable myself;

	public RobotMainController(boolean hardwareRobot) {
		super();

		// TODO generate proper ID
		String id = "TESTID";
		this.myself = new Robot(id, hardwareRobot, true);
		this.game.addRobot(this.myself);

		this.infoComponent = "robot/" + id;
	}

	@Override
	public boolean startMqtt(String brokerUrl) {
		if (this.mqttController == null) {
			String clientId = "robot_" + this.myself.getId();
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
		// TODO Auto-generated method stub

	}

	@Override
	public void updatePosition(int x, int y, Orientation orientation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updatePositionProgress(int progress) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestField(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void releaseField(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRobotReady() {
		// TODO Auto-generated method stub

	}

	@Override
	public IRobot getMyself() {
		return this.myself;
	}

}
