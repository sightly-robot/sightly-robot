package de.unihannover.swp2015.robots2.ai.core;

import de.unihannover.swp2015.robots2.ai.exceptions.NoValidOrientationException;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

public class AIEventManager extends Thread implements Runnable {

	private AI ai;
	private IEvent event;

	public AIEventManager(AI ai, IEvent event) {
		super();
		this.ai = ai;
		this.event = event;
		// Automatically starts the thread
		this.start();
	}

	@Override
	public void run() {
		switch (event.getType()) {
		case FIELD_FOOD:
			break;
		case STAGE_WALL:
			System.out.println("WALL");
			this.ai.initialize();
			if (this.ai.getGame().isRunning() && this.ai.getMyself().getPosition() != null) {
				try {
					System.out.println("WALLStart");
					this.ai.nextField(this.ai.getNextOrientation());
				} catch (NoValidOrientationException e) {
				}
			}
			break;
		case STAGE_SIZE:
			break;
		case FIELD_STATE:
			break;
		case ROBOT_SCORE:
			break;
		case ROBOT_POSITION:
			if (this.ai.getGraph() != null) {
				IRobot robot = (IRobot) event.getObject();
				IPosition pos = robot.getPosition();

				if (this.ai.getMyself().getPosition() == null || (this.ai.getMyself().getPosition() != null
						&& (this.ai.getMyself().getPosition().getX() != pos.getX()
								|| this.ai.getMyself().getPosition().getY() != pos.getY()))) {
					System.out.println("POS" + pos.getX() + pos.getY());
					this.ai.getGraph().setRobotPosition(this.ai.getMyself(), pos);

					if (this.ai.getGame().isRunning() && this.ai.getMyself().getPosition() != null) {
						if (robot == this.ai.getController().getMyself()) {		// ABC!!
							try {
								System.out.print("POSStart");
								Orientation o = this.ai.getNextOrientation();
								System.out.println(o.name());
								this.ai.nextField(o);
							} catch (NoValidOrientationException e) {
							}
						}
					}
				} else if (this.ai.getMyself().getOrientation() == null
						|| this.ai.getMyself().getOrientation() != pos.getOrientation()) {
					System.out.println("Orientation" + pos.getOrientation().name());
					this.ai.getMyself().setOrientation(pos.getOrientation());
				}
			}
			break;
		case GAME_STATE: // reicht das?
			System.out.println("Game");
			IGame game = (IGame) event.getObject();
			if (game.isRunning() && this.ai.getGraph() != null && this.ai.getMyself().getPosition() != null) {
				try {
					System.out.println("GAMEStart");
					this.ai.nextField(this.ai.getNextOrientation());
				} catch (NoValidOrientationException e) {
				}
			}
			break;
		case GAME_PARAMETER:
			break;
		case ROBOT_ADD:
			break;
		case ROBOT_STATE:
			break;
		default:
			break;
		}
	}

	/**
	 * Checks if event can be handled properly
	 * 
	 * @return
	 */
	private boolean checkConditions() {
		switch (this.event.getType()) {
		case STAGE_WALL:
			break;
		default:
			return false;
		}
		return false;
	}
}
