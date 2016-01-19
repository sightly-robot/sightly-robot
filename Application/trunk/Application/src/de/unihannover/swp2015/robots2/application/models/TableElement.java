package de.unihannover.swp2015.robots2.application.models;

/**
 * Table element data holder for pivot table view.
 * 
 * @author Tim Ebbeke
 */
public class TableElement {
	private String id;
	private String player;
	private String name;
	private int points;

	/**
	 * Construct a table element from its components 
	 * @param id The robot id. used internally.
	 * @param player The robot name, can be choosen by robot developers.
	 * @param points The robot points.
	 */
	public TableElement(String id, String player, int points, boolean showId) {
		super();
		this.id = id;
		this.name = player;
		this.points = points;
		shallShowId(showId);
	}
	
	/**
	 * The the player name.
	 * @return Returns the robot name.
	 */
	public String getPlayer() {
		return player;
	}
	
	/**
	 * Toggles in between name and id. 
	 * @param showId Will show the id if set to true.
	 */
	public void shallShowId(boolean showId) {
		if (showId)
			this.player = id;
		else
			this.player = name;
	}
	
	/**
	 * Sets the player name.
	 * @param player The robot name.
	 */
	public void setPlayer(String player) {
		this.player = player;
	}
	
	/**
	 * Gets the robot points / score.
	 * @return The robot score
	 */
	public int getPoints() {
		return points;
	}
	
	/**
	 * Sets the robots points / score
	 * @param points The robot score.
	 */
	public void setPoints(int points) {
		this.points = points;
	}
	
	/**
	 * Gets the robot id (which is unique in a network / game).
	 * @return The robot id.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the robot id (which has to be unique in a network / game).
	 * @param id The robot id.
	 */
	public void setId(String id) {
		this.id = id;
	}	
}
