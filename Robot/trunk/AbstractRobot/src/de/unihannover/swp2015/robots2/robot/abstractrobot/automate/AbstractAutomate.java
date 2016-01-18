package de.unihannover.swp2015.robots2.robot.abstractrobot.automate;

import java.awt.Point;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.robot.abstractrobot.Direction;
import de.unihannover.swp2015.robots2.robot.interfaces.AiEventObserver;
import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;

/**
 * The AbstractAutomate is a {@code Runnable} that automatically creates a
 * {@code Thread} for controlling the current state of a {@link HardwareRobot}
 * or {@link SoftwareRobot}.
 * 
 * @author Lenard Spiecker
 */
public abstract class AbstractAutomate implements AiEventObserver, Runnable {

	//LOGGER:
	private static Logger LOGGER = LogManager.getLogger(AbstractAutomate.class.getName());
	// model
	protected IRobotController robotController;
	protected IRobot robot;

	// thread
	private Thread automation = new Thread(this);
	private static final long LOOP_WAIT_MS = 30;

	// state
	private IState state;

	// positioning
	private Point nextPosition = new Point(0, 0);

	// progress
	private static final long PROGRESS_UPDATE_DURATION = 100;
	private long nextUpdateTime;
	private int lastProgress = 0;

	protected double[] progressMeasurements = new double[] { 1000, 1000, 1000, 1000 };
	private Direction currentDirection = Direction.FORWARDS;
	private long lastWaitTime;

	/**
	 * Constructs a new AbstractAutomate.
	 * 
	 * @param robotController
	 *            the controller of the robot this automate controls
	 * @param waitState
	 *            the initial state of the automate
	 */
	public AbstractAutomate(IRobotController robotController,final IState waitState,final IState setupState,final IState disableState, final IState connectedState) {
		this.robotController = robotController;
		robot = robotController.getMyself();

		state = connectedState;
		
		robotController.getMyself().observe(new IModelObserver() {
			@Override
			public void onModelUpdate(IEvent event) {
				switch (event.getType()) {
				case ROBOT_STATE:
					switch (AbstractAutomate.this.robotController.getMyself().getState()) {
					case SETUPSTATE:
						setState(setupState);
						break;
					case CONNECTED:
						setState(connectedState);
						break;
					case ENABLED:
						setState(waitState);
						break;
					default:
						setState(disableState);
						break;
					}
					break;
				default:
					break;
				}
				
			}
		});
	}

	/**
	 * Starts the automate.
	 */
	public void start() {
		state.start();
		automation.start();
	}

	/**
	 * Actually runs the automate.
	 */
	@Override
	public void run() {
		IState tempState = state;
		while (!Thread.interrupted()) {
			synchronized (state) {
				tempState = state.execute();

				if (state != tempState) {
					state = tempState;
					state.start();
					if (!state.isDriving()) {
						// measurements
						progressMeasurements[currentDirection.ordinal()] = System.currentTimeMillis() - lastWaitTime;
						// update position only
						updatePostition(nextPosition.x, nextPosition.y);
					}
				}

				if (System.currentTimeMillis() > nextUpdateTime) {
					int currentProgress = calcProgress();
					if (currentProgress != lastProgress) {
						updateProgress(currentProgress);
						lastProgress = currentProgress;
					}
					nextUpdateTime = System.currentTimeMillis() + PROGRESS_UPDATE_DURATION;
				}
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

	private void updatePostition(final int x, final int y) {
		new Thread() {
			public void run() {
				robotController.updatePosition(x, y, robot.getPosition().getOrientation());
			};
		}.start();
	}

	private void updateOrientation(final Orientation orientation) {
		new Thread() {
			public void run() {
				robotController.updatePosition(robot.getPosition().getX(), robot.getPosition().getY(), orientation);
			};
		}.start();
	}

	private void updateProgress(final int progress) {
		new Thread() {
			public void run() {
				robotController.updatePositionProgress(progress);
			};
		}.start();
	}

	private int calcProgress() {
		if (state.isDriving()) {
			return (int) ((Math.min(progressMeasurements[currentDirection.ordinal()],
					(System.currentTimeMillis() - lastWaitTime)) / progressMeasurements[currentDirection.ordinal()])
					* 1000);
		} else {
			return 0;
		}
	}

	@Override
	public boolean nextOrientationEvent(Orientation orientation) {
		synchronized (state) {
			if (!state.isDriving()) {
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
				

				// set new state
				currentDirection = Direction.calcDirection(robot.getPosition().getOrientation(), orientation);
				LOGGER.trace("Positioning: "+robot.getPosition().getX()+" "+robot.getPosition().getY() + " Next: "+nextPosition.x+" "+nextPosition.y+" Orientation: "+orientation.name()+" Direction: "+currentDirection.name());
				state = state.getStateForDirection(currentDirection);
				state.start();

				updateOrientation(orientation);

				// measurements
				lastWaitTime = System.currentTimeMillis();

				synchronized (automation) {
					automation.notify();
				}
				return true;
			}
			LOGGER.warn("NextOrientationEvents while driving are not allowed!");
			return false;
		}
	}
	
	protected boolean setState(IState iState) {
		synchronized (state) {
			if (!state.isDriving()) {
				// set new state
				state = iState;
				state.start();

				// measurements
				lastWaitTime = System.currentTimeMillis();

				synchronized (automation) {
					automation.notify();
				}
				return true;
			}
			return false;
		}
	}

	@Override
	public void nextButOneOrientationEvent(Orientation orientation){
		Direction newDirection = Direction.calcDirection(robot.getPosition().getOrientation(), orientation);
		state.setNextButOneDirection(newDirection);
	}

	public Thread getAutomation() {
		return automation;
	}
}
