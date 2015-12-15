package de.unihannover.swp2015.robots2.visual.core;

import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesKey;

/**
 * Contains all keys, which will be used for instances of {@link de.unihannover.swp2015.robots2.visual.util.pref.IPreferences}.
 * 
 * @author Rico Schrage
 */
public enum PrefConst implements IPreferencesKey {

	DEVICE_WIDTH("device_width"),
	DEVICE_HEIGHT("device_height"),
	FIELD_WIDTH_KEY("field_width"), 
	FIELD_HEIGHT_KEY("field_height"), 
	VIEW_WIDTH("view_width"), 
	VIEW_HEIGHT("view_height");
	
	private final String key;
	
	private PrefConst(final String key) {
		this.key = key;
	}
	
	public String getKey() {
		return this.key;
	}

	@Override
	public Enum<?> getEnum() {
		return this;
	}
	
}
