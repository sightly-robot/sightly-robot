package de.unihannover.swp2015.robots2.visual.util.pref;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import de.unihannover.swp2015.robots2.visual.util.pref.observer.PreferencesObservable;

/**
 * It's an observable implementation of {@link IPreferences}.
 * 
 * @see {@link IPreferences}
 * @author Rico Schrage
 */
public class FlexPreferences extends PreferencesObservable implements IPreferences {

	/**
	 * Handler for saving data persistent.
	 */
	protected final Preferences persistentDataHandler;

	/**
	 * Map for string values.
	 */
	protected final Map<IPreferencesKey, String> stringMap;

	/**
	 * Map for boolean values.
	 */
	protected final Map<IPreferencesKey, Boolean> booleanMap;

	/**
	 * Map for integer values.
	 */
	protected final Map<IPreferencesKey, Integer> integerMap;

	/**
	 * Map for float values.
	 */
	protected final Map<IPreferencesKey, Float> floatMap;

	/**
	 * Constructs a {@link FlexPreferences}, <code>name</code> will be used as filename to store persistent data.
	 * @see {@link com.badlogic.gdx.Preferences}
	 * @param name
	 */
	public FlexPreferences(final String name) {
		this.stringMap = new HashMap<>();
		this.booleanMap = new HashMap<>();
		this.integerMap = new HashMap<>();
		this.floatMap = new HashMap<>();

		this.persistentDataHandler = Gdx.app.getPreferences(name);
	}
	
	@Override
	public boolean getBoolean(IPreferencesKey key) {
		return getBoolean(key, false);
	}

	@Override
	public boolean getBoolean(IPreferencesKey key, boolean def) {
		final Boolean result = booleanMap.get(key);
		if (result == null) {
			if (persistentDataHandler.contains(key.getKey())) {
				final boolean pResult = persistentDataHandler.getBoolean(key.getKey());
				this.booleanMap.put(key, pResult);
				return pResult;
			}
			return def;
		}
		return result;
	}

	@Override
	public float getFloat(IPreferencesKey key) {
		return getFloat(key, 0f);
	}
	
	@Override
	public float getFloat(IPreferencesKey key, float def) {
		final Float result = floatMap.get(key);
		if (result == null) {
			if (persistentDataHandler.contains(key.getKey())) {
				final Float pResult = persistentDataHandler.getFloat(key.getKey());
				this.floatMap.put(key, pResult);
				return pResult;
			}
			return def;
		}
		return result;
	}

	@Override
	public int getInt(IPreferencesKey key) {
		return getInt(key, 0);
	}
	
	@Override
	public int getInt(IPreferencesKey key, int def) {
		final Integer result = integerMap.get(key);
		if (result == null) {
			if (persistentDataHandler.contains(key.getKey())) {
				final Integer pResult = persistentDataHandler.getInteger(key.getKey());
				this.integerMap.put(key, pResult);
				return pResult;
			}
			return def;
		}
		return result;
	}

	@Override
	public String getString(IPreferencesKey key) {
		return getString(key, "");
	}
	
	@Override
	public String getString(IPreferencesKey key, String def) {
		final String result = stringMap.get(key);
		if (result == null) {
			if (persistentDataHandler.contains(key.getKey())) {
				final String pResult = persistentDataHandler.getString(key.getKey());
				this.stringMap.put(key, pResult);
				return pResult;
			}
			return def;
		}
		return result;
	}

	@Override
	public void putBoolean(IPreferencesKey key, boolean value) {
		this.putBoolean(key, value, false);
	}

	@Override
	public void putBoolean(IPreferencesKey key, boolean value, boolean persistent) {
		if (persistent) {
			persistentDataHandler.putBoolean(key.getKey(), value);
		}
		else if (persistentDataHandler.contains(key.getKey())) {
			throw new IllegalArgumentException("The key already exists for storing data in a persistent way!");
		}
		
		this.booleanMap.put(key, value);
		this.notifyObserver(key);
	}

	@Override
	public void putFloat(IPreferencesKey key, float value) {
		this.putFloat(key, value, false);
	}

	@Override
	public void putFloat(IPreferencesKey key, float value, boolean persistent) {
		if (persistent) {
			persistentDataHandler.putFloat(key.getKey(), value);
		}
		else if (persistentDataHandler.contains(key.getKey())) {
			throw new IllegalArgumentException("The key already exists for storing data in a persistent way!");
		}
		
		this.floatMap.put(key, value);
		this.notifyObserver(key);	
	}

	@Override
	public void putInt(IPreferencesKey key, int value) {
		this.putInt(key, value, false);
	}

	@Override
	public void putInt(IPreferencesKey key, int value, boolean persistent) {
		if (persistent) {
			persistentDataHandler.putInteger(key.getKey(), value);
		}
		else if (persistentDataHandler.contains(key.getKey())) {
			throw new IllegalArgumentException("The key already exists for storing data in a persistent way!");
		}
		
		this.integerMap.put(key, value);
		this.notifyObserver(key);
	}

	@Override
	public void putString(IPreferencesKey key, String value) {
		this.putString(key, value, false);
	}

	@Override
	public void putString(IPreferencesKey key, String value, boolean persistent) {
		if (persistent) {
			persistentDataHandler.putString(key.getKey(), value);
		}
		else if (persistentDataHandler.contains(key.getKey())) {
			throw new IllegalArgumentException("The key already exists for storing data in a persistent way!");
		}
		
		this.stringMap.put(key, value);
		this.notifyObserver(key);
	}

}
