package de.unihannover.swp2015.robots2.visual.resource;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Array;

import de.unihannover.swp2015.robots2.visual.resource.ResConst.ResType;
import de.unihannover.swp2015.robots2.visual.resource.texture.RenderUnit;

/**
 * Default implementation of {@link IResourceHandler}.
 * 
 * @author Rico Schrage
 */
public class ResourceHandler implements IResourceHandler {
	
	/**
	 * Will be returned if texMap.get(key) == null
	 */
	private static final ResConst placeholderKey = ResConst.DEFAULT_FIELD;
	
	/**
	 * All chars (as char-array), which will be used by the visualization.
	 */
	private static final String NECESSARY_CHARS = "abcdefghijklmnopqrstuvxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789()";
	
	/**
	 * Map key->textureRegion.
	 * <br>
	 * Hint: <code>frameSetMap.keySet() intersection texMap.keySet() = { }</code>.
	 * 
	 * @see {@link ResConst} for available keys
	 */
	private final Map<ResConst, TextureRegion> texMap;
	
	/**
	 * Map key->Array of TextureRegion's.
	 * <br>
	 * Hint: <code>frameSetMap.keySet() intersection texMap.keySet() = { }</code>.
	 * 
	 * @see {@link ResConst} for available keys
	 */
	private final Map<ResConst, Array<TextureRegion>> frameSetMap;
	
	/**
	 * Map key->font.
	 * 
	 * @see {@link ResConst} for available keys
	 */
	private final Map<ResConst, BitmapFont> fontMap; 
	
	/**
	 * Map key->RendeUnit
	 * <br>
	 * Hint: <code>renderUnitMap.keySet() subset of (frameSetMap.keySet() union texMap.keySet())</code>.
	 */
	private final Map<ResConst, RenderUnit> renderUnitMap;
		
	/**
	 * Base path to the texture themes.
	 */
	private final String pathToThemes;
	
	/**
	 * Main textureAtlas.
	 */
	private TextureAtlas texAtlas;

	/**
	 * Current texture pack.
	 */
	private String texPack = ResConst.DEFAULT_THEME.getName();
	
	/**
	 * Constructs ResourceHandler
	 */
	public ResourceHandler(final String pathToThemes) {
		this.texMap = new HashMap<>();
		this.frameSetMap = new HashMap<>();
		this.renderUnitMap = new HashMap<>();
		this.fontMap = new HashMap<>();
		this.pathToThemes = pathToThemes;
		
		this.createRegions();
		this.createFonts();
	}

	/**
	 * Creates all textures depending on the resource pack.
	 */
	private void createRegions() {
		
		this.texMap.clear();
		this.frameSetMap.clear();
		this.texAtlas = new TextureAtlas(Gdx.files.internal(getPathToTheme()), true);
		
		//TODO support animations
		final ResConst[] resConsts = ResConst.values();
		for (final ResConst res : resConsts) {
			if (res.getType() == ResType.TEX) {
				AtlasRegion region = texAtlas.findRegion(res.getName());
				texMap.put(res, region);
				if (renderUnitMap.containsKey(res)) {
					renderUnitMap.get(res).initAsTexture(region);
				}
			}
		}
	}
	
	private String getPathToTheme() {
		return pathToThemes + File.separator + texPack + File.separator + ResConst.ATLAS_NAME.getName() + ".atlas";
	}
	
	/**
	 * Creates all fonts, that can be used.
	 */
	private void createFonts() {
		final FreeTypeFontGenerator defFontGen = new FreeTypeFontGenerator(Gdx.files.internal("assets/font/Roboto-Regular.ttf"));
		final FreeTypeFontParameter defPara = new FreeTypeFontParameter();
		defPara.characters = NECESSARY_CHARS;
		defPara.size = 15;
		defPara.flip = true;
		this.fontMap.put(ResConst.DEFAULT_FONT, defFontGen.generateFont(defPara));
		
		final FreeTypeFontParameter bigDefFont = new FreeTypeFontParameter();
		bigDefFont.characters = NECESSARY_CHARS;
		bigDefFont.size = 28;
		bigDefFont.flip = true;
		this.fontMap.put(ResConst.DEFAULT_FONT_BIG, defFontGen.generateFont(bigDefFont));

		final FreeTypeFontParameter titleDefFont = new FreeTypeFontParameter();
		titleDefFont.characters = NECESSARY_CHARS;
		titleDefFont.size = 36;
		titleDefFont.flip = true;
		this.fontMap.put(ResConst.DEFAULT_FONT_TITLE, defFontGen.generateFont(titleDefFont));
	}

	@Override
	public RenderUnit createRenderUnit(final ResConst key) {
		if (renderUnitMap.containsKey(key))
			return renderUnitMap.get(key);

		final RenderUnit result = new RenderUnit();
		if (texMap.containsKey(key)) {
			result.initAsTexture(texMap.get(key));
		}
		else if (frameSetMap.containsKey(key)) {
			//TODO modify for texture-packs (optional)
			result.initAsAnimation(frameSetMap.get(key), PlayMode.LOOP, 1); 
		}
		else {
			throw new IllegalArgumentException("No map contains the key: " + key + ".");
		}
		this.renderUnitMap.put(key, result);
		return result;
	}

	@Override
	public RenderUnit[] createRenderUnit(final ResConst... keys) {
		final RenderUnit[] renderUnits = new RenderUnit[keys.length];
		for (int i = 0; i < keys.length; ++i) {
			if (renderUnitMap.containsKey(keys[i])) {
				renderUnits[i] = renderUnitMap.get(keys[i]);
				continue;
			}
			
			final RenderUnit result = new RenderUnit();
			if (texMap.containsKey(keys[i])) {
				result.initAsTexture(texMap.get(keys[i]));
			}
			else if (frameSetMap.containsKey(keys[i])) {
				//TODO modify for texture-packs (optional)
				result.initAsAnimation(frameSetMap.get(keys[i]), PlayMode.LOOP, 1);
			}
			else {
				throw new IllegalArgumentException("No map contains the key: " + keys[i] + ".");
			}

			renderUnits[i] = result;
			this.renderUnitMap.put(keys[i], result);
		}
		return renderUnits;
	}
	
	@Override
	public TextureRegion getRegion(final ResConst key) {
		final TextureRegion region = texMap.get(key);
		if (region == null)
			return texMap.get(placeholderKey);
		return region;
	}
	
	@Override
	public TextureRegion[] getRegion(final ResConst... keys) {
		TextureRegion[] regionArray = new TextureRegion[keys.length];
        for (int i = 0 ; i < keys.length; ++i) {
            regionArray[i] = texMap.get(keys[i]);
        }
        return regionArray;
	}

	@Override
	public void dispose() {
		this.texAtlas.dispose();
	}
	
	public void loadTexturePack(final String name) {
		this.texPack = name;
		this.createRegions();
	}

	@Override
	public BitmapFont getFont(ResConst key) {
		return fontMap.get(key);
	}

	@Override
	public BitmapFont[] getFont(ResConst... keys) {
		BitmapFont[] regionArray = new BitmapFont[keys.length];
        for (int i = 0 ; i < keys.length; ++i) {
            regionArray[i] = fontMap.get(keys[i]);
        }
        return regionArray;
	}

}
