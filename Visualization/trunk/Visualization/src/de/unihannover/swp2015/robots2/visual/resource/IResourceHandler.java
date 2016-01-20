package de.unihannover.swp2015.robots2.visual.resource;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
	 * Returns the font, which is mapped to the given key, with the size <code>size</code>.
	 * 
	 * @param key {@link ResConst}
	 * @return BitmapFont
	 */
	public BitmapFont getFont(int size, ResConst key);
	
	/**
	 * Returns the fonts, which are mapped to the given key's with the given size. Key order defines the order of the resulting texture-regions.
	 * E.g. input "a", "b"; Result: Font mapped to "a", Font mapped to "b".
	 * 
	 * @param keys {@link ResConst}
	 * @return array of fonts.
	 */
	public BitmapFont[] getFont(int size, final ResConst... keys); 
	
	/**
	 * Creates render unit, created with the texture region(s), which are mapped to <code>key</code>. 
	 * The resulting {@link RenderUnit} will be put into an internal map to avoid redundancy.
	 * <br>
	 * So it just allocates memory when there is no a appropriate {@link RenderUnit} for the given key.
	 * If you wan't to use the texture package system you have to use this method, because the renderUnit will get updated automatically.
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
	
	/**
	 * Load the texture pack identified by the give name.
	 * 
	 * @param name id of the pack
	 */
	public void loadTexturePack(final String name);
	
	/**
	 * Creates a new bitmap font using an internal generator with the give parameters. If you create
	 * a font with this method, the resource handler manages them, so you must not call dispose 
	 * yourself.
	 * 
	 * @param size size of the font
	 * @param loadChars chars you want to display
	 * @param flip inverts y-axes 
	 * @return font
	 */
	public BitmapFont createFont(int size, String loadChars, boolean flip);
	
	/**
	 * Creates a new bitmap font using an internal generator with the give parameters. If you create
	 * a font with this method, the resource handler manages them, so you must not call dispose 
	 * yourself.
	 * 
	 * @param size size of the font
	 * @param loadChars chars you want to display
	 * @param flip inverts y-axes 
	 * @param width of the stroke
	 * @param color of the stroke 
	 * @return font
	 */
	public BitmapFont createFont(int size, String loadChars, boolean flip, int borderWidth, Color borderColor);
	
	/**
	 * Creates a new skin object with resources managed by this handler.
	 * 
	 * @return {@link Skin}
	 */
	public Skin createSkin();
	
}
