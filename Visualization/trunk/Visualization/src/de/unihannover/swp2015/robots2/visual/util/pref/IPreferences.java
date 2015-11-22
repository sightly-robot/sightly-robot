package de.unihannover.swp2015.robots2.visual.util.pref;

import de.unihannover.swp2015.robots2.visual.util.pref.observer.IPreferencesObservable;

public interface IPreferences extends IPreferencesObservable {

	boolean getBoolean(final String key, final boolean def);
	float getFloat(final String key, final float def);
	int getInt(final String key, final int def);
	String getString(final String key, final String def);
	
	boolean putBoolean(final String key, final boolean value);
	float putFloat(final String key, final float value);
	int putInt(final String key, final int value);
	String putString(final String key, final String value);
	
}
