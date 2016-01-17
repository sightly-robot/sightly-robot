package de.unihannover.swp2015.robots2.visual.core;

import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesKey;

/**
 * Contains all keys, which will be used for instances of {@link de.unihannover.swp2015.robots2.visual.util.pref.IPreferences}.
 * 
 * @author Rico Schrage
 */
public enum PrefKey implements IPreferencesKey {
	
	DEVICE_WIDTH("device_width", 0f),
	DEVICE_HEIGHT("device_height", 0f),
	FIELD_WIDTH_KEY("field_width", 0f), 
	FIELD_HEIGHT_KEY("field_height", 0f), 
	VIEW_WIDTH("view_width", 0f), 
	VIEW_HEIGHT("view_height", 0f),
	
	X_OFFSET("abscissaOffset", 0f),
	Y_OFFSET("ordinateOffset", 0f),
	X_SCALE("abscissaScale", 0f),
	Y_SCALE("ordinateScale", 0f),
	RENDER_WALL("renderWalls", true),
	RENDER_RESOURCES("renderResources", true),
	RENDER_NAME("renderName", true),
	RENDER_SCORE("renderScore", true),
	RENDER_ROBOTS("renderRobots", true),
	RENDER_VIRTUAL_ROBOTS("renderVirtualRobots", true);
		
	/** Key for the hashMap */
	private final String key;
	/** Default value of the key */
	private final Object defaultValue;
	
	/**
	 * Constructs a PrefKey with the given key.
	 * 
	 * @param key key, which will be used to map to the value.
	 */
	private PrefKey(final String key, final Object defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}
	
	/**
	 * @return Returns the key, which will be used to map the value.
	 */
	public String getKey() {
		return key;
	}
	
	public Object getDefault() {
		return defaultValue;
	}
	
}
