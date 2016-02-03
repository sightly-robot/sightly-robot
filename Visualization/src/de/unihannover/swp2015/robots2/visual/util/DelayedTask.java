package de.unihannover.swp2015.robots2.visual.util;

/**
 * A delayed task which will be executed after a given amount of time.
 * 
 * @author Rico Schrage
 */
public class DelayedTask {

	/** Task which is supposed to be executed */
	private final Task task;
	/** Time after the task should be executed */
	private float delay;
	/** Remaining delay */
	private float currentDelay;
	/** True if the task has finished */
	private boolean finished = false;

	/**
	 * Construct a delayed task.
	 * 
	 * @param delay
	 *            delay in seconds
	 * @param task
	 *            task
	 */
	public DelayedTask(float delay, Task task) {
		this.task = task;
		this.delay = delay;
		this.currentDelay = delay;
	}

	/**
	 * Updated state of the task.
	 * 
	 * @param delta
	 *            time passed since last frame
	 */
	public void update(float delta) {
		if (finished) {
			return;
		}

		this.currentDelay -= delta;
		if (currentDelay <= 0) {
			task.work();
			finished = true;
		}
	}

	/**
	 * Check if task has finished.
	 * 
	 * @return true if task has finished
	 */
	public boolean hasFinished() {
		return finished;
	}

	/**
	 * Kills the task.
	 */
	public void kill() {
		finished = true;
		currentDelay = 0;
	}

	/**
	 * Resets the task to it's initial values.
	 */
	public void reset() {
		finished = false;
		currentDelay = delay;
	}

}