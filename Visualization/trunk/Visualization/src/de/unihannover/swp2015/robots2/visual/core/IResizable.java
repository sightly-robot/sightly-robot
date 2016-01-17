package de.unihannover.swp2015.robots2.visual.core;

import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Makes the implementing class resizable. 
 * 
 * @author Rico Schrage
 */
public interface IResizable {
	
	/**
	 * Will be called when the application has been resized.
	 * 
	 * @param view viewport of the application
	 */
	void onResize(Viewport view);
}
