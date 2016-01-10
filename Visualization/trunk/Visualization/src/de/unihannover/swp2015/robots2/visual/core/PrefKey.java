package de.unihannover.swp2015.robots2.visual.core;

import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesKey;

/**
 * Contains all keys, which will be used for instances of {@link de.unihannover.swp2015.robots2.visual.util.pref.IPreferences}.
 * 
 * @author Rico Schrage
 */
public enum PrefKey implements IPreferencesKey<PrefKey> {

	DEVICE_WIDTH("device_width"),
	DEVICE_HEIGHT("device_height"),
	FIELD_WIDTH_KEY("field_width"), 
	FIELD_HEIGHT_KEY("field_height"), 
	VIEW_WIDTH("view_width"), 
	VIEW_HEIGHT("view_height");
	
	/** Key for the hashMap */
	private final String key;
	
	/**
	 * Constructs a PrefKey with the given key.
	 * 
	 * @param key key, which will be used to map to the value.
	 */
	private PrefKey(final String key) {
		this.key = key;
	}
	
	/**
	 * @return Returns the key, which will be used to map the value.
	 */
	public String getKey() {
		return key;
	}
	
}
