package de.sightly_robot.sightly_robot.visual.core;

import de.sightly_robot.sightly_robot.visual.util.pref.IPreferencesKey;

/**
 * This contains all keys, which will be used for instances of
 * {@link de.sightly_robot.sightly_robot.visual.util.pref.IPreferences}.
 * 
 * Those include keys for using the visualization with a beamer, whether certain
 * object types should be displayed and which texture pack to use.
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
	X_SCALE("abscissaScale", 1f), 
	Y_SCALE("ordinateScale", 1f),
	CYCLE_TEXTURE_PACK("cycleTexturePack", "next"),
	RENDER_WALL("renderWalls", true),
	RENDER_RESOURCES("renderResources", true), 
	RENDER_LOCK("renderLockState", false),
	RENDER_VIRTUAL_BUBBLE("renderVirtualBubble", true), 
	RENDER_HARDWARE_BUBBLE("renderBubble", true), 
	RENDER_ROBOTS("renderRobots", true),
	RENDER_VIRTUAL_ROBOTS("renderVirtualRobots", true);
	
	/** Key for the hashMap */
	private final String key;
	/** default value of the key */
	private final Object defaultValue;

	/**
	 * Constructs a PrefKey with the given key.
	 * 
	 * @param key
	 *            key which will be used to map to the value
	 */
	private PrefKey(final String key, final Object defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	/**
	 * @return the key which will be used to map the value
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return default value of the key
	 */
	public Object getDefault() {
		return defaultValue;
	}

}
