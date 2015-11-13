package de.unihannover.swp2015.robots2.model.implementation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.unihannover.swp2015.robots2.model.interfaces.*;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.*;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class Game extends AbstractModel implements IGame, IGameWriteable {

	private final IStageWriteable stage;
	private final Map<String, IRobotWriteable> robots;
	private volatile boolean running;
	private volatile float vRobotSpeed;
	private volatile int hesitationTime;

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
	public void addRobot(String id, IRobotWriteable robot) {
		this.robots.put(id, robot);
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
	public Map<String, ? extends IRobot> getRobots() {
		return this.robots;
	}

	@Override
	public void setVRobotSpeed(float vRobotSpeed) {
		this.vRobotSpeed = vRobotSpeed;
	}

	@Override
	public void setHesitationTime(int hesitationTime) {
		this.hesitationTime = hesitationTime;
	}

	@Override
	public float getVRobotSpeed() {
		return this.vRobotSpeed;
	}

	@Override
	public int getHesitationTime() {
		return this.hesitationTime;
	}

}
