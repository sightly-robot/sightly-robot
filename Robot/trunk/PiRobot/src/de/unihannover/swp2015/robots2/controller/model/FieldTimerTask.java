package de.unihannover.swp2015.robots2.controller.model;

import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.controller.main.IFieldTimerController;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent.UpdateType;
import de.unihannover.swp2015.robots2.model.interfaces.IField.State;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IFieldWriteable;

public class FieldTimerTask implements Callable<Object> {
	private final IFieldWriteable field;
	private final IFieldTimerController callback;

	private Logger log = LogManager.getLogger(this.getClass().getSimpleName());

	public FieldTimerTask(IFieldWriteable field, IFieldTimerController callback) {
		this.field = field;
		this.callback = callback;
	}

	@Override
	public Object call() {
		switch (this.field.getState()) {
		case LOCKED:
			log.debug("Field " + field.getX() + "-" + field.getY()
					+ " was auto released because robot " + field.getLockedBy()
					+ " didn't occupy.");
			this.field.setState(State.FREE);
			this.field.setLockedBy("");
			this.field.emitEvent(UpdateType.FIELD_STATE);
			break;

		case LOCK_WAIT:
			if (callback != null)
				callback.occupyField(field.getX(), field.getY());

			break;
		case RANDOM_WAIT:
			log.debug(
					"Random wait for field {}-{} finished. Changing to state FREE.",
					field.getX(), field.getY());
			this.field.setLockedBy("");
			this.field.emitEvent(UpdateType.FIELD_STATE);

			break;

		default:
			break;

		}
		return null;
	}

}
