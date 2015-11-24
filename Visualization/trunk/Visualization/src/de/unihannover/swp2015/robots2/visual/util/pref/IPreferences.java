package de.unihannover.swp2015.robots2.visual.util.pref;

import de.unihannover.swp2015.robots2.visual.util.pref.observer.IPreferencesObservable;

public interface IPreferences extends IPreferencesObservable {

	boolean getBoolean(final String key, final boolean def);
	float getFloat(final String key, final float def);
	int getInt(final String key, final int def);
	String getString(final String key, final String def);
	
	void putBoolean(final String key, final boolean value);
	void putFloat(final String key, final float value);
	void putInt(final String key, final int value);
	void putString(final String key, final String value);
	
}
