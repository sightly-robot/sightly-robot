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
		if (texMap.containsKey(key)) {
			final RenderUnit result = new RenderUnit();
			result.initAsTexture(texMap.get(key));
			return result;
		}
		else if (frameSetMap.containsKey(key)) {
			final RenderUnit result = new RenderUnit();
			//TODO modify for texture-packs (optional)
			result.initAsAnimation(frameSetMap.get(key), PlayMode.LOOP, 1); 
			return result;
		}
		return null;
	}

	@Override
	public RenderUnit[] createRenderUnit(final String... keys) {
		final RenderUnit[] renderUnits = new RenderUnit[keys.length];
		for (int i = 0; i < keys.length; ++i) {
			if (texMap.containsKey(keys[i])) {
				final RenderUnit result = new RenderUnit();
				result.initAsTexture(texMap.get(keys[i]));
				renderUnits[i] = result;
			}
			else if (frameSetMap.containsKey(keys[i])) {
				final RenderUnit result = new RenderUnit();
				//TODO modify for texture-packs (optional)
				result.initAsAnimation(frameSetMap.get(keys[i]), PlayMode.LOOP, 1); 
				renderUnits[i] = result;
			}
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
