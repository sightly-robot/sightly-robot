package de.unihannover.swp2015.robots2.hardwarerobot.automate;

import java.awt.Point;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import de.unihannover.swp2015.robots2.aiinterface.AiEventObserver;
import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller.Pi2GoGPIOController;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;

/**
 * Automate is a Runnable Class, that automatically creates an Thread and runs
 * itself for controlling the current State of the HardwareRoboter.
 * 
 * @author Lenard Spiecker
 *
 */
public class Automate implements AiEventObserver, Runnable {

	// Model:
	private IRobotController robotController;
	private IRobot robot;

	// Thread:
	private Thread automation = new Thread(this);
	private static final long LOOP_WAIT_MS = 30;

	// State:
	private State state = State.WAIT_STATE;

	// Positioning:
	private Point nextPosition = new Point(0, 0);

	// Sensor Listener:
	private GpioPinListenerDigital sensorListener = new GpioPinListenerDigital() {
		@Override
		public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent arg0) {
			synchronized (automation) {
				automation.notify();
			}
		}
	};

	public Automate(IRobotController robotController) {
		this.robotController = robotController;
		robot = robotController.getMyself();

		Pi2GoGPIOController.getInstance().getLineLeft().addListener(sensorListener);
		Pi2GoGPIOController.getInstance().getLineRight().addListener(sensorListener);
		Pi2GoGPIOController.getInstance().getButton().addListener(sensorListener);

		automation.start();
	}

	@Override
	public void run() {
		State tempState = state;
		while (!Thread.interrupted()) {
			tempState = state.getNextState();

			if (state != tempState) {
				if (tempState == State.WAIT_STATE) {
					// Update Only Position:
					robotController.updatePosition(nextPosition.x, nextPosition.y,
							robot.getPosition().getOrientation());
				}
				// state.start();
				state = tempState;
				System.out.println(state.name());
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
		if (state == State.WAIT_STATE) {
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

			// Update Only Orientation:
			robotController.updatePosition(robot.getPosition().getX(), robot.getPosition().getY(), orientation);

			// Set new State:
			state = getStateForOrientation(orientation);
			System.out.println(state.name());

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

	private State getStateForOrientation(Orientation orientation) {
		switch (orientation) {
		case NORTH:
			switch (robot.getPosition().getOrientation()) {
			case EAST:
				return State.TURN_LEFT_1_STATE;
			case SOUTH:
				return State.TURN_180_1_STATE;
			case WEST:
				return State.TURN_RIGHT_1_STATE;
			case NORTH:
				return State.LINE_FOLLOW_STATE;
			}
			break;
		case EAST:
			switch (robot.getPosition().getOrientation()) {
			case NORTH:
				return State.TURN_RIGHT_1_STATE;
			case WEST:
				return State.TURN_180_1_STATE;
			case SOUTH:
				return State.TURN_LEFT_1_STATE;
			case EAST:
				return State.LINE_FOLLOW_STATE;
			}
			break;
		case SOUTH:
			switch (robot.getPosition().getOrientation()) {
			case EAST:
				return State.TURN_RIGHT_1_STATE;
			case NORTH:
				return State.TURN_180_1_STATE;
			case WEST:
				return State.TURN_LEFT_1_STATE;
			case SOUTH:
				return State.LINE_FOLLOW_STATE;
			}
			break;
		case WEST:
			switch (robot.getPosition().getOrientation()) {
			case SOUTH:
				return State.TURN_RIGHT_1_STATE;
			case EAST:
				return State.TURN_180_1_STATE;
			case NORTH:
				return State.TURN_LEFT_1_STATE;
			case WEST:
				return State.LINE_FOLLOW_STATE;
			}
			break;
		}
		return State.LINE_FOLLOW_STATE;
	}

}
