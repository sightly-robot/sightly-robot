package de.unihannover.swp2015.robots2.ai.core;

import java.awt.Point;

import de.unihannover.swp2015.robots2.ai.exceptions.InvalidPathException;
import de.unihannover.swp2015.robots2.ai.exceptions.InvalidStageException;
import de.unihannover.swp2015.robots2.ai.exceptions.NoValidOrientationException;
import de.unihannover.swp2015.robots2.ai.graph.AIGraph;
import de.unihannover.swp2015.robots2.ai.graph.Node;
import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.robot.interfaces.AbstractAI;

public class AI extends AbstractAI implements IModelObserver {

	private boolean hasStarted = false;
	private IField nextField;
	private Orientation nextOrientation;
	private AIGraph graph; // volatile

	private IGame game;
	private Robot myself;

	public AI(IRobotController controller) { // controller from data modell
		super(controller);
		this.game = controller.getGame();
		this.game.observe(this);
		this.game.getStage().observe(this);
		this.iRobotController.getMyself().observe(this);
		if (this.game.getStage().getWidth() != 0 && this.game.getStage().getHeight() != 0) {
			for (int i = 0; i < this.game.getStage().getWidth(); i++) {
				for (int j = 0; j < this.game.getStage().getHeight(); j++) {
					this.game.getStage().getField(i, j).observe(this);
				}
			}
		}
		myself = new Robot(iRobotController.getMyself().getId());
		initialize();
	}

	private class Tuple<X, Y> {
		public final X x;
		public final Y y;

		public Tuple(X x, Y y) {
			this.x = x;
			this.y = y;
		}
	}

