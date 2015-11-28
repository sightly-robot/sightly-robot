package de.unihannover.swp2015.robots2.visual.core;

import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesKey;

public enum PrefConst implements IPreferencesKey {

	FIELD_WIDTH_KEY("field_width"), 
	FIELD_HEIGHT_KEY("field_height"), 
	MAP_ROWS_KEY("map_rows"), 
	MAP_COLS_KEY("map_cols"), 
	WALL_THICK_KEY("wall_thick");
	
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
