package de.sightly_robot.sightly_robot.ai.core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.sightly_robot.sightly_robot.ai.exceptions.InvalidStageException;
import de.sightly_robot.sightly_robot.ai.exceptions.NoValidOrientationException;
import de.sightly_robot.sightly_robot.ai.graph.AIGraph;
import de.sightly_robot.sightly_robot.ai.graph.Edge;
import de.sightly_robot.sightly_robot.ai.graph.Node;
import de.sightly_robot.sightly_robot.controller.interfaces.IRobotController;
import de.sightly_robot.sightly_robot.model.externalInterfaces.IModelObserver;
import de.sightly_robot.sightly_robot.model.interfaces.IEvent;
import de.sightly_robot.sightly_robot.model.interfaces.IField;
import de.sightly_robot.sightly_robot.model.interfaces.IGame;
import de.sightly_robot.sightly_robot.model.interfaces.IPosition;
import de.sightly_robot.sightly_robot.model.interfaces.IRobot;
import de.sightly_robot.sightly_robot.model.interfaces.IField.State;
import de.sightly_robot.sightly_robot.model.interfaces.IPosition.Orientation;
import de.sightly_robot.sightly_robot.model.interfaces.IRobot.RobotState;
import de.sightly_robot.sightly_robot.robot.interfaces.AbstractAI;

/**
 * A specific instance of the AbstractAI used to handle where a robot should drive.
 * 
 * @author Timon Barlag, Philip Naumann
 *
 */
public class AI extends AbstractAI implements IModelObserver {

	private static Logger logger = LogManager.getLogger(AI.class.getName());

	private boolean hasStarted = false; 
	private IField nextField;
	private Orientation nextOrientation;
	private AIGraph graph;

	private IGame game;
	private Robot myself;

	/**
	 * Constructor for AI objects. Creates an AI object as an instance of AbstractAI
	 * based on the given IRobotController.
	 * 
	 * @param controller The IRobotController the AI should be based on.
	 */
	public AI(IRobotController controller) {
		super(controller);

		logger.debug("Loading map from stage");

		this.game = controller.getGame();
		this.game.observe(this);
		this.game.getStage().observe(this);
		this.iRobotController.getMyself().observe(this);
		if (this.game.getStage().getWidth() != 0 && this.game.getStage().getHeight() != 0) {
			for (int i = 0; i < this.game.getStage().getWidth(); i++) {
				for (int j = 0; j < this.game.getStage().getHeight(); j++) {
					logger.trace("Observing field ({}, {})", i, j);
					this.game.getStage().getField(i, j).observe(this);
				}
			}
		}
		myself = new Robot(iRobotController.getMyself().getId());
		initialize();
	}
	
	/**
	 * Helper class that stores two values. Used to return Node and Orientation in 
	 * getNewNode().
	 *
	 * @param <X> First value of the Tuple.
	 * @param <Y> Second value of the Tuple.
	 */
	private class Tuple<X, Y> {
		public final X x;
		public final Y y;

		public Tuple(X x, Y y) {
			this.x = x;
			this.y = y;
		}
	}

	public void initialize() {
		logger.debug("Initialize AI");

		if (this.game.getStage().getWidth() != 0 && this.game.getStage().getHeight() != 0) {
			try {
				this.graph = new AIGraph(this.game.getStage(), myself);
				logger.debug("Positioning myself");
				IPosition myPos = iRobotController.getMyself().getPosition();
				this.graph.setRobotPosition(myself, myPos);
				if (myPos.getX() != -1 && myPos.getY() != -1) {
					this.graph.calculateNextOrientation(myPos.getX(), myPos.getY());
				}
			} catch (InvalidStageException e) {
				logger.error("AI couldn't be initialized, because the stage was invalid", e);
			}
		}
	}

	/**
	 * React to given events
	 */
	@Override
	public void onModelUpdate(IEvent event) {
		logger.trace("Received event: {}", event.getType());
		switch (event.getType()) {

		case FIELD_FOOD:
			if (graph != null) {
				IField f = (IField) event.getObject();
				graph.setFood(f.getX(), f.getY(), f.getFood());
				logger.trace("Positioned food on ({},{})", f.getX(), f.getY());
			}
			break;
		case STAGE_WALL:
			initialize();
			break;
		case STAGE_SIZE:
			if (this.game.getStage().getWidth() != 0 && this.game.getStage().getHeight() != 0) {
				logger.debug("Received new {} event", event);
				for (int i = 0; i < this.game.getStage().getWidth(); i++) {
					for (int j = 0; j < this.game.getStage().getHeight(); j++) {
						this.game.getStage().getField(i, j).observe(this);
						logger.trace("Observing field ({},{}", i, j);
					}
				}
			}
			break;
		case FIELD_STATE:
			handleFieldStateEvent(event);
			break;
		case ROBOT_SCORE:
			break;
		case ROBOT_POSITION:
			handleRobotPositionEvent(event);
			break;
		case GAME_STATE:
			handleGameStateEvent(event);
			break;
		case GAME_PARAMETER:
			break;
		case ROBOT_ADD:
			break;
		case ROBOT_STATE:
			handleRobotStateEvent(event);
			break;
		default:
			break;
		}

	}
		
