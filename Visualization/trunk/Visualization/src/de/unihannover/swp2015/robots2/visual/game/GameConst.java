package de.unihannover.swp2015.robots2.visual.game;

/**
 * Contains all constants, which will be used to display the game.
 * 
 * @author Rico Schrage
 */
public class GameConst {
	
	/** x-scale of the robot-bubble */
	public static final float BUBBLE_X_SCALE =  0.75f;
	/** y-scale of the robot-bubble */
	public static final float BUBBLE_Y_SCALE =  0.2f;
	/** Alpha value of the bubble */
	public static final float BUBBLE_ALPHA = 0.5f;
	
	/** coefficient of the device-height. Solution will be used to resize the window on an aspect ratio change. */
	public static final float HEIGHT_SCALE = 0.75f;
	
	/** scale of the robots */
	public static final float ROBOT_SCALE = 0.7f;
	
	/** relative width of the UI */
	public static final float UI_WIDTH_REL = 0.6f;
	/** relative height of the UI */
	public static final float UI_HEIGHT_REL = 0.6f;
	/** padding of the main frame of the UI */
	public static final float UI_PADDING_ABS = 20f;
	/** gap between list elements */
	public static final float UI_DIFF_ABS = 40f;
	/** gap between panel and the beginning of the ranking */
	public static final float UI_TOP_ABS = 50f;
	
	/** title of the ranking */
	public static final String UI_TITLE = "RANKING";
	/** string, which should be displayed if the game has never been started */
	public static final String UI_NO_DATA = "PLEASE START A GAME";
	
	private GameConst() {
		// pure static class
	}
	
}
