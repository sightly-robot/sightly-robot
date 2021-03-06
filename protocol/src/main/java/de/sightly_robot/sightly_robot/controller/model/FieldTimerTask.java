package de.sightly_robot.sightly_robot.controller.model;

import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.sightly_robot.sightly_robot.controller.main.IFieldTimerController;
import de.sightly_robot.sightly_robot.model.interfaces.IEvent.UpdateType;
import de.sightly_robot.sightly_robot.model.interfaces.IField.State;
import de.sightly_robot.sightly_robot.model.writeableInterfaces.IFieldWriteable;

public class FieldTimerTask implements Callable<Object> {
	private final IFieldWriteable field;
	private final IFieldTimerController callback;

	private static final Logger LOGGER = LogManager.getLogger(FieldStateModelController.class.getName());

	public FieldTimerTask(IFieldWriteable field, IFieldTimerController callback) {
		this.field = field;
		this.callback = callback;
	}

	@Override
	public Object call() {
		switch (this.field.getState()) {
		case LOCKED:
			LOGGER.debug("Field " + field.getX() + "-" + field.getY()
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
			LOGGER.debug(
					"Random wait for field {}-{} finished. Changing to state FREE.",
					field.getX(), field.getY());
			this.field.setState(State.FREE);
			this.field.setLockedBy("");
			this.field.emitEvent(UpdateType.FIELD_STATE);

			break;

		default:
			break;

		}
		return null;
	}

}
