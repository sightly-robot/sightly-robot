package de.unihannover.swp2015.robots2.visual.util;

import com.badlogic.gdx.graphics.Color;

public class ColorUtil {

	public static Color fromAwtColor(final java.awt.Color color) {
		final Color result = new Color(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/255f);
		return result;
	}
	
}
