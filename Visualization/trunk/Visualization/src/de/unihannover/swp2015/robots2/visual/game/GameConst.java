package de.unihannover.swp2015.robots2.visual.game;

import com.badlogic.gdx.graphics.Color;

/**
 * Contains all constants, which will be used to display the game.
 * 
 * @author Rico Schrage
 */
public class GameConst {

	/** Height of the icon realtive to the height of the bubble*/
	public static final float BUBBLE_ICON_HEIGHT_REL = 1f/4f;
	/** Width of the icon relative to the bubble width*/
	public static final float BUBBLE_ICON_WIDTH_REL = 1f/4f;
	/** Y position of the icon relative to the bubble height*/
	public static final float BUBBLE_ICON_Y_REL = 1f/8f;
	/** X position of the icon relative to the bubble width */
	public static final float BUBBLE_ICON_X_REL = 1f/1.6f;
	/** Y position of the font for the name relative to the height of the bubble */
	public static final float BUBBLE_FONT_NAME_Y_REL = 1f/7f;
	/** Y position of the font for the points relative to the height of the bubble */
	public static final float BUBBLE_FONT_POINTS_Y_REL = 4f/11f;
	/** Left padding of the bubble relative to the bubble width */
	public static final float BUBBLE_PADDING_LEFT_REL = 1f/7f;
	/** Y offset of the bubble relative to the field-size */
	public static final float BUBBLE_Y_OFFSET_REL = 0.1f;
	/** Height of the bubble relative to the field-size */
	public static final float BUBBLE_HEIGHT_REL = 0.5f;
	/** Font size of the name, relative to the field-size */
	public static final float BUBBLE_NAME_FONT_SIZE_REL = 1f/12f;
	/** Font size of the name, relative to the field-size */
	public static final float BUBBLE_NAME_POINTS_SIZE_REL = 1f/9f;
	/** Alpha value of the bubble */
	public static final float BUBBLE_ALPHA = 1f;
	
	/** coefficient of the device-height. Solution will be used to resize the window on an aspect ratio change. */
	public static final float HEIGHT_SCALE = 1f;
	
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
	/** alpha of the whole ui */
	public static final float UI_APLHA = 0.7f;
	
	/** title of the ranking */
	public static final String UI_TITLE = "RANKING";
	/** string, which should be displayed if the game has never been started */
	public static final String UI_NO_DATA = "PLEASE START A GAME";
	
	public static final Color UI_COLOR = new Color(0.8f,0.8f,0.8f,0.8f);
	
	private GameConst() {
		// pure static class
	}
	
}
