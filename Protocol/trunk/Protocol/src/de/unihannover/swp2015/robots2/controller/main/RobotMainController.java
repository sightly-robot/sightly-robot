package de.unihannover.swp2015.robots2.controller.main;

import java.util.List;

import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.model.implementation.Robot;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IRobotWriteable;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class RobotMainController extends AbstractMainController implements IRobotController {

	private IRobotWriteable mysqlf;
	
	public RobotMainController(boolean hardwareRobot) {
		super();
		
		this.mysqlf = new Robot("TESTID", hardwareRobot, true);
		this.game.addRobot( this.mysqlf );
	}
	
	@Override
	public void sendInfoMessage(String topic, String message) {
		if( this.mqttController != null ) {
			this.mqttController.sendMessage("event/info/robot/"+this.mysqlf.getId()+"/"+topic, message);
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
		return this.mysqlf;
	}

	@Override
	protected List<String> getMqttSubscribeTopics() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
