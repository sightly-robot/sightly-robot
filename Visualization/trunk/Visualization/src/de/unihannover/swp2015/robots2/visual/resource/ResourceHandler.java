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
	 * @see {@link ResourceConstants} for available keys
	 */
	private final Map<String, TextureRegion> texMap;
	
	/**
	 * Map key->Array of TextureRegion's.
	 * <br>
	 * Hint: <code>frameSetMap.keySet() intersection texMap.keySet() = { }</code>.
	 * 
	 * @see {@link ResourceConstants} for available keys
	 */
	private final Map<String, Array<TextureRegion>> frameSetMap;
	
	/**
	 * Map key->RendeUnit
	 * <br>
	 * Hint: <code>renderUnitMap.keySet() subset of (frameSetMap.keySet() union texMap.keySet())</code>.
	 */
	private final Map<String, RenderUnit> renderUnitMap;
	
	/**
	 * Main textureAtlas
	 */
	private final TextureAtlas texAtlas;
		
	/**
	 * Will be returned if texMap.get(key) == null
	 */
	private static final String placeholderKey = ResourceConstants.DEFAULT_MAP;
	
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
		final TextureRegion someRegion = texAtlas.findRegion("simplistic_textures_map");
		this.texMap.put(ResourceConstants.DEFAULT_MAP, someRegion);
		
		final TextureRegion wallHRegion = texAtlas.findRegion("wall_h");
		this.texMap.put(ResourceConstants.DEFAULT_WALL_H, wallHRegion);
		
		final TextureRegion wallVRegion = texAtlas.findRegion("wall_v");
		this.texMap.put(ResourceConstants.DEFAULT_WALL_V, wallVRegion);
		
		final TextureRegion circle = texAtlas.findRegion("circle");
		this.texMap.put(ResourceConstants.DEFAULT_ROBO_EAST, circle);
		this.texMap.put(ResourceConstants.DEFAULT_ROBO_NORTH, circle);
		this.texMap.put(ResourceConstants.DEFAULT_ROBO_WEST, circle);
		this.texMap.put(ResourceConstants.DEFAULT_ROBO_SOUTH, circle);
		this.texMap.put(ResourceConstants.DEFAULT_RES_0, circle);
		this.texMap.put(ResourceConstants.DEFAULT_RES_1, circle);
		this.texMap.put(ResourceConstants.DEFAULT_RES_2, circle);
		this.texMap.put(ResourceConstants.DEFAULT_RES_3, circle);
		this.texMap.put(ResourceConstants.DEFAULT_RES_4, circle);
		this.texMap.put(ResourceConstants.DEFAULT_RES_5, circle);
		this.texMap.put(ResourceConstants.DEFAULT_RES_6, circle);
		this.texMap.put(ResourceConstants.DEFAULT_RES_7, circle);
		this.texMap.put(ResourceConstants.DEFAULT_RES_8, circle);
		this.texMap.put(ResourceConstants.DEFAULT_RES_9, circle);
		this.texMap.put(ResourceConstants.DEFAULT_RES_10, circle);
		//TODO finish this
	}

	@Override
	public RenderUnit createRenderUnit(final String key) {
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
	public RenderUnit[] createRenderUnit(final String... keys) {
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
	public TextureRegion getRegion(final String key) {
		final TextureRegion region = texMap.get(key);
		if (region == null)
			return texMap.get(placeholderKey);
		return region;
	}
	
	@Override
	public TextureRegion[] getRegion(final String... keys) {
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
