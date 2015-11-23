package de.unihannover.swp2015.robots2.visual.resource;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

/**
 * Handles all resources. Supports the usage of multi-texture-packages.
 *  
 * @author Rico Schrage
 */
public interface IResourceHandler extends Disposable {

	/**
	 * Returns texture region from an internal map.
	 * 
	 * @param key key of the texture-region
	 * @return placeholder if there is no region mapped to the <code>key</code>
	 */
	public TextureRegion getRegion(final String key);
	
	/**
	 * Returns texture regions from an internal map. Key order defines the order of the resulting texture-regions.
	 * E.g. input: "a", "b"; Result: Region mapped to "a", Region mapped to "b".  
	 * 
	 * @param key keys of the texture-regions
	 * @return result[i] = null if there is no region mapped to <code>key[i]</code>
	 */
	public TextureRegion[] getRegion(final String... key);
	
}
