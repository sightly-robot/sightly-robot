package de.unihannover.swp2015.robots2.visual.resource;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Array;

import de.unihannover.swp2015.robots2.visual.resource.texture.RenderUnit;

/**
 * Default implementation of {@link IResourceHandler}.
 * 
 * @author Rico Schrage
 */
public class ResourceHandler implements IResourceHandler {

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
	 * Main textureAtlas.
	 */
	private final TextureAtlas texAtlas;
		
	/**
	 * Will be returned if texMap.get(key) == null
	 */
	private static final ResConst placeholderKey = ResConst.DEFAULT_FIELD;
	
	/**
	 * Constructs ResourceHandler
	 */
	public ResourceHandler(final String pathToAtlas) {
		this.texMap = new HashMap<>();
		this.frameSetMap = new HashMap<>();
		this.renderUnitMap = new HashMap<>();
		this.fontMap = new HashMap<>();
		this.texAtlas = new TextureAtlas(Gdx.files.internal(pathToAtlas), true);

		this.createRegions();
		this.createFonts();
	}

	/**
	 * Helper. Will be replaced soon.
	 */
	private void createRegions() {
		
		final String folder ="default_theme/";
		final String theme = "grass"; //TODO changing of themes, renaming files, make it changeable more easily
		
		final TextureRegion wallN = texAtlas.findRegion("wall_n");
		this.texMap.put(ResConst.DEFAULT_WALL_N, wallN);
		
		final TextureRegion wallW = texAtlas.findRegion("wall_w");
		this.texMap.put(ResConst.DEFAULT_WALL_W, wallW);
		
		final TextureRegion wallE = texAtlas.findRegion("wall_e");
		this.texMap.put(ResConst.DEFAULT_WALL_E, wallE);
		
		final TextureRegion wallS = texAtlas.findRegion("wall_s");
		this.texMap.put(ResConst.DEFAULT_WALL_S, wallS);
		
		final TextureRegion wallVRegion = texAtlas.findRegion("wall_v");
		this.texMap.put(ResConst.DEFAULT_BUBBLE, wallVRegion);
		
		final TextureRegion startpos = texAtlas.findRegion("startpos_white");
		this.texMap.put(ResConst.DEFAULT_STARTPOS, startpos);
		
		final Array<TextureAtlas.AtlasRegion> food = texAtlas.findRegions("food");
		this.texMap.put(ResConst.DEFAULT_RES_1, food.get(0));
		this.texMap.put(ResConst.DEFAULT_RES_2, food.get(1));
		this.texMap.put(ResConst.DEFAULT_RES_3, food.get(2));
		this.texMap.put(ResConst.DEFAULT_RES_4, food.get(3));
		this.texMap.put(ResConst.DEFAULT_RES_5, food.get(4));
		this.texMap.put(ResConst.DEFAULT_RES_6, food.get(5));
		this.texMap.put(ResConst.DEFAULT_RES_7, food.get(6));
		this.texMap.put(ResConst.DEFAULT_RES_8, food.get(7));
		this.texMap.put(ResConst.DEFAULT_RES_9, food.get(8));
		this.texMap.put(ResConst.DEFAULT_RES_10, food.get(9));
		
		final TextureRegion robo = texAtlas.findRegion("robo");
		this.texMap.put(ResConst.DEFAULT_ROBO, robo);
		
		final TextureRegion noWay = texAtlas.findRegion(folder+"0_way_tile_"+theme);
		this.texMap.put(ResConst.DEFAULT_FIELD, noWay);

		final TextureRegion way1 = texAtlas.findRegion(folder+"1_way_tile_"+theme);
		this.texMap.put(ResConst.DEFAULT_FIELD_1_N, way1);
		this.texMap.put(ResConst.DEFAULT_FIELD_1_E, way1);
		this.texMap.put(ResConst.DEFAULT_FIELD_1_S, way1);
		this.texMap.put(ResConst.DEFAULT_FIELD_1_W, way1);
		
		final TextureRegion way2 = texAtlas.findRegion(folder+"2_way_tile_"+theme);
		this.texMap.put(ResConst.DEFAULT_FIELD_2_N, way2);
		this.texMap.put(ResConst.DEFAULT_FIELD_2_E, way2);
		
		final TextureRegion way3 = texAtlas.findRegion(folder+"3_way_tile_"+theme);
		this.texMap.put(ResConst.DEFAULT_FIELD_3_N, way3);
		this.texMap.put(ResConst.DEFAULT_FIELD_3_E, way3);
		this.texMap.put(ResConst.DEFAULT_FIELD_3_S, way3);
		this.texMap.put(ResConst.DEFAULT_FIELD_3_W, way3);
		
		final TextureRegion way4 = texAtlas.findRegion(folder+"4_way_tile_"+theme);
		this.texMap.put(ResConst.DEFAULT_FIELD_4, way4);
		
		final TextureRegion wayC = texAtlas.findRegion(folder+"curve_tile_"+theme);
		this.texMap.put(ResConst.DEFAULT_FIELD_C_NE, wayC);
		this.texMap.put(ResConst.DEFAULT_FIELD_C_ES, wayC);
		this.texMap.put(ResConst.DEFAULT_FIELD_C_SW, wayC);
		this.texMap.put(ResConst.DEFAULT_FIELD_C_WN, wayC);
	}
	
	/**
	 * Creates all fonts, that can be used.
	 */
	private void createFonts() {
		final FreeTypeFontGenerator defFontGen = new FreeTypeFontGenerator(Gdx.files.internal("assets/font/Roboto-Regular.ttf"));
		final FreeTypeFontParameter defPara = new FreeTypeFontParameter();
		defPara.characters = "abcdefghijklmnopqrstuvxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789()";
		defPara.size = 15;
		defPara.flip = true;
		this.fontMap.put(ResConst.DEFAULT_FONT, defFontGen.generateFont(defPara));
		
		final FreeTypeFontParameter bigDefFont = new FreeTypeFontParameter();
		bigDefFont.characters = "abcdefghijklmnopqrstuvxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789()";
		bigDefFont.size = 28;
		bigDefFont.flip = true;
		this.fontMap.put(ResConst.DEFAULT_FONT_BIG, defFontGen.generateFont(bigDefFont));

		final FreeTypeFontParameter titleDefFont = new FreeTypeFontParameter();
		titleDefFont.characters = "abcdefghijklmnopqrstuvxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789()";
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
		//TODO implement (optional)
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
