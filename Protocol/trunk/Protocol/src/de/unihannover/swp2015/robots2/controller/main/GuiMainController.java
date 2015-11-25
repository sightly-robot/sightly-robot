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
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class GuiMainController extends AbstractMainController implements
		IGuiController {

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
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getRobotSettings(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setVisualizationSettings(String settings) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getVisualizationSettings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void letRobotBlink(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerVisualizationControl(IVisualizationControl control) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerHardwareRobotControl(IHardwareRobotControl control) {
		// TODO Auto-generated method stub
		
	}

}
