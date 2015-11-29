package de.unihannover.swp2015.robots2.visual.resource;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import de.unihannover.swp2015.robots2.visual.resource.texture.RenderUnit;

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
	 * Map key->RendeUnit
	 * <br>
	 * Hint: <code>renderUnitMap.keySet() subset of (frameSetMap.keySet() union texMap.keySet())</code>.
	 */
	private final Map<ResConst, RenderUnit> renderUnitMap;
	
	/**
	 * Main textureAtlas
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
		this.texAtlas = new TextureAtlas(pathToAtlas);

		this.createRegions();
	}
	
	private void createRegions() {
		final TextureRegion noWay = texAtlas.findRegion("wall_test");
		this.texMap.put(ResConst.DEFAULT_FIELD, noWay);
		
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
		
		final TextureRegion food = texAtlas.findRegion("food");
		this.texMap.put(ResConst.DEFAULT_RES_0, wallVRegion);
		this.texMap.put(ResConst.DEFAULT_RES_1, food);
		this.texMap.put(ResConst.DEFAULT_RES_2, food);
		this.texMap.put(ResConst.DEFAULT_RES_3, food);
		this.texMap.put(ResConst.DEFAULT_RES_4, food);
		this.texMap.put(ResConst.DEFAULT_RES_5, food);
		this.texMap.put(ResConst.DEFAULT_RES_6, food);
		this.texMap.put(ResConst.DEFAULT_RES_7, food);
		this.texMap.put(ResConst.DEFAULT_RES_8, food);
		this.texMap.put(ResConst.DEFAULT_RES_9, food);
		this.texMap.put(ResConst.DEFAULT_RES_10, food);
		
		final TextureRegion robo = texAtlas.findRegion("robo");
		this.texMap.put(ResConst.DEFAULT_ROBO_EAST, robo);
		this.texMap.put(ResConst.DEFAULT_ROBO_NORTH, robo);
		this.texMap.put(ResConst.DEFAULT_ROBO_WEST, robo);
		this.texMap.put(ResConst.DEFAULT_ROBO_SOUTH, robo);
		
		final TextureRegion way1 = texAtlas.findRegion("1_way_tile_tiled");
		this.texMap.put(ResConst.DEFAULT_FIELD_1_N, way1);
		this.texMap.put(ResConst.DEFAULT_FIELD_1_E, way1);
		this.texMap.put(ResConst.DEFAULT_FIELD_1_S, way1);
		this.texMap.put(ResConst.DEFAULT_FIELD_1_W, way1);
		
		final TextureRegion way2 = texAtlas.findRegion("2_way_WE_tile_tiled");
		this.texMap.put(ResConst.DEFAULT_FIELD_2_N, way2);
		this.texMap.put(ResConst.DEFAULT_FIELD_2_E, way2);
		
		final TextureRegion way3 = texAtlas.findRegion("3_way_tile_tiled");
		this.texMap.put(ResConst.DEFAULT_FIELD_3_N, way3);
		this.texMap.put(ResConst.DEFAULT_FIELD_3_E, way3);
		this.texMap.put(ResConst.DEFAULT_FIELD_3_S, way3);
		this.texMap.put(ResConst.DEFAULT_FIELD_3_W, way3);
		
		final TextureRegion way4 = texAtlas.findRegion("4_way_tile_tiled");
		this.texMap.put(ResConst.DEFAULT_FIELD_4, way4);
		
		final TextureRegion wayC = texAtlas.findRegion("curve_tile_tiled");
		this.texMap.put(ResConst.DEFAULT_FIELD_C_NE, wayC);
		this.texMap.put(ResConst.DEFAULT_FIELD_C_ES, wayC);
		this.texMap.put(ResConst.DEFAULT_FIELD_C_SW, wayC);
		this.texMap.put(ResConst.DEFAULT_FIELD_C_WN, wayC);

		//TODO rotate if needed
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

}
