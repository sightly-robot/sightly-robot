package de.unihannover.swp2015.robots2.server.main;

import de.unihannover.swp2015.robots2.controller.interfaces.IServerController;
import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;

/**
 * Game server component, that observes robots, harvests food and credit scores.
 * 
 * @author Michael Thies
 */
public class Harvester implements IModelObserver {
	private final IServerController controller;

	public Harvester(IServerController controller) {
		this.controller = controller;

		this.controller.getGame().observe(this);
	}

	@Override
	public void onModelUpdate(IEvent event) {
		switch (event.getType()) {
		case ROBOT_ADD:
			IRobot r = (IRobot) event.getObject();
			r.observe(this);
			break;
		case ROBOT_DELETE:
			// TODO delete food retained message
			break;

		case ROBOT_POSITION:
			this.onPositionChange((IRobot) event.getObject());
			break;
		default:
			break;
		}
	}

	private void onPositionChange(IRobot robot) {
		int x = robot.getPosition().getX();
		int y = robot.getPosition().getY();
		
		IField field = this.controller
				.getGame()
				.getStage()
				.getField(x,y);
		
		if (field.getFood() != 0) {
			controller.increaseScore(robot.getId(), field.getFood());
			controller.updateFood(x, y, 0);
		}
	}

}
