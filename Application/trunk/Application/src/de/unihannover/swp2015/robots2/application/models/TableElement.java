package de.unihannover.swp2015.robots2.application.models;

/**
 * Table element data holder for pivot table view.
 * 
 * @author Tim Ebbeke
 */
public class TableElement {
	private String player;
	private int points;

	public TableElement(String player, int points) {
		super();
		this.player = player;
		this.points = points;
	}
	
	public String getPlayer() {
		return player;
	}
	public void setPlayer(String player) {
		this.player = player;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}	
}
