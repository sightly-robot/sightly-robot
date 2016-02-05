package de.sightly_robot.sightly_robot.model.implementation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.sightly_robot.sightly_robot.model.interfaces.*;
import de.sightly_robot.sightly_robot.model.writeableInterfaces.*;

/**
 * Basic implmentation of the interface IGameWirtable, hence a wrapper for all
 * the parts of the shared data model: A Stage, a map of Robots and some game
 * parameters.
 * 
 * @version 0.2
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public class Game extends AbstractModel implements IGame, IGameWriteable {

	/** The Stage of this Game. Contains a number of Fields. */
	private final IStageWriteable stage;
	/**
	 * A collection of Robots participating in this Game. Implemented as Map for
	 * quick reference using a Robot's id.
	 */
	private final Map<String, IRobotWriteable> robots;
	/**
	 * True if the Game is running. (Robots are allowed to move, food grows and
	 * scores are occasionally increased.)
	 */
	private volatile boolean running;
	/** True if the Model is currently synchronized via MQTT. */
	private volatile boolean synced;
	/** Maximum speed of virtual robots in seconds / field. */
	private volatile float vRobotSpeed;
	/** Maximum rotation speed of virtual robots in seconds / 360° */
	private volatile float vRobotRotationSpeed;

	/**
	 * Constructs a new game.
	 * 
	 * A basic Stage will be constructed and inserted by default.
	 */
	public Game() {
		super();

		this.stage = new Stage();
		this.robots = new ConcurrentHashMap<String, IRobotWriteable>();
	}

	@Override
	public IStageWriteable getStageWriteable() {
		return this.stage;
	}

	@Override
	public void addRobot(IRobotWriteable robot) {
		this.robots.put(robot.getId(), robot);
	}

	@Override
	public void removeRobot(String id) {
		this.robots.remove(id);
	}

	@Override
	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public void setSynced(boolean synced) {
		this.synced = synced;
	}

	@Override
	public Map<String, IRobotWriteable> getRobotsWriteable() {
		return this.robots;
	}

	@Override
	public IStage getStage() {
		return this.stage;
	}

	@Override
	public boolean isRunning() {
		return this.running;
	}

	@Override
	public boolean isSynced() {
		return this.synced;
	}

	@Override
	public Map<String, ? extends IRobot> getRobots() {
		return this.robots;
	}

	@Override
	public void setVRobotSpeed(float vRobotSpeed) {
		this.vRobotSpeed = vRobotSpeed;
	}

	@Override
	public void setVRobotRotationSpeed(float vRobotRotationSpeed) {
		this.vRobotRotationSpeed = vRobotRotationSpeed;
	}

	@Override
	public float getVRobotSpeed() {
		return this.vRobotSpeed;
	}

	@Override
	public float getVRobotRotationSpeed() {
		return this.vRobotRotationSpeed;
	}

}
