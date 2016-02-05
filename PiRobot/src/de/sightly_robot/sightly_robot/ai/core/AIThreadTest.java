package de.sightly_robot.sightly_robot.ai.core;

import de.sightly_robot.sightly_robot.ai.exceptions.InvalidStageException;
import de.sightly_robot.sightly_robot.ai.exceptions.NoValidOrientationException;
import de.sightly_robot.sightly_robot.ai.graph.AIGraph;
import de.sightly_robot.sightly_robot.controller.interfaces.IRobotController;
import de.sightly_robot.sightly_robot.model.externalInterfaces.IModelObserver;
import de.sightly_robot.sightly_robot.model.interfaces.IEvent;
import de.sightly_robot.sightly_robot.model.interfaces.IField;
import de.sightly_robot.sightly_robot.model.interfaces.IGame;
import de.sightly_robot.sightly_robot.model.interfaces.IPosition.Orientation;
import de.sightly_robot.sightly_robot.robot.interfaces.AbstractAI;

public class AIThreadTest extends AbstractAI implements IModelObserver{
	
	private boolean hasStarted = false;
	private IField nextField;
	private Orientation nextOrientation;
	private AIGraph graph; // volatile

	private IGame game;
	
	private AIEventManager eventManager; //TODO synchronized/ volatile ?
	
	private Robot myself;

	public AIThreadTest(IRobotController controller) { // controller from data modell
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
		this.eventManager = new AIEventManager(this);
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
		try {
			this.eventManager.getQueue().put(event);
		}
		catch(InterruptedException e) {
			e.printStackTrace();
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
	 * Returns the next Orientation, the robot is supposed to move in.
	 * 
	 * @return Orientation, the robot is supposed to move in next.
	 */
	public Orientation findNextOrientation() throws NoValidOrientationException {
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
	
	public void fireNextOrientationEventProxy(Orientation orientation) {
		this.fireNextOrientationEvent(orientation);
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
	
	public IRobotController getIRobotController() {
		return this.iRobotController;
	}
	
	public boolean isStarted() {
		return this.hasStarted;
	}
	
	public void setStarted(boolean started) {
		this.hasStarted = started;
	}
	
	public Orientation getNextOrientation() {
		return this.nextOrientation;
	}
}
