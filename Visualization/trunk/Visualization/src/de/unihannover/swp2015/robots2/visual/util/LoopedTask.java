package de.unihannover.swp2015.robots2.visual.util;

import com.badlogic.gdx.Gdx;

/**
 * Will perform a task with a given interval.
 * 
 * @author Rico Schrage
 *
 */
public class LoopedTask {

	/** Task, which will be executed */
	private final Task task;
	/** Interval between the executions */
	private final float interval;

	/** Time, which passed after last execution */
	private float progress;

	/**
	 * Construct a looped task with the given interval.
	 * 
	 * @param fullInterval
	 *            interval between executions
	 * @param job
	 *            task, which should get excuted
	 */
	public LoopedTask(float fullInterval, Task job) {
		this.interval = fullInterval;
		this.progress = fullInterval;
		this.task = job;
	}

	public void update() {
		progress -= Gdx.graphics.getDeltaTime();
		if (progress <= 0) {
			task.work();
			progress = interval;
		}
	}

}
