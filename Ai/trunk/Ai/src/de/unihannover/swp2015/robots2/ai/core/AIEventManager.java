package de.unihannover.swp2015.robots2.ai.core;

import java.awt.Point;
import java.util.concurrent.BlockingQueue;

import de.unihannover.swp2015.robots2.ai.exceptions.NoValidOrientationException;
import de.unihannover.swp2015.robots2.ai.graph.AIGraph;
import de.unihannover.swp2015.robots2.ai.graph.Node;
import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;

public class AIEventManager extends Thread implements Runnable {

	private AI ai;
	private IEvent event;
	private BlockingQueue queue;

	private class Tuple<X, Y> {
		public final X x;
		public final Y y;

		public Tuple(X x, Y y) {
			this.x = x;
			this.y = y;
		}
	}

	public AIEventManager(AI ai, IEvent event) {
		super();
		this.ai = ai;
		this.event = event;
		// Automatically starts the thread
		this.start();
	}

	@Override
	public void run() {
		AIGraph graph = this.ai.getGraph();
		Robot myself = this.ai.getMyself();
		IGame game = this.ai.getGame();
		IField nextField = this.ai.getNextField();
		Orientation nextOrientation = this.ai.getNextOrientation();
		IRobotController iRobotController = ai.getIRobotController();

		while (true) { // TODO maybe change that
			
			switch (event.getType()) {
			case FIELD_FOOD:
				break;
			case STAGE_WALL:
				System.out.println("WALL");
				this.ai.initialize();
				// if (game.isRunning() && myself.getPosition() != null &&
				// !getController().getMyself().isSetupState()) {
				// try {
				// System.out.println("WALLStart");
				// fireNextOrientationEvent(getNextOrientation());
				// } catch (NoValidOrientationException e) {
				// }
				// }
				break;
			case STAGE_SIZE:
				IStage stage = this.ai.getGame().getStage();
				if (stage.getWidth() != 0 && stage.getHeight() != 0) {
					for (int i = 0; i < stage.getWidth(); i++) {
						for (int j = 0; j < stage.getHeight(); j++) {
							stage.getField(i, j).observe(this.ai); 	// TODO maybe
																	// let
																	// handler
																	// observe
						}
					}
				}
				break;
			case FIELD_STATE:
				if (graph != null) {
					IField field = (IField) event.getObject();
					Node node = myself.getPosition();

					// TODO nullpointerexception possible?
					if (game.isRunning() && nextField == field && nextOrientation != null) {
						switch (field.getState()) {
						case OURS:
							this.ai.fireNextOrientationEventProxy(nextOrientation); // or
							break;
						case FREE:
							int xCoord = nextField.getX();
							int yCoord = nextField.getY();
							iRobotController.requestField(xCoord, yCoord);
							break;
						case LOCK_WAIT:
							break;
						case LOCKED:
							// break;
						case OCCUPIED:
							// break;
						case RANDOM_WAIT:
							iRobotController.releaseField(nextField.getX(), nextField.getY());

							Tuple<Point, Orientation> tuple = this.getNewNode(myself);
							Point point = tuple.x;
							int x = (int) point.getX();
							int y = (int) point.getY();

							iRobotController.requestField(x, y);
							nextField = game.getStage().getField(x, y);
							nextOrientation = tuple.y;
							break;
						default:
							break;
						}
					}
				}
				break;
			case ROBOT_SCORE:
				break;
			case ROBOT_POSITION:
				if (graph != null) {
					IRobot robot = (IRobot) event.getObject();
					IPosition pos = robot.getPosition();
					/*
					 * If the position is not set yet, break and do nothing
					 */
					if (pos.getX() == -1 || pos.getY() == -1) {
						break;
					}

					if (robot.isMyself()) {
						/*
						 * If no position was set for myself yet, then update it
						 * to the current node (done in setRobotPosition)
						 */
						if (myself.getPosition() == null) {
							graph.setRobotPosition(myself, pos);
							break;
						}
						/*
						 * If myself has a position, update it if it's not the
						 * same as the old one
						 */
						else { // TODO add request for field etc back in
							/*
							 * Check if the new position is different than our
							 * old one, only keep going if this is true
							 */
							if (pos.getX() == myself.getPosition().getX()
									&& pos.getY() == myself.getPosition().getY()) {
								System.out.println("New position is the same as the old one");
								break;
							} else {
								Node position = myself.getPosition();
								iRobotController.releaseField(position.getX(), position.getY());

								graph.setRobotPosition(myself, pos);
								/*
								 * Only keep going if the game is running
								 */
								boolean requested = false;
								while (!requested) {
									if (game.isRunning()) {
										Tuple<Point, Orientation> tuple = this.getNewNode(myself);
										Point point = tuple.x;
										int x = (int) point.getX();
										int y = (int) point.getY();

										/*
										 * Check if field on next orientation is
										 * free before going
										 */
										switch (game.getStage().getField(x, y).getState()) {
										// Lock if free
										case FREE:
											iRobotController.requestField(x, y);
											nextField = game.getStage().getField(x, y);
											nextOrientation = tuple.y;
											requested = true;
											break;
										// No need to use LOCKED state at the
										// moment
										case LOCKED:
											break;
										// Calculate new field
										case OCCUPIED:
											break;
										// Drive on field if we have
										// successfully
										// got it
										// (locked by us)
										// Should be triggered by another
										// event!!
										// TODO
										case OURS:
											// fireNextOrientationEvent(nextOrientation);
											break;
										// Don't use for now
										case LOCK_WAIT:
											break;
										// Don't use for now
										case RANDOM_WAIT:
											break;
										// Should throw error if a field has no
										// event
										default:
											break;
										}

									}
								}
							}
						}
					}
					/*
					 * Case robot is not myself
					 */
					else {
						/*
						 * Get intern graph-robot object if it already exists,
						 * else create new one and set position
						 */
						if (!graph.getRobots().containsKey(robot.getId())) {
							Robot newRobot = new Robot(robot.getId());
							graph.getRobots().put(robot.getId(), newRobot);
							graph.setRobotPosition(newRobot, pos);
						} else {
							Robot otherRobot = graph.getRobots().get(robot.getId());
							int x = otherRobot.getPosition().getX();
							int y = otherRobot.getPosition().getY();
							/*
							 * Check if position was actually updated
							 */
							if (x == pos.getX() && y == pos.getY()) {
								System.out.println("Other robot's position is the same as the old one!");
								break;
							} else {
								graph.setRobotPosition(otherRobot, pos);
							}
						}
					}
				}
				break;
			case GAME_STATE: // reicht das?
				System.out.println("Game");
				// TODO check hasStarted state!!
				IGame currGame = (IGame) event.getObject();
				if (currGame.isRunning() && graph != null && myself.getPosition() != null
						&& iRobotController.getMyself().getState().equals(IRobot.RobotState.ENABLED)) {

					System.out.println("GAMEStart");
					Tuple<Point, Orientation> tuple = this.getNewNode(myself);
					Point point = tuple.x;
					int x = (int) point.getX();
					int y = (int) point.getY();

					iRobotController.requestField(x, y);
					nextField = game.getStage().getField(x, y);
					nextOrientation = tuple.y;

				}
				break;
			case GAME_PARAMETER:
				break;
			case ROBOT_ADD:
				break;
			case ROBOT_STATE:
				IRobot robot = (IRobot) event.getObject();
				if (robot.isMyself()) {
					if (this.ai.isStarted() == false) {
						if (robot.getState().equals(IRobot.RobotState.ENABLED)) {
							this.ai.setStarted(true);
							if (game.isRunning() && graph != null && myself.getPosition() != null) {

								System.out.println("GAMEStart");
								Tuple<Point, Orientation> tuple = this.getNewNode(myself);
								Point point = tuple.x;
								int x = (int) point.getX();
								int y = (int) point.getY();

								iRobotController.requestField(x, y);
								nextField = game.getStage().getField(x, y);
								nextOrientation = tuple.y;

							}
						}
					}
				}
				break;
			default:
				break;
			}
		}
	}

	private Tuple<Point, Orientation> getNewNode(Robot myself) {
		int x = myself.getPosition().getX();
		int y = myself.getPosition().getY();
		try {
			Orientation orientation = this.ai.findNextOrientation();
			/*
			 * Calculate next node position
			 */
			switch (orientation) {
			case NORTH:
				y -= 1;
				break;
			case EAST:
				x += 1;
				break;
			case SOUTH:
				y += 1;
				break;
			case WEST:
				x -= 1;
				break;
			default:
				break;
			}
			return new Tuple(new Point(x, y), orientation);
		} catch (NoValidOrientationException e) {
			e.printStackTrace();
			return null; // TODO make sure no invalid value is returned
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
