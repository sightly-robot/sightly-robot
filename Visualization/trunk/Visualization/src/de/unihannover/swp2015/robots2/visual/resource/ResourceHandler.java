package de.unihannover.swp2015.robots2.visual.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

import de.unihannover.swp2015.robots2.visual.resource.ResConst.ResType;
import de.unihannover.swp2015.robots2.visual.resource.texture.RenderUnit;

/**
 * Default implementation of {@link IResourceHandler}.
 * 
 * @author Rico Schrage
 */
public class ResourceHandler implements IResourceHandler {

	/** Will be returned if texMap.get(key) == null */
	private static final ResConst PLACEHOLDER = ResConst.DEFAULT_FIELD;

	/** All chars (as char-array), which will be used by the visualization. */
	public static final String NECESSARY_CHARS = "abcdefghijklmnopqrstuvxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789()";

	/** Base path to the texture themes. */
	private final String themePath;

	/** Main textureAtlas. */
	private TextureAtlas texAtlas;

	/** Font-generator */
	private FreeTypeFontGenerator fontGenerator;

	/** Current texture pack. */
	private String texPack = ResConst.DEFAULT_THEME.getName();

	/** Current skin for the UI */
	private Skin uiSkin;

	/**
	 * Map key->textureRegion. <br>
	 * Hint:
	 * <code>frameSetMap.keySet() intersection texMap.keySet() = { }</code>.
	 * 
	 * @see {@link ResConst} for available keys
	 */
	private final Map<ResConst, TextureRegion> texMap;

	/**
	 * Map key->Array of TextureRegion's. <br>
	 * Hint:
	 * <code>frameSetMap.keySet() intersection texMap.keySet() = { }</code>.
	 * 
	 * @see {@link ResConst} for available keys
	 */
	private final Map<ResConst, Array<TextureRegion>> frameSetMap;

	/**
	 * Map key->map(size->font).
	 * 
	 * @see {@link ResConst} for available keys
	 */
	private final Map<ResConst, Map<Integer, BitmapFont>> fontMap;

	/**
	 * Map key->RendeUnit <br>
	 * Hint:
	 * <code>renderUnitMap.keySet() subset of (frameSetMap.keySet() union texMap.keySet())</code>
	 * .
	 */
	private final Map<ResConst, RenderUnit> renderUnitMap;

	/**
	 * ResConst -> TextureRegionDrawable This map will store drawables for
	 * scene2d elements.
	 */
	private final Map<ResConst, TextureRegionDrawable> textureRegionDrawableMap;

	/**
	 * Constructs ResourceHandler
	 */
	public ResourceHandler(final String pathToThemes) {
		this.texMap = new EnumMap<>(ResConst.class);
		this.frameSetMap = new EnumMap<>(ResConst.class);
		this.renderUnitMap = new EnumMap<>(ResConst.class);
		this.fontMap = new EnumMap<>(ResConst.class);
		this.textureRegionDrawableMap = new EnumMap<>(ResConst.class);
		this.themePath = pathToThemes;

		this.createRegions();
		this.createFonts();
	}

	/**
	 * Creates all textures depending on the resource pack.
	 */
	private void createRegions() {

		texMap.clear();
		frameSetMap.clear();
		texAtlas = new TextureAtlas(Gdx.files.internal(getPathToTheme()), true);

		// TODO support animations
		final ResConst[] resConsts = ResConst.values();
		for (final ResConst res : resConsts) {
			if (res.getType() == ResType.TEX) {
				AtlasRegion region = texAtlas.findRegion(res.getName());
				region.getTexture().setFilter(res.getFilter(), res.getFilter());
				texMap.put(res, region);
				if (renderUnitMap.containsKey(res)) {
					renderUnitMap.get(res).initAsTexture(region);
				}
			}
		}

		if (uiSkin == null) {
			return;
		}

		uiSkin.addRegions(texAtlas);
		for (final ResConst res : resConsts) {
			if (res.getType() == ResType.TEX) {
				final TextureRegionDrawable drawable = textureRegionDrawableMap.get(res);
				if (drawable != null) {
					drawable.setRegion(uiSkin.getRegion(res.toString()));
				}
			}
		}
	}

	private String getPathToTheme() {
		return themePath + File.separator + texPack + File.separator + ResConst.ATLAS_NAME.getName() + ".atlas";
	}

