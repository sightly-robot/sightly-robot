package de.sightly_robot.sightly_robot.visual.util;

import com.badlogic.gdx.graphics.Color;

/**
 * Color conversion
 * 
 * All utility methods are related to {@link Color}.
 * 
 * @author Rico Schrage
 */
public class ColorUtil {

	private ColorUtil() {
		// utility class
	}

	/**
	 * Converts an awt-color object to a badlogic-color object.
	 * 
	 * @param color
	 *            awt-color object
	 * @return badlogic-color object
	 */
	public static Color fromAwtColor(final java.awt.Color color) {
		return new Color(color.getRed() / 255f, color.getGreen() / 255f,
				color.getBlue() / 255f, color.getAlpha() / 255f);
	}

}
