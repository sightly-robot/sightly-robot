package de.unihannover.swp2015.robots2.visual.util;

import com.badlogic.gdx.graphics.Color;

/**
 * All utility methods related to {@link Color}.
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
	 *            color object, which should gets converted.
	 * @return converted badlogic color object
	 */
	public static Color fromAwtColor(final java.awt.Color color) {
		return new Color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f,
				color.getAlpha() / 255f);
	}

}
