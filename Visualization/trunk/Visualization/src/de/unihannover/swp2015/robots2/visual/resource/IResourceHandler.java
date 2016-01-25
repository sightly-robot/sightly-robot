package de.unihannover.swp2015.robots2.visual.resource;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;

import de.unihannover.swp2015.robots2.visual.resource.texture.RenderUnit;

/**
 * Handles all resources.
 * 
 * Supports the usage of multi-texture-packages.
 * 
 * @author Rico Schrage
 */
public interface IResourceHandler extends Disposable {

	/**
	 * Returns texture region from an internal map.
	 * 
	 * @param key
	 *            key of the texture region
	 * @return placeholder if there is no region mapped to the <code>key</code>
	 */
	public TextureRegion getRegion(final ResConst key);

	/**
	 * Returns texture regions from an internal map.
	 * 
	 * The key order defines the order of the resulting texture regions. <br>
	 * E.g. input: "a", "b"; Result: Region mapped to "a", Region mapped to "b".
	 * 
	 * @param key
	 *            keys of the texture regions
	 * @return collection of texture regions (result[i] = null if there is no
	 *         region mapped to <code>key[i]</code>)
	 */
	public TextureRegion[] getRegion(final ResConst... keys);

	/**
	 * Returns the font, which is mapped to the given key, with the size
	 * <code>size</code>.
	 * 
	 * @param key
	 *            {@link ResConst}
	 * @return BitmapFont
	 */
	public BitmapFont getFont(int size, ResConst key);

	/**
	 * Returns the fonts, which are mapped to the given keys with the given
	 * size.
	 * 
	 * Key order defines the order of the resulting fonts. <br>
	 * E.g. input "a", "b"; Result: Font mapped to "a", Font mapped to "b".
	 * 
	 * @param keys
	 *            {@link ResConst}
	 * @return array of fonts
	 */
	public BitmapFont[] getFont(int size, final ResConst... keys);

	/**
	 * Creates render unit which is created with the texture region(s) that are
	 * mapped to <code>key</code>.
	 * 
	 * The resulting {@link RenderUnit} will be put into an internal map to
	 * avoid redundancy. <br>
	 * This way it just allocates memory when there is no appropriate
	 * {@link RenderUnit} for the given key. If you want to use the texture
	 * package system, you have to use this method, so the render unit will
	 * get updated automatically.
	 * 
	 * @param key
	 *            key of the texture region
	 * @return RenderUnit
	 */
	public RenderUnit createRenderUnit(final ResConst key);

	/**
	 * @see #getRenderUnit(String)
	 * @param key
	 *            key of the texture region
	 * @return RenderUnit
	 */
	public RenderUnit[] createRenderUnit(final ResConst... keys);

	/**
	 * Load the texture pack identified by the give name.
	 * 
	 * @param name
	 *            id of the pack
	 */
	public void loadTexturePack(final String name);

	/**
	 * Creates a new bitmap font using an internal generator with the given
	 * parameters.
	 * 
	 * If you create a font with this method the resource handler'll manage it,
	 * which means you must not call dispose yourself.
	 * 
	 * @param size
	 *            size of the font
	 * @param loadChars
	 *            characters you want to display
	 * @param flip
	 *            inverts y axis
	 * @return font
	 */
	public BitmapFont createFont(int size, String loadChars, boolean flip);

	/**
	 * Creates a new bitmap font using an internal generator with the given
	 * parameters.
	 * 
	 * If you create a font with this method the resource handler'll manage it,
	 * which means you must not call dispose yourself.
	 * 
	 * @param size
	 *            size of the font
	 * @param loadChars
	 *            characters you want to display
	 * @param flip
	 *            inverts y axis
	 * @param width
	 *            width of the stroke
	 * @param color
	 *            color of the stroke
	 * @return resulting font
	 */
	public BitmapFont createFont(int size, String loadChars, boolean flip,
			int borderWidth, Color borderColor, Color fontColor);

	/**
	 * Creates a new skin object with resources managed by this handler.
	 * 
	 * @return {@link Skin}
	 */
	public Skin createSkin();

	/**
	 * Creates a new self updating drawable object.
	 * 
	 * Only skin resources will be considered as keys. <br>
	 * The created drawables are managed by the resource-handler.
	 * 
	 * @param key
	 *            key of the region you want to obtain as drawable
	 * @return drawable
	 */
	public Drawable createDrawableFromSkin(ResConst key);

}
