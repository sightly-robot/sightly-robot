package de.unihannover.swp2015.robots2.server.main;

import de.unihannover.swp2015.robots2.controller.interfaces.IServerController;
import de.unihannover.swp2015.robots2.controller.interfaces.InfoType;
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
			this.onPositionChange(r);
			break;
		case ROBOT_DELETE:
			// TODO delete food retained message
			break;

		case GAME_STATE:
			if (this.controller.getGame().isRunning())
				for (IRobot robot : this.controller.getGame().getRobots()
						.values())
					this.onPositionChange(robot);
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

		if (x < 0 || y < 0
				|| y >= this.controller.getGame().getStage().getHeight()
				|| x >= this.controller.getGame().getStage().getWidth())
			return;

		IField field = this.controller.getGame().getStage().getField(x, y);

		if (field.getFood() != 0 && this.controller.getGame().isRunning()) {
			this.controller.sendInfoMessage(InfoType.DEBUG, "score", "add "
					+ field.getFood() + " to score of robot " + robot.getId());
			controller.increaseScore(robot.getId(), field.getFood());
			controller.updateFood(x, y, 0);
		}
	}

}
