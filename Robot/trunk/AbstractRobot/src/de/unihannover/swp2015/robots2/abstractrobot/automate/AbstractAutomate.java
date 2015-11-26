package de.unihannover.swp2015.robots2.abstractrobot.automate;

import java.awt.Point;

import de.unihannover.swp2015.robots2.abstractrobot.Direction;
import de.unihannover.swp2015.robots2.aiinterface.AiEventObserver;
import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;

/**
 * AbstractAutomate is a Runnable Class, that automatically creates 
 * an Thread for controlling the current State of a Hard- or SoftwareRoboter.
 * 
 * @author Lenard Spiecker
 */
public abstract class AbstractAutomate implements AiEventObserver, Runnable {

		// Model:
		private IRobotController robotController;
		private IRobot robot;

		// Thread:
		private Thread automation = new Thread(this);
		private static final long LOOP_WAIT_MS = 30;

		// State:
		private IState state;

		// Positioning:
		private Point nextPosition = new Point(0, 0);

		public AbstractAutomate(IRobotController robotController, IState waitState) {
			this.robotController = robotController;
			robot = robotController.getMyself();

			state = waitState;
		}
		
		public void start()
		{
			automation.start();
		}

		@Override
		public void run() {
			IState tempState = state;
			while (!Thread.interrupted()) {
				tempState = state.execute();

				if (state != tempState) {
					if (tempState.isWait()) {
						// Update Only Position:
						robotController.updatePosition(nextPosition.x, nextPosition.y,
								robot.getPosition().getOrientation());
					}
					state = tempState;
					state.start();
					//System.out.println(state.name());
				}

				synchronized (automation) {
					try {
						automation.wait(LOOP_WAIT_MS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		@Override
		public boolean nextOrientationEvent(Orientation orientation) {
			if (state.isWait()) {
				nextPosition.setLocation(robot.getPosition().getX(), robot.getPosition().getY());
				switch (orientation) {
				case NORTH:
					nextPosition.translate(0, -1);
					break;
				case EAST:
					nextPosition.translate(+1, 0);
					break;
				case SOUTH:
					nextPosition.translate(0, +1);
					break;
				case WEST:
					nextPosition.translate(-1, 0);
					break;
				}

				// Set new State:
				state = state.getStateForDirection(Direction.calcDirection(robot.getPosition().getOrientation(),orientation));
//				System.out.println(state.name());

				// Update Only Orientation:
				robotController.updatePosition(robot.getPosition().getX(), robot.getPosition().getY(), orientation);
				
				synchronized (automation) {
					automation.notify();
				}
				return true;
			}
			return false;
		}

		@Override
		public void nextButOneOrientationEvent(Orientation orientation) {
			// TODO Blinking right or left.
		}
}
