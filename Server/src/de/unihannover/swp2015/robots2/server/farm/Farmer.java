package de.unihannover.swp2015.robots2.server.farm;

import java.util.Iterator;
import java.util.concurrent.DelayQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.controller.interfaces.IServerController;
import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;

/**
 * Game server component. Holds the growing queue and a farmworker.
 * 
 * @author Patrick Kawczynski
 * @version 0.1
 */
public class Farmer implements IModelObserver {
	private final IServerController controller;
	private final FarmWorker farmworker;
	private final DelayQueue<GrowEvent> growQueue;
	
	private static final Logger LOGGER = LogManager.getLogger(Farmer.class.getName());

	private long stoppedTime;

	public Farmer(IServerController controller) {
		this.controller = controller;
		this.controller.getGame().observe(this);
		this.controller.getGame().getStage().observe(this);

		this.growQueue = new DelayQueue<GrowEvent>();
		this.farmworker = new FarmWorker(this.growQueue, controller);
	}

	@Override
	public void onModelUpdate(IEvent event) {
		switch (event.getType()) {
		case STAGE_GROWINGRATE:
			this.initializeGrowQueue((IStage) event.getObject());
			this.stoppedTime = System.currentTimeMillis();
			break;
		case GAME_STATE:
			if (((IGame) event.getObject()).isRunning()) {
				this.startGrowQueue();
			} else {
				this.stopGrowQueue();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Adds the reveiced growing rates to a time scheduled queue.
	 * 
	 * @param stage
	 *            The stage with the fields and their growing rates.
	 */
	public void initializeGrowQueue(IStage stage) {
		this.growQueue.clear();

		int stageWidth = stage.getWidth();
		int stageHeight = stage.getHeight();

		for (int y = 0; y < stageHeight; y++) {
			for (int x = 0; x < stageWidth; x++) {
				IField f = stage.getField(x, y);
				if (f.getGrowingRate() > 0) {
					this.growQueue.add(new GrowEvent(f));
				}
			}
		}
	}

	/**
	 * Shift all Elements in the growing queue to the correct time and starts
	 * the farmworker thread, or proceed it, if it is waiting.
	 */
	private void startGrowQueue() {
		this.shiftGrowQueue(System.currentTimeMillis() - this.stoppedTime);

		if (!this.farmworker.isAlive()) {
			LOGGER.info("Starting Farm Worker.");
			this.farmworker.start();
		}

		this.farmworker.proceed();
	}

	/**
	 * Saves the current time (for shifting next time) and pause the farmworker.
	 */
	private void stopGrowQueue() {
		this.stoppedTime = System.currentTimeMillis();
		LOGGER.info("Stopping Farm Worker.");
		this.farmworker.pause();
	}

	/**
	 * Shift all growevents in the queue at the passed time.
	 * 
	 * @param shift
	 *            milliseconds
	 */
	private void shiftGrowQueue(long shift) {
		Iterator<GrowEvent> it = this.growQueue.iterator();
		LOGGER.debug("Shifting grow queue by {} ms.",shift);
		while (it.hasNext()) {
			it.next().shiftNextGrow(shift);
		}
	}

}
