package de.unihannover.swp2015.robots2.server.farm;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.model.interfaces.IField;

/**
 * The GrowEvent holds the IField, on which it will executed and its grow time.
 * 
 * These objects are queued by the {@link FarmWorker}.
 * 
 * @author Patrick Kawczynski
 * @version 0.1
 */
public class GrowEvent implements Delayed {

	private final IField field;
	private long nextGrowTime;

	private static final Logger LOGGER = LogManager.getLogger(Farmer.class.getName());
	
	public GrowEvent(IField field) {
		this.LOGGER.trace("Generating Grow Event for Field {}-{}",field.getX(),field.getY());
		this.field = field;
		this.calculateNextGrow();
	}

	/**
	 * Calculates from the current time and the growingrate of the field the
	 * time, where it will grow.
	 */
	public void calculateNextGrow() {
		this.nextGrowTime = System.currentTimeMillis()
				+ this.field.getGrowingRate();
	}

	/**
	 * Shift the next grow time with the given
	 * 
	 * @param shift
	 *            milliseconds
	 */
	public void shiftNextGrow(long shift) {
		this.nextGrowTime += shift;
	}

	/**
	 * Returns the next grow time
	 * 
	 * @return time in milliseconds
	 */
	public long getNextGrowTime() {
		return this.nextGrowTime;
	}

	/**
	 * Returns the fields, where the growevent will executed.
	 * 
	 * @return
	 */
	public IField getField() {
		return this.field;
	}

	@Override
	public int compareTo(Delayed o) {
		if (o instanceof GrowEvent) {
			GrowEvent ge = (GrowEvent) o;
			if (this.nextGrowTime < ge.getNextGrowTime()) {
				return -1;
			} else if (this.nextGrowTime > ge.getNextGrowTime()) {
				return 1;
			} else {
				return 0;
			}
		}
		return 0;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		long diff = this.nextGrowTime - System.currentTimeMillis();
		return unit.convert(diff, TimeUnit.MILLISECONDS);
	}

}
