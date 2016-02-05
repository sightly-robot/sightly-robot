package de.sightly_robot.sightly_robot.visual.util;

import com.badlogic.gdx.Gdx;

/**
 * Will perform a task with a given time interval.
 * 
 * @author Rico Schrage
 *
 */
public class LoopedTask {

	/** Task which will be executed */
	private final Task task;
	/** Interval between the executions */
	private final float interval;

	/** Time that has passed after the last execution */
	private float progress;

	/**
	 * Construct a looped task with the given interval.
	 * 
	 * @param fullInterval
	 *            interval between executions
	 * @param job
	 *            task that is supposed to be executed
	 */
	public LoopedTask(float fullInterval, Task job) {
		this.interval = fullInterval;
		this.progress = fullInterval;
		this.task = job;
	}

	/**
	 * Updates internal state of the task.
	 * 
	 * Important: You have to call this every tick.
	 */
	public void update() {
		progress -= Gdx.graphics.getDeltaTime();
		if (progress <= 0) {
			task.work();
			progress = interval;
		}
	}

}
