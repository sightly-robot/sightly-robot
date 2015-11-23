package de.unihannover.swp2015.robots2.visual.resource;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ResourceHandler implements IResourceHandler {

	/**
	 * Map key->textureRegion
	 * @see {@link ResourceConstants} for available keys
	 */
	private final Map<String, TextureRegion> texMap;
	
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
		this.texAtlas = new TextureAtlas(pathToAtlas);
		
		this.createRegions();
	}
	
	private void createRegions() {
		final TextureRegion someRegion = texAtlas.findRegion("simplistic_textures_map");
		this.texMap.put(ResourceConstants.DEFAULT_MAP, someRegion);
		//TODO finish this
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
	
	

}
