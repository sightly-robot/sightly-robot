package de.unihannover.swp2015.robots2.controller.model;

import de.unihannover.swp2015.robots2.model.writeableInterfaces.IStageWriteable;

public class StageModelController implements IModelController {

	private IStageWriteable stage;
	
	public StageModelController(IStageWriteable stage) {
		this.stage = stage;
	}
	
	@Override
	public void handleMqttMessage(String topic, String message) {
		// TODO Auto-generated method stub
		
	}

}
