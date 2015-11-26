package de.unihannover.swp2015.robots2.visual.util.pref;

import java.util.HashMap;
import java.util.Map;

import de.unihannover.swp2015.robots2.visual.util.pref.observer.PreferencesObservable;

public class Preferences extends PreferencesObservable implements IPreferences {

	final Map<String, String> stringMap;
	final Map<String, Boolean> booleanMap;
	final Map<String, Integer> integerMap;
	final Map<String, Float> floatMap; 
	
	public Preferences() {
		this.stringMap = new HashMap<>();
		this.booleanMap = new HashMap<>();
		this.integerMap = new HashMap<>();
		this.floatMap = new HashMap<>();
	}
	
	@Override
	public boolean getBoolean(String key, boolean def) {
		Boolean result = booleanMap.get(key);
		if (result == null)
			return def;
		return result;
	}

	@Override
	public float getFloat(String key, float def) {
		Float result = floatMap.get(key);
		if (result == null)
			return def;
		return result;
	}

	@Override
	public int getInt(String key, int def) {
		Integer result = integerMap.get(key);
		if (result == null)
			return def;
		return result;
	}

	@Override
	public String getString(String key, String def) {
		String result = stringMap.get(key);
		if (result == null)
			return def;
		return result;
	}

	@Override
	public void putBoolean(String key, boolean value) {
		this.booleanMap.put(key, value);
		this.notifyObserver(key);
	}

	@Override
	public void putFloat(String key, float value) {
		this.floatMap.put(key, value);
		this.notifyObserver(key);
	}

	@Override
	public void putInt(String key, int value) {
		this.integerMap.put(key, value);
		this.notifyObserver(key);
	}

	@Override
	public void putString(String key, String value) {
		this.stringMap.put(key, value);
		this.notifyObserver(key);
	}

}