	/**
	 * Reacts to Events of type FIELD_STATE.
	 * 
	 * @param event The FIELD_STATE Event that is to be handled.
	 */
	private void handleFieldStateEvent(IEvent event) {
		logger.trace("Calling handleFieldStateEvent");
		if (graph != null) {
			IField field = (IField) event.getObject();
			IPosition myPos = iRobotController.getMyself().getPosition();
			if(!this.game.isRunning() && field.getX() != myPos.getX() && field.getY() != myPos.getY()) {
				logger.debug("Field State: release Field ({},{}) because game is not running.", field.getX(), field.getY());
				this.iRobotController.releaseField(field.getX(), field.getY());
			}
			
			if(field.getState() == State.OURS && (field.getX() != nextField.getX() || field.getY() != nextField.getY())) {
				logger.debug("Releasing field ({},{}), because it should not be ours.", field.getX(), field.getY());
				this.iRobotController.releaseField(field.getX(), field.getY());
			}
			
			if (iRobotController.getMyself().getState() == RobotState.ENABLED && game.isRunning()
					&& this.nextField == field && this.nextOrientation != null) {
				switch (field.getState()) {
				case OURS:
					logger.debug("Sending new orientation to robot: {}", this.nextOrientation);
					if(fireNextOrientationEvent(this.nextOrientation)) {
						this.graph.calculateNextOrientation(this.nextField.getX(), this.nextField.getY());
					} // or
					break;
				case FREE:
					int xCoord = this.nextField.getX();
					int yCoord = this.nextField.getY();
					iRobotController.requestField(xCoord, yCoord);
					logger.debug("Requesting field ({},{})", xCoord, yCoord);
					break;
				case LOCK_WAIT:
					break;
				case LOCKED:

				case OCCUPIED:

				case RANDOM_WAIT:

					Tuple<Point, Orientation> tuple = this.getNewNode();
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
	}
	
	/**
	 * Reacts to Events of type ROBOT_POSITION
	 * 
	 * @param event The ROBOT_POSITION Event that is to be handled.
	 */
	private void handleRobotPositionEvent(IEvent event) {
		logger.trace("Calling handleRobotPositionEvent");
		if (graph != null) {
			IRobot robot = (IRobot) event.getObject();
			IPosition pos = robot.getPosition();
			/*
			 * If the position is not set yet, break and do nothing
			 */
			if (pos.getX() == -1 || pos.getY() == -1) {
				logger.error("Position is invalid!");
				return;
			}

			if (robot.isMyself()) {
				/*
				 * If no position was set for myself yet, then update it to the
				 * current node (done in setRobotPosition)
				 */
				if (myself.getPosition() == null) {
					this.graph.setRobotPosition(myself, pos);
					logger.debug("Set position for the first time for myself");
					this.graph.calculateNextOrientation(pos.getX(), pos.getY());
					return;
				}
				/*
				 * If myself has a position, update it if it's not the same as
				 * the old one
				 */
				else { 
					/*
					 * Check if the new position is different than our old one,
					 * only keep going if this is true
					 */
					if (pos.getX() == myself.getPosition().getX() && pos.getY() == myself.getPosition().getY()) {
						logger.warn("New position is the same as the old one!");
						return;
					} else {
						Node position = this.myself.getPosition();
						iRobotController.releaseField(position.getX(), position.getY());

						this.graph.setRobotPosition(myself, pos);
						/*
						 * Only keep going if the game is running
						 */
						boolean requested = false;
						while (!requested && iRobotController.getMyself().getState() == RobotState.ENABLED) {
							if (this.game.isRunning()) {
								Tuple<Point, Orientation> tuple = this.getNewNode();
								Point point = tuple.x;
								int x = (int) point.getX();
								int y = (int) point.getY();
								/*
								 * Check if field on next orientation is free
								 * before going
								 */
								switch (this.game.getStage().getField(x, y).getState()) {
								// Lock if free
								case FREE:
									logger.debug("Requesting field ({},{})", x, y);
									iRobotController.requestField(x, y);
									this.nextField = this.game.getStage().getField(x, y);
									this.nextOrientation = tuple.y;
									requested = true;
									break;
								case LOCKED:
									break;
								case OCCUPIED:
									break;
								case OURS:
									break;
								case LOCK_WAIT:
									break;
								case RANDOM_WAIT:
									break;
								default:
									break;
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Reacts to Events of type GAME_STATE.
	 * 
	 * @param event The GAME_STATE Event that is to be handled.
	 */
	private void handleGameStateEvent(IEvent event) {
		logger.trace("Calling handleGameStateEvent");
		IGame game = (IGame) event.getObject();
		if(!game.isRunning()) {
			logger.debug("Game paused.");
			this.nextField = null;
		} else {
			logger.debug("Game running.");
		}
		if (game.isRunning() && graph != null && myself.getPosition() != null
				&& iRobotController.getMyself().getState() == RobotState.ENABLED) {
			Tuple<Point, Orientation> tuple = this.getNewNode();
			Point point = tuple.x;
			int x = (int) point.getX();		
			int y = (int) point.getY();

			logger.debug("Requesting field ({},{})", x, y);
			this.iRobotController.requestField(x, y);
			this.nextField = game.getStage().getField(x, y);
			this.nextOrientation = tuple.y;
		}
		if (!game.isRunning() && this.nextField != null) {
			logger.debug("Releasing field ({}--{}), because game is not running anymore.", this.nextField.getX(), this.nextField.getY());
			iRobotController.releaseField(this.nextField.getX(), this.nextField.getY());
		}
	}
	
	/**
	 * Reacts to Events of type ROBOT_STATE.
	 * 
	 * @param event The ROBOT_STATE Event that is to be handled.
	 */
	private void handleRobotStateEvent(IEvent event) {
		logger.trace("Calling handleRobotStateEvent");
		IRobot robot = (IRobot) event.getObject();
		logger.debug("RobotStateEvent: {}", robot.getState());
		if (robot.isMyself()) {
			if (this.hasStarted == false) {
				if (robot.getState() == RobotState.ENABLED) {
					this.hasStarted = true;
					if (getController().getGame().isRunning() && graph != null && myself.getPosition() != null) {
						// System.out.println("GAMEStart");
						Tuple<Point, Orientation> tuple = this.getNewNode();
						Point point = tuple.x;
						int x = (int) point.getX();
						int y = (int) point.getY();

						logger.debug("Requesting field ({},{})", x, y);
						this.iRobotController.requestField(x, y);
						this.nextField = this.game.getStage().getField(x, y);
						this.nextOrientation = tuple.y;
					}
				}
			}
			//logger.trace("Game has not started!");
			logger.debug("state is not enabled");
			if (robot.getState() != RobotState.ENABLED) {
				this.hasStarted = false;
				if (this.nextField != null) {
					iRobotController.releaseField(this.nextField.getX(), this.nextField.getY());
				}
				
				if(robot.getState() == RobotState.SETUPSTATE) {
					iRobotController.setRobotReady();
					//this.hasStarted = true;
				}
			}
		}
	}

	/**
	 * Gets next Node which should be requested by either getting the calculated next Node from 
	 * the graph or, if that Node is occupied, a random valid Node. Additionally retrieves the
	 * Orientation the robot needs to drive in to reach the Node.
	 * 
	 * @return A tuple of the next Node to request and the Orientation the robot needs to drive in
	 * 			to reach it. 
	 */
	private Tuple<Point, Orientation> getNewNode() {
		int x = this.myself.getPosition().getX();
		int y = this.myself.getPosition().getY();
		try {
			Orientation orientation = this.findNextOrientation();
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
			while(!isFieldAvailable(x, y)) {
				List<Edge> avNeighbors = new ArrayList<Edge>();
				for(Edge e : this.myself.getPosition().getNeighbors()) {
					x = e.getTarget().getX();
					y = e.getTarget().getY();
					if(isFieldAvailable(x, y)) {
						avNeighbors.add(e);
					}
				}
				Collections.shuffle(avNeighbors);
				Node tmp = avNeighbors.get(0).getTarget();
				return new Tuple<>(new Point(tmp.getX(), tmp.getY()), avNeighbors.get(0).getDirection());
			}
			return new Tuple<>(new Point(x, y), orientation);
		} catch (NoValidOrientationException e) {
			logger.error("getNewNode: no valid orientation was found!", e);
			return null; 
		}
	}
	/**
	 * Checks, whether field on given location is available by checking, if it is occupied,
	 * locked or in random_wait.
	 * 
	 * @param x The x coordinate of the field for which availability should be checked.
	 * @param y The y coordinate of the field for which availability should be checked.
	 * @return true if the field is not occupied, locked or in random_wait and 
	 * 			false otherwise
	 */
	private boolean isFieldAvailable(int x, int y) {
		return !(this.game.getStage().getField(x, y).getState() == State.OCCUPIED ||
				this.game.getStage().getField(x, y).getState() == State.LOCKED ||
				this.game.getStage().getField(x, y).getState() == State.RANDOM_WAIT);
	}

	@Override
	public void setRelativeSpeed(double foreward, double sideward, double backward) {

	}

	/**
	 * Returns the next Orientation, the robot is supposed to move in by getting the calculated
	 * orientation from the graph.
	 * 
	 * @return Orientation, the robot is supposed to move in next.
	 */
	public Orientation findNextOrientation() throws NoValidOrientationException {
		logger.trace("Calling findNextOrientation");
		return this.graph.getNextOrientation();
	}
}