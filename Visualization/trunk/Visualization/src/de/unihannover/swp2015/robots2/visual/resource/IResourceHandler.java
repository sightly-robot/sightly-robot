package de.unihannover.swp2015.robots2.visual.resource;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

import de.unihannover.swp2015.robots2.visual.resource.texture.RenderUnit;

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
	public TextureRegion getRegion(final ResConst key);
	
	/**
	 * Returns texture regions from an internal map. Key order defines the order of the resulting texture-regions.
	 * E.g. input: "a", "b"; Result: Region mapped to "a", Region mapped to "b".  
	 * 
	 * @param key keys of the texture-regions
	 * @return result[i] = null if there is no region mapped to <code>key[i]</code>
	 */
	public TextureRegion[] getRegion(final ResConst... keys);
	
	/**
	 * Creates render unit, created with the texture region(s), which are mapped to <code>key</code>. 
	 * The resulting {@link RenderUnit} will be put into an internal map to avoid redundancy.
	 * <br>
	 * So it just allocates memory when there is no a appropriate {@link RenderUnit} for the given key.
	 *  
	 * @param key key of the texture region
	 * @return RenderUnit
	 */
	public RenderUnit createRenderUnit(final ResConst key);

	/**
	 * @see #getRenderUnit(String)
	 * @param key key of the texture region
	 * @return RenderUnit
	 */
	public RenderUnit[] createRenderUnit(final ResConst... keys);
	
	//optional
	public void loadTexturePack(final String name);
	
}
