package de.unihannover.swp2015.robots2.model.interfaces;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IEvent {

	/**
	 * 
	 * @version 0.1
	 * @author Patrick Kawczynski
	 */
	public enum UpdateType {
		GAME_STATE,
		GAME_PARAMETER,
		STAGE_SIZE,
		STAGE_WALL,
		FIELD_STATE,
		FIELD_FOOD,
		ROBOT_SCORE,
		ROBOT_POSITION,
		ROBOT_ADD,
		ROBOT_STATE
	}
	
	/**
	 * 
	 * @return
	 */
	public UpdateType getType();
	
	/**
	 * 
	 * @return
	 */
	public Object getObject();
}
