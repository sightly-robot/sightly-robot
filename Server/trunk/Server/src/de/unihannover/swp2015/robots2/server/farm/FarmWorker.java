package de.unihannover.swp2015.robots2.server.farm;

import java.util.concurrent.DelayQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.controller.interfaces.IServerController;

/**
 * The FarmWorker schedules the gorw events.
 * 
 * @author Patrick Kawczynski
 * @version 0.1
 */
public class FarmWorker extends Thread {

	private final DelayQueue<GrowEvent> growQueue;
	private final IServerController controller;

	private boolean paused;

	private Logger log = LogManager.getLogger(FarmWorker.class.getName());

	public FarmWorker(DelayQueue<GrowEvent> growQueue,
			IServerController controller) {
		this.growQueue = growQueue;
		this.controller = controller;
		this.paused = true;
	}

	@Override
	public void run() {

		try {
			while (true) {
				synchronized (this) {
					while (this.paused) {
						wait();
					}
				}
				this.log.trace("Waiting for next grow event...");
				GrowEvent ge = this.growQueue.take();
				if (ge.getField().getFood() < 10) {
					this.log.trace("Growing Field {}-{}", ge.getField().getX(),
							ge.getField().getY());
					this.controller.increaseFood(ge.getField().getX(), ge
							.getField().getY(), 1);
				}
				ge.calculateNextGrow();
				this.growQueue.put(ge);
			}
		} catch (InterruptedException e) {
			log.debug(e);
		}
	}

	/**
	 * Pause the thread.
	 */
	public void pause() {
		this.paused = true;
	}

	/**
	 * Resumee the thread
	 */
	public synchronized void proceed() {
		this.paused = false;
		notify();
	}

}