	public void initialize() {
		if (this.game.getStage().getWidth() != 0 && this.game.getStage().getHeight() != 0) {
			try {
				this.graph = new AIGraph(this.game.getStage(), myself);

				this.graph.setRobotPosition(myself, iRobotController.getMyself().getPosition());
			} catch (InvalidStageException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * React on given events
	 */
	@Override
	public void onModelUpdate(IEvent event) {
		switch (event.getType()) {
	
		case FIELD_FOOD:
			if( graph != null ) {
				IField f = (IField)event.getObject();
				graph.setFood(f.getX(), f.getY(), f.getFood());
			}
			break;
		case STAGE_WALL:
			System.out.println("WALL");
			initialize();
			break;
		case STAGE_SIZE:
			if (this.game.getStage().getWidth() != 0 && this.game.getStage().getHeight() != 0) {
				for (int i = 0; i < this.game.getStage().getWidth(); i++) {
					for (int j = 0; j < this.game.getStage().getHeight(); j++) {
						this.game.getStage().getField(i, j).observe(this);
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
	
	private void handleFieldStateEvent(IEvent event) {
		if (graph != null) {
			IField field = (IField) event.getObject();
			Node node = myself.getPosition();

			// TODO nullpointerexception possible?
			if (game.isRunning() && this.nextField == field && this.nextOrientation != null) {
				switch (field.getState()) {
				case OURS:
					fireNextOrientationEvent(this.nextOrientation); // or
					break;
				case FREE:
					int xCoord = this.nextField.getX();
					int yCoord = this.nextField.getY();
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
	
	private void handleRobotPositionEvent(IEvent event) {
		if (graph != null) {
			IRobot robot = (IRobot) event.getObject();
			IPosition pos = robot.getPosition();
			/*
			 * If the position is not set yet, break and do nothing
			 */
			if (pos.getX() == -1 || pos.getY() == -1) {
				return;
			}

			if (robot.isMyself()) {
				/*
				 * If no position was set for myself yet, then update it to
				 * the current node (done in setRobotPosition)
				 */
				if (myself.getPosition() == null) {
					this.graph.setRobotPosition(myself, pos);
					return;
				}
				/*
				 * If myself has a position, update it if it's not the same
				 * as the old one
				 */
				else { //TODO add request for field etc back in
					/*
					 * Check if the new position is different than our old
					 * one, only keep going if this is true
					 */
					if (pos.getX() == myself.getPosition().getX() && pos.getY() == myself.getPosition().getY()) {
						System.out.println("New position is the same as the old one");
						return;
					} else {
						Node position = this.myself.getPosition();
						iRobotController.releaseField(position.getX(), position.getY());

						this.graph.setRobotPosition(myself, pos);
						/*
						 * Only keep going if the game is running
						 */
						boolean requested = false;
						while(!requested) {							
							if (this.game.isRunning()) {
									Tuple<Point, Orientation> tuple = this.getNewNode();
									Point point = tuple.x;
									int x = (int) point.getX();
									int y = (int) point.getY();
									/*
									 * Check if field on next orientation is
									 * free before going
									 */
									switch (this.game.getStage().getField(x, y).getState()) {
									// Lock if free
									case FREE:
										 iRobotController.requestField(x, y);
										 this.nextField = this.game.getStage().getField(x, y);
										 this.nextOrientation = tuple.y;
										 requested = true;
										break;
									// No need to use LOCKED state at the moment
									case LOCKED:
										break; 
									// Calculate new field
									case OCCUPIED: 
										break;
									// Drive on field if we have successfully
									// got it
									// (locked by us)
									// Should be triggered by another event!!
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
				 * Get intern graph-robot object if it already exists, else
				 * create new one and set position
				 */
				if (!this.graph.getRobots().containsKey(robot.getId())) {
					Robot newRobot = new Robot(robot.getId());
					this.graph.getRobots().put(robot.getId(), newRobot);
					this.graph.setRobotPosition(newRobot, pos);
				} else {
					Robot otherRobot = this.graph.getRobots().get(robot.getId());
					int x = otherRobot.getPosition().getX();
					int y = otherRobot.getPosition().getY();
					/*
					 * Check if position was actually updated
					 */
					if (x == pos.getX() && y == pos.getY()) {
						System.out.println("Other robot's position is the same as the old one!");
						return;
					} else {
						this.graph.setRobotPosition(otherRobot, pos);
					}
				}
			}
		}
	}

	private void handleGameStateEvent(IEvent event) {
		// TODO check hasStarted state!!
		IGame game = (IGame) event.getObject();
			if (game.isRunning() && graph != null && myself.getPosition() != null
				&& iRobotController.getMyself().getState().equals(IRobot.RobotState.ENABLED)) {
			Tuple<Point, Orientation> tuple = this.getNewNode();
			Point point = tuple.x;
			int x = (int) point.getX();
			int y = (int) point.getY();
				
			this.iRobotController.requestField(x, y);
			this.nextField = game.getStage().getField(x, y);
			this.nextOrientation = tuple.y;
		}
	}
	
	private void handleRobotStateEvent(IEvent event) {
		IRobot robot = (IRobot) event.getObject();
		if (robot.isMyself()) {
			if (this.hasStarted == false) {
				if (robot.getState().equals(IRobot.RobotState.ENABLED)) {
					this.hasStarted = true;
					if (getController().getGame().isRunning() && graph != null && myself.getPosition() != null) {
						System.out.println("GAMEStart");
						Tuple<Point, Orientation> tuple = this.getNewNode();
						Point point = tuple.x;
						int x = (int) point.getX();
						int y = (int) point.getY();

						this.iRobotController.requestField(x, y);
						this.nextField = this.game.getStage().getField(x, y);
						this.nextOrientation = tuple.y;
					}
				}
			}
		}
	}
	
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
			return new Tuple(new Point(x, y), orientation);
		} catch (NoValidOrientationException e) {
			e.printStackTrace();
			return null; // TODO make sure no invalid value is returned
		}
	}

	@Override
	public void setRelativeSpeed(double foreward, double sideward, double backward) {
		// TODO Auto-generated method stub

	}

	/**
	 * Returns the next Orientation, the robot is supposed to move in.
	 * 
	 * @return Orientation, the robot is supposed to move in next.
	 */
	public Orientation findNextOrientation() throws NoValidOrientationException {
		try {
			return this.graph.getOrientationFromPath(this.graph.getBFSPath(this.graph.findBestNode(5)));
		} catch (InvalidPathException e) {
			e.printStackTrace();
		}
		return null;
	}
		
	public Orientation getNextOrientation() {
		return this.nextOrientation;
	}
}