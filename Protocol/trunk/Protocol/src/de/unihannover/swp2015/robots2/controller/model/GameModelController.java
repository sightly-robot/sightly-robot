package de.unihannover.swp2015.robots2.controller.model;

import de.unihannover.swp2015.robots2.model.writeableInterfaces.IGameWriteable;

public class GameModelController implements IModelController {

	private IGameWriteable game;
	
	public GameModelController(IGameWriteable game) {
		this.game = game;
	}

	@Override
	public void handleMqttMessage(String topic, String message) {
		// TODO Auto-generated method stub
		
	}
}
