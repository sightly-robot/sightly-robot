package de.unihannover.swp2015.robots2.model.interfaces;

/**
 * This interface describes an event message, wich can passed between different
 * objects and holds the information about the event.
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public interface IEvent {

	/**
	 * This enumeration contains all possible types of update-events which can
	 * occur.
	 * 
	 * @version 0.1
	 * @author Patrick Kawczynski
	 */
	public enum UpdateType {
		/**
		 * The state of the game changed (running / stopped). Emitted from Game.
		 */
		GAME_STATE,

		/**
		 * The state of the MQTT connection changed and so the model sync state.
		 * Emitted from Game.
		 */
		MODEL_SYNC_STATE,

		/** The global parameters of the game changed. Emitted from Game. */
		GAME_PARAMETER,

		/**
		 * The size of the stage (number for Fields) changed. Emitted from
		 * Stage.
		 */
		STAGE_SIZE,

		/**
		 * The walls of the fields changed. (A new map was started by the user.)
		 * Emitted from Stage.
		 */
		STAGE_WALL,

		/**
		 * The growingrates of the fields changed. (A new map was started by the
		 * user.) Emitted from Stage.
		 */
		STAGE_GROWINGRATE,

		/**
		 * The start positions were updated. Emitted from Stage.
		 */
		STAGE_STARTPOSITIONS,

		/**
		 * The lock/occupy state of a field changed. Emitted from the affected
		 * Field, object reference addresses this Field.
		 */
		FIELD_STATE,

		/**
		 * The food state of a field changed. Emitted from the affected Field,
		 * object reference addresses this Field.
		 */
		FIELD_FOOD,

		/**
		 * The score of a robot changed (could by "myself"). Emitted from the
		 * affected Robot, object reference addresses this Robot.
		 */
		ROBOT_SCORE,

		/**
		 * The position of a robot changed (could by "myself"). This will not be
		 * emitted on every progress change. Emitted from the affected Robot,
		 * object reference addresses this Robot.
		 */
		ROBOT_POSITION,

		/**
		 * The drive progress or position of a robot changed (could by
		 * "myself"). Emitted from the affected Robot, object reference
		 * addresses this Robot.
		 */
		ROBOT_PROGRESS,

		/**
		 * The state of a robot changed (updatedState, errorState). Emitted from
		 * the affected Robot, object reference addresses this Robot.
		 */
		ROBOT_STATE,

		/**
		 * A new robot appeared. Emitted from Game, object reference addresses
		 * the new Robot!
		 */
		ROBOT_ADD,

		/**
		 * A robot was deleted from the game by user interaction. Emitted from
		 * Game, object reference addresses the deleted Robot!
		 */
		ROBOT_DELETE
	}

	/**
	 * Returns the Event-type.
	 * 
	 * @return The event-type
	 */
	public UpdateType getType();

	/**
	 * Returns the related object of the event.
	 * 
	 * @return The Object.
	 */
	public Object getObject();
}