	/**
	 * Creates all fonts, that can be used.
	 */
	private void createFonts() {
		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("assets/font/Roboto-Regular.ttf"));
		fontMap.put(ResConst.DEFAULT_FONT, new HashMap<Integer, BitmapFont>());
		fontMap.get(ResConst.DEFAULT_FONT).put(15, createFont(15, NECESSARY_CHARS, true, 1, Color.BLACK, Color.WHITE));
	}

	@Override
	public BitmapFont createFont(int size, String loadChars, boolean flip) {
		return createFont(size, loadChars, flip, 0, Color.WHITE, Color.WHITE);
	}

	@Override
	public BitmapFont createFont(int size, String loadChars, boolean flip, int borderWidth, Color borderColor,
			Color fontColor) {
		if (fontMap.get(ResConst.DEFAULT_FONT).containsKey(size)) {
			return fontMap.get(ResConst.DEFAULT_FONT).get(size);
		}

		final FreeTypeFontParameter para = new FreeTypeFontParameter();
		para.size = size;
		para.characters = loadChars;
		para.flip = flip;
		para.borderColor = borderColor;
		para.borderWidth = borderWidth;
		para.color = fontColor;
		final BitmapFont font = fontGenerator.generateFont(para);
		fontMap.get(ResConst.DEFAULT_FONT).put(size, font);
		return fontGenerator.generateFont(para);
	}

	/**
	 * Load a texture-pack. This updates all renderUnits managed by this
	 * handler.
	 * 
	 * @param name
	 *            name of the theme
	 */
	public void loadTexturePack(final String name) {
		texPack = name;
		createRegions();
	}

	@Override
	public RenderUnit createRenderUnit(final ResConst key) {
		if (renderUnitMap.containsKey(key))
			return renderUnitMap.get(key);

		final RenderUnit result = new RenderUnit();
		if (texMap.containsKey(key)) {
			result.initAsTexture(texMap.get(key));
		} else if (frameSetMap.containsKey(key)) {
			result.initAsAnimation(frameSetMap.get(key), PlayMode.LOOP, 1);
		} else {
			throw new IllegalArgumentException("No map contains the key: " + key + ".");
		}
		renderUnitMap.put(key, result);
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
			} else if (frameSetMap.containsKey(keys[i])) {
				result.initAsAnimation(frameSetMap.get(keys[i]), PlayMode.LOOP, 1);
			} else {
				throw new IllegalArgumentException("No map contains the key: " + keys[i] + ".");
			}

			renderUnits[i] = result;
			renderUnitMap.put(keys[i], result);
		}
		return renderUnits;
	}

	@Override
	public TextureRegion getRegion(final ResConst key) {
		final TextureRegion region = texMap.get(key);
		if (region == null) {
			return texMap.get(PLACEHOLDER);
		}
		return region;
	}

	@Override
	public TextureRegion[] getRegion(final ResConst... keys) {
		final TextureRegion[] regionArray = new TextureRegion[keys.length];
		for (int i = 0; i < keys.length; ++i) {
			regionArray[i] = texMap.get(keys[i]);
		}
		return regionArray;
	}

	@Override
	public void dispose() {
		texAtlas.dispose();
	}

	@Override
	public BitmapFont getFont(int size, ResConst key) {
		return fontMap.get(key).get(size);
	}

	@Override
	public BitmapFont[] getFont(int size, ResConst... keys) {
		final BitmapFont[] regionArray = new BitmapFont[keys.length];
		for (int i = 0; i < keys.length; ++i) {
			regionArray[i] = fontMap.get(keys[i]).get(size);
		}
		return regionArray;
	}

	@Override
	public Skin createSkin() {
		if (uiSkin != null) {
			return uiSkin;
		}

		uiSkin = new Skin();
		uiSkin.add(ResConst.SKIN_DEFAULT_FONT.toString(),
				createFont(50, NECESSARY_CHARS, true, 10, Color.BLACK, Color.WHITE));
		uiSkin.add(ResConst.SKIN_TITLE_FONT.toString(),
				createFont(50, NECESSARY_CHARS, true, 10, Color.BLACK, Color.WHITE));
		uiSkin.add(ResConst.SKIN_RANKING_FONT.toString(),
				createFont(50, NECESSARY_CHARS, true, 10, Color.BLACK, Color.WHITE));
		uiSkin.getFont(ResConst.SKIN_DEFAULT_FONT.toString()).getRegion().getTexture().setFilter(TextureFilter.Linear,
				TextureFilter.Linear);
		uiSkin.getFont(ResConst.SKIN_TITLE_FONT.toString()).getRegion().getTexture().setFilter(TextureFilter.Linear,
				TextureFilter.Linear);
		uiSkin.getFont(ResConst.SKIN_RANKING_FONT.toString()).getRegion().getTexture().setFilter(TextureFilter.Linear,
				TextureFilter.Linear);
		uiSkin.addRegions(texAtlas);
		uiSkin.load(Gdx.files
				.internal(ResConst.ATLAS_PATH.toString() + ResConst.DEFAULT_THEME + File.separator + "skin.json"));
		return uiSkin;
	}

	@Override
	public Drawable createDrawableFromSkin(ResConst key) {
		if (textureRegionDrawableMap.containsKey(key)) {
			return textureRegionDrawableMap.get(key);
		}
		if (uiSkin == null) {
			return null;
		}
		final TextureRegion region = uiSkin.getRegion(key.toString());
		if (region != null) {
			return new TextureRegionDrawable(region);
		}
		return null;
	}
	
	/**
	 * @return all keys of the available themes.
	 */
	public static List<String> themeKeys() {
		FileHandle themeList = Gdx.files.internal(ResConst.THEME_LIST.toString());
		String[] themes = themeList.readString().split("\n");

		List<String> result = new ArrayList<>(themes.length);
		
		for (final String theme : themes) {
			if (!theme.isEmpty())
				result.add(theme);
		}
		return result;
	}
	
	/**
	 * @return index of the default theme (referred to the list created when calling {@link #themeKeys()})
	 */
	public static int getDefaultThemeIndex(List<String> themes) {
		for (int i = 0; i < themes.size(); ++i) {
			if (themes.get(i).trim().equals(ResConst.DEFAULT_THEME.toString()))
				return i;
		}
		throw new IllegalStateException("Default theme is missing!");
	}

}
