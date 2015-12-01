package de.unihannover.swp2015.robots2.core;

import de.unihannover.swp2015.robots2.aiinterface.AbstractAi;
import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.exceptions.InvalidStageException;
import de.unihannover.swp2015.robots2.exceptions.NoValidOrientationException;
import de.unihannover.swp2015.robots2.graph.AIGraph;
import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;

public class AI extends AbstractAi implements IModelObserver {

	private IField nextField;
	private AIGraph graph;
	private IGame game;

	private Robot myself;

	public AI(IRobotController controller) { // controller from data modell
		super(controller);
		this.game = controller.getGame();
		this.game.observe(this);
		this.game.getStage().observe(this);
		this.iRobotController.getMyself().observe(this);
		myself = new Robot(iRobotController.getMyself().getId());
		initialize();
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
			break;
		case STAGE_WALL:
			initialize();
			if (game.isRunning() && myself.getPosition() != null) {
				try {
					fireNextOrientationEvent(getNextOrientation());
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
			if(graph != null)
			{
				IRobot robot = (IRobot) event.getObject();
				IPosition pos = robot.getPosition();

				this.graph.setRobotPosition(myself, pos);
				
				if (this.game.isRunning() && myself.getPosition() != null) {
					if (robot.getPosition().getX() != this.graph.getMyPosition().getX()
							|| robot.getPosition().getY() != this.graph.getMyPosition().getY()) {
						try {
							fireNextOrientationEvent(getNextOrientation());
						} catch (NoValidOrientationException e) {
						}
					}
				}
			}
			break;
		case GAME_STATE: // reicht das?
			IGame game = (IGame) event.getObject();
			if (game.isRunning() && graph != null && myself.getPosition() != null) {
				try {
					fireNextOrientationEvent(getNextOrientation());
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

	@Override
	public void setRelativeSpeed(double foreward, double sideward, double backward) {
		// TODO Auto-generated method stub

	}

	// Backwards not in increment 1
	public IField getRandomField() {
		return null;
	}

	/**
	 * Returns the next Orientation, the robot i supposed to move in.
	 * 
	 * @return Orientation, the robot is supposed to move in next.
	 */
	public Orientation getNextOrientation() throws NoValidOrientationException {
		return this.graph.getRandomOrientation();
	}

	/**
	 * 
	 * @param field
	 * @return
	 */
	public boolean occupy(IField field) {
		boolean isOccupied = true;

		return isOccupied;
	}

	public IField getNextField() {
		return nextField;
	}

	public void setNextField(IField nextField) {
		this.nextField = nextField;
	}
}