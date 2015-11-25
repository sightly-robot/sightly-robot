package de.unihannover.swp2015.robots2.controller.model;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.unihannover.swp2015.robots2.controller.main.IFieldTimerController;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent.UpdateType;
import de.unihannover.swp2015.robots2.model.interfaces.IField.State;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IFieldWriteable;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IStageWriteable;

/**
 * Part of the main controller that provides some methods for field state
 * handling.
 * 
 * @author Michael Thies
 */
public class FieldStateModelController {

	private final IStageWriteable stage;
	private final ScheduledThreadPoolExecutor timer;
	private IFieldTimerController fieldTimerCallback = null;

	public FieldStateModelController(IStageWriteable stage) {
		this.stage = stage;

		this.timer = new ScheduledThreadPoolExecutor(20);
		this.timer.setRemoveOnCancelPolicy(true);
	}

	/**
	 * Set a callback handler that is informed when a field state timer expires.
	 * Depending on the state of the field one of the methods of this callback
	 * handler will be called to initiate a new lock-try or occupation of the
	 * field.
	 * 
	 * @param timerCallback
	 *            The callback handler for all new timers
	 */
	public void setFieldTimerCallback(IFieldTimerController fieldTimerCallback) {
		this.fieldTimerCallback = fieldTimerCallback;
	}

	/**
	 * Handle an FIELD_OCCUPY_LOCK message.
	 * 
	 * Updates the state of the field (given by id in MQTT topic) dependent on
	 * its current state.
	 * 
	 * @param key
	 *            MQTT Topic string of the message
	 * @param message
	 *            MQTT payload
	 */
	public void mqttFieldLock(String key, String message) {
		String[] coord = key.split("-");
		int x = Integer.parseInt(coord[0]);
		int y = Integer.parseInt(coord[1]);

		IFieldWriteable f = this.stage.getFieldWriteable(x,
				y);

		switch (f.getState()) {
		case FREE:
		case RANDOM_WAIT: {
			f.cancelStateTimer();
			f.setState(State.LOCKED);
			f.setLockedBy(message);

			Future<Object> newTimer = this.timer.schedule(new FieldTimerTask(f,
					null), 3000, TimeUnit.MILLISECONDS);
			f.setStateTimerFuture(newTimer);

			f.emitEvent(UpdateType.FIELD_STATE);
			break;
		}

		case LOCK_WAIT: {
			f.cancelStateTimer();
			f.setState(State.RANDOM_WAIT);

			// TODO generate random wait
			Future<Object> newTimer = this.timer.schedule(new FieldTimerTask(f,
					this.fieldTimerCallback), 1000, TimeUnit.MILLISECONDS);
			f.setStateTimerFuture(newTimer);

			f.emitEvent(UpdateType.FIELD_STATE);
			break;
		}

		default:
			break;
		}
	}

	/**
	 * Handle an FIELD_OCCUPY_SET message.
	 * 
	 * Updates the state of the field (given by id in MQTT topic) dependent on
	 * its current state.
	 * 
	 * @param key
	 *            MQTT Topic string of the message
	 * @param message
	 *            MQTT payload
	 */
	public void mqttFieldOccupy(String key, String message) {
		String[] coord = key.split("-");
		int x = Integer.parseInt(coord[0]);
		int y = Integer.parseInt(coord[1]);

		IFieldWriteable f = this.stage.getFieldWriteable(x,
				y);

		if (f.getState() != State.OURS) {
			f.cancelStateTimer();
			f.setState(State.OCCUPIED);
			f.setLockedBy(message);
			f.emitEvent(UpdateType.FIELD_STATE);
		}
	}

	/**
	 * Handle an FIELD_OCCUPY_RELEASE message.
	 * 
	 * Updates the state of the field (given by id in MQTT topic) dependent on
	 * its current state.
	 * 
	 * @param key
	 *            MQTT Topic string of the message
	 * @param message
	 *            MQTT payload
	 */
	public void mqttFieldRelease(String key, String message) {
		String[] coord = key.split("-");
		int x = Integer.parseInt(coord[0]);
		int y = Integer.parseInt(coord[1]);

		IFieldWriteable f = this.stage.getFieldWriteable(x,
				y);

		switch (f.getState()) {
		case LOCKED:
		case OCCUPIED:
			f.cancelStateTimer();
			f.setState(State.FREE);
			f.setLockedBy("");
			f.emitEvent(UpdateType.FIELD_STATE);
			break;
		default:
			break;
		}
	}

	/**
	 * Set the state of the field at the given coordinates to LOCK_WAIT and
	 * start the timer for occupation.
	 * 
	 * You must proof that the current field state == FREE and send a lock
	 * message before calling this method!
	 * 
	 * @param x
	 *            x coordinate of the field
	 * @param y
	 *            y coordinate of the field
	 */
	public void setFieldLock(int x, int y) {
		IFieldWriteable f = this.stage.getFieldWriteable(x, y);
		f.setState(State.LOCK_WAIT);

		Future<Object> newTimer = this.timer.schedule(new FieldTimerTask(f,
				this.fieldTimerCallback), 1000, TimeUnit.MILLISECONDS);
		f.setStateTimerFuture(newTimer);

		f.emitEvent(UpdateType.FIELD_STATE);
	}

	/**
	 * Set the state of the field at the given coordinates to OURS.
	 * 
	 * You must send a lock message before calling this method!
	 * 
	 * @param x
	 *            x coordinate of the field
	 * @param y
	 *            y coordinate of the field
	 */
	public void setFieldOccupy(int x, int y) {
		IFieldWriteable f = this.stage.getFieldWriteable(x, y);
		f.setState(State.OURS);

		f.emitEvent(UpdateType.FIELD_STATE);

	}

	/**
	 * Set the state of the field at the given coordinates to FREE after it has
	 * been OURS.
	 * 
	 * You must send a lock message before calling this method!
	 * 
	 * @param x
	 *            x coordinate of the field
	 * @param y
	 *            y coordinate of the field
	 */
	public void setFieldRelease(int x, int y) {
		IFieldWriteable f = this.stage.getFieldWriteable(x, y);

		f.setState(State.FREE);
		f.emitEvent(UpdateType.FIELD_STATE);
	}
}
