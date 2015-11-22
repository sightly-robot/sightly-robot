package de.unihannover.swp2015.robots2.controller.main;

import java.util.List;

import de.unihannover.swp2015.robots2.controller.interfaces.IServerController;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class ServerMainController extends AbstractMainController implements IServerController {

	public ServerMainController() {
		super();
	}
	
	@Override
	public void sendInfoMessage(String topic, String message) {
		if( this.mqttController != null ) {
			this.mqttController.sendMessage("event/info/server/"+topic, message);
		}	
	}

	@Override
	public void handleMqttMessage(String topic, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateScore(String robotId, int score) {
		this.game.getRobotsWriteable().get(robotId).addScore(score);
	}

	@Override
	public void updateFood(int x, int y, int value) {
		for( int i=0; i<value; i++ ) {
			this.game.getStageWriteable().getFieldWriteable(x, y).incrementFood();
		}
	}

	@Override
	protected List<String> getMqttSubscribeTopics() {
		// TODO Auto-generated method stub
		return null;
	}
}
