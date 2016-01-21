package de.unihannover.swp2015.robots2.visual.game;

import com.badlogic.gdx.graphics.Color;

/**
 * Contains all constants, which will be used to display the game.
 * 
 * @author Rico Schrage
 */
public class GameConst {

	// --------------------------------------------------------------------------------
	// Constants for the robot-bubble
	// --------------------------------------------------------------------------------

	/** Height of the icon realtive to the height of the bubble */
	public static final float BUBBLE_ICON_HEIGHT_REL = 1f / 4f;

	/** Width of the icon relative to the bubble width */
	public static final float BUBBLE_ICON_WIDTH_REL = 1f / 4f;

	/** Y position of the icon relative to the bubble height */
	public static final float BUBBLE_ICON_Y_REL = 1f / 8f;

	/** X position of the icon relative to the bubble width */
	public static final float BUBBLE_ICON_X_REL = 1f / 1.6f;

	/**
	 * Y position of the font for the name relative to the height of the bubble
	 */
	public static final float BUBBLE_FONT_NAME_Y_REL = 1f / 7f;
	/**
	 * Y position of the font for the points relative to the height of the
	 * bubble
	 */
	public static final float BUBBLE_FONT_POINTS_Y_REL = 4f / 11f;

	/** Left padding of the bubble relative to the bubble width */
	public static final float BUBBLE_PADDING_LEFT_REL = 1f / 7f;

	/** Y offset of the bubble relative to the field-size */
	public static final float BUBBLE_Y_OFFSET_REL = 0.1f;

	/** Height of the bubble relative to the field-size */
	public static final float BUBBLE_HEIGHT_REL = 0.5f;

	/** Font size of the name, relative to the field-size */
	public static final float BUBBLE_NAME_FONT_SIZE_REL = 1f / 12f;

	/** Font size of the name, relative to the field-size */
	public static final float BUBBLE_NAME_POINTS_SIZE_REL = 1f / 9f;

	/** Alpha value of the bubble */
	public static final float BUBBLE_ALPHA = 1f;

	// --------------------------------------------------------------------------------
	// Miscellaneous constants
	// --------------------------------------------------------------------------------

	/**
	 * coefficient of the device-height. Solution will be used to resize the
	 * window on an aspect ratio change.
	 */
	public static final float HEIGHT_SCALE = 1f;

	/** scale of the robots */
	public static final float ROBOT_SCALE = 0.7f;

	// --------------------------------------------------------------------------------
	// Constants for the UI
	// --------------------------------------------------------------------------------

	/** relative width of the UI */
	public static final float UI_WIDTH_REL = 0.6f;

	/** relative height of the UI */
	public static final float UI_HEIGHT_REL = 0.6f;

	/** padding of the main frame of the UI relative to the panel width */
	public static final float UI_PADDING_REL = 1 / 10f;

	/** alpha of the whole UI */
	public static final float UI_APLHA = 0.7f;

	/** Aspect ratio of the UI main panel */
	public static final float UI_ASPECT_RATIO = 3f / 4f;

	/** UI ranking scale */
	public static final float UI_RANKING_FONT_SCALE = 1 / 1900f;

	/**
	 * Determines how massive the distance of the ranking depends on the number
	 * of the robots
	 */
	public static final float UI_RANKING_DISTANCE_SIZE_MULTIPLIER = 42f;

	/**
	 * Determines how massive the scale of the font for the ranking depends on
	 * the number of the robots
	 */
	public static final float UI_RANKING_SCALE_SIZE_MULTIPLIER = 6f;

	/**
	 * Relative of the stage width, determines how massive the distance will be
	 * (together with the size multiplier)
	 */
	public static final float UI_DISTANCE_PART = 1 / 100f;

	/** title of the ranking */
	public static final String UI_TITLE = "RANKING";

	/** string, which should be displayed if the game has never been started */
	public static final String UI_NO_DATA = "PLEASE START A GAME";

	/** Color of the main panel of the UI */
	public static final Color UI_COLOR = new Color(0.8f, 0.8f, 0.8f, 0.8f);

	private GameConst() {
		// pure static class
	}

}
