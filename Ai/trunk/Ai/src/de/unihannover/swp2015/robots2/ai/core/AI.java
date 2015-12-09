package de.unihannover.swp2015.robots2.ai.core;

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

	private IField nextField;
	private AIGraph graph; // volatile

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
			System.out.println("WALL");
			initialize();
			if (game.isRunning() && myself.getPosition() != null && !getController().getMyself().isSetupState()) {
				try {
					System.out.println("WALLStart");
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
			if (graph != null) {
				IRobot robot = (IRobot) event.getObject();
				IPosition pos = robot.getPosition();
				/*
				 * If the position is not set yet, break and do nothing
				 */
				if (pos.getX() == -1) {
					break;
				}

				if (robot.isMyself()) {
					this.graph.setRobotPosition(myself, pos);
					/*
					 * If no position was set for myself yet, then update it to
					 * the current node (done in setRobotPosition)
					 */
					if (myself.getPosition() == null) {
						this.graph.setRobotPosition(myself, pos);
						break;
					}
					/*
					 * If myself has a position, update it
					 */
					else {
						this.getGraph().setRobotPosition(myself, pos);
						/*
						 * Only keep going if the game is running
						 */
						if(this.game.isRunning()) {
							try {
								Orientation nextOrientation = getNextOrientation();
								Orientation currentOrientation = myself.getOrientation();
								/*
								 * Calculate next node position
								 */
								// TODO
								switch(nextOrientation) {
								case NORTH:
									
								}
								/*
								 * Check if field on next orientation is occupied before going
								 */
								if(this.game.getStage().getField(x, y))
							} catch (NoValidOrientationException e) {
								e.printStackTrace();
							}
						}
					}
				} else {

					if (myself.getPosition() == null
							|| (myself.getPosition() != null && (myself.getPosition().getX() != pos.getX()
									|| myself.getPosition().getY() != pos.getY()))) {
						System.out.println("POS" + pos.getX() + pos.getY());
						this.graph.setRobotPosition(myself, pos);

						if (this.game.isRunning() && myself.getPosition() != null) {
							if (robot == iRobotController.getMyself()) {
								try {
									System.out.print("POSStart");
									Orientation o = getNextOrientation();
									System.out.println(o.name());
									fireNextOrientationEvent(o);
								} catch (NoValidOrientationException e) {
								}
							}
						}
					} else if (myself.getOrientation() == null || myself.getOrientation() != pos.getOrientation()) {
						System.out.println("Orientation" + pos.getOrientation().name());
						myself.setOrientation(pos.getOrientation());
					}
				}
			}
			break;
		case GAME_STATE: // reicht das?
			System.out.println("Game");
			IGame game = (IGame) event.getObject();
			if (game.isRunning() && graph != null && myself.getPosition() != null  && !getController().getMyself().isSetupState()) {
				try {
					System.out.println("GAMEStart");
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
			if (getController().getGame().isRunning() && graph != null && myself.getPosition() != null  && !getController().getMyself().isSetupState()) {
				try {
					System.out.println("GAMEStart");
					fireNextOrientationEvent(getNextOrientation());
				} catch (NoValidOrientationException e) {
				}
			}
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

	public void nextField(Orientation o) {
		fireNextOrientationEvent(o);
	}

	public IField getNextField() {
		return nextField;
	}

	public void setNextField(IField nextField) {
		this.nextField = nextField;
	}

	public AIGraph getGraph() {
		return graph;
	}

	public void setGraph(AIGraph graph) {
		this.graph = graph;
	}

	public Robot getMyself() {
		return myself;
	}

	public void setMyself(Robot myself) {
		this.myself = myself;
	}

	public IGame getGame() {
		return game;
	}

	public void setGame(IGame game) {
		this.game = game;
	}
}