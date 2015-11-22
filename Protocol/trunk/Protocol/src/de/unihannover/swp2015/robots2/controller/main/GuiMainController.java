package de.unihannover.swp2015.robots2.controller.main;

import java.util.List;

import de.unihannover.swp2015.robots2.controller.externalInterfaces.IHardwareRobotControl;
import de.unihannover.swp2015.robots2.controller.externalInterfaces.IVisualizationControl;
import de.unihannover.swp2015.robots2.controller.interfaces.IGuiController;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class GuiMainController extends AbstractMainController implements IGuiController {

	public GuiMainController() {
		super();
	}
	
	@Override
	public void sendInfoMessage(String topic, String message) {
		if( this.mqttController != null ) {
			this.mqttController.sendMessage("event/info/gui/"+topic, message);
		}	
	}

	@Override
	public void handleMqttMessage(String topic, String message) {
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
	public void setRobotPosition(int x, int y, IRobot robot, Orientation orientation) {
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
	public void setRobotSettings(String id, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getRobotSettings(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setVisualizationSettings(String value) {
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

	@Override
	protected List<String> getMqttSubscribeTopics() {
		// TODO Auto-generated method stub
		return null;
	}

}
