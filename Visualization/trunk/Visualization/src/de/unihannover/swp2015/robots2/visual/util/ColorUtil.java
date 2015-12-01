package de.unihannover.swp2015.robots2.visual.util;

import com.badlogic.gdx.graphics.Color;

/**
 * All utility method related to {@link Color}.
 * 
 * @author Rico Schrage
 */
public class ColorUtil {

	/**
	 * Converts an awt-color object to a badlogic-color object.
	 * 
	 * @param color color object, which should gets converted.
	 * @return converted badlogic color object
	 */
	public static Color fromAwtColor(final java.awt.Color color) {
		final Color result = new Color(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/255f);
		return result;
	}
	
}
