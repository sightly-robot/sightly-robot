package de.unihannover.swp2015.robots2.visual.resource;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Handles all resources. Supports the usage of multi-texture-packages.
 *  
 * @author Rico Schrage
 */
public interface IResourceHandler {

	/**
	 * Returns texture region from an internal map.
	 * 
	 * @param key key of the texture-region
	 * @return null if there is no region mapped to the <code>key</code>
	 */
	public TextureRegion getRegion(final String key);
	
}
