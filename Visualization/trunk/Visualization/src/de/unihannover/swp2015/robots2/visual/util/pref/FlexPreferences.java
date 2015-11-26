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
	protected final Map<String, String> stringMap;

	/**
	 * Map for boolean values.
	 */
	protected final Map<String, Boolean> booleanMap;

	/**
	 * Map for integer values.
	 */
	protected final Map<String, Integer> integerMap;

	/**
	 * Map for float values.
	 */
	protected final Map<String, Float> floatMap;

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
	public boolean getBoolean(String key, boolean def) {
		final Boolean result = booleanMap.get(key);
		if (result == null) {
			if (persistentDataHandler.contains(key)) {
				final boolean pResult = persistentDataHandler.getBoolean(key);
				this.booleanMap.put(key, pResult);
				return pResult;
			}
			return def;
		}
		return result;
	}

	@Override
	public float getFloat(String key, float def) {
		final Float result = floatMap.get(key);
		if (result == null) {
			if (persistentDataHandler.contains(key)) {
				final Float pResult = persistentDataHandler.getFloat(key);
				this.floatMap.put(key, pResult);
				return pResult;
			}
			return def;
		}
		return result;
	}

	@Override
	public int getInt(String key, int def) {
		final Integer result = integerMap.get(key);
		if (result == null) {
			if (persistentDataHandler.contains(key)) {
				final Integer pResult = persistentDataHandler.getInteger(key);
				this.integerMap.put(key, pResult);
				return pResult;
			}
			return def;
		}
		return result;
	}

	@Override
	public String getString(String key, String def) {
		final String result = stringMap.get(key);
		if (result == null) {
			if (persistentDataHandler.contains(key)) {
				final String pResult = persistentDataHandler.getString(key);
				this.stringMap.put(key, pResult);
				return pResult;
			}
			return def;
		}
		return result;
	}

	@Override
	public void putBoolean(String key, boolean value) {
		this.putBoolean(key, value, false);
	}

	@Override
	public void putBoolean(String key, boolean value, boolean persistent) {
		if (persistent) {
			persistentDataHandler.putBoolean(key, value);
		}
		else if (persistentDataHandler.contains(key)) {
			throw new IllegalArgumentException("The key already exists for storing data in a persistent way!");
		}
		
		this.booleanMap.put(key, value);
		this.notifyObserver(key);
	}

	@Override
	public void putFloat(String key, float value) {
		this.putFloat(key, value, false);
	}

	@Override
	public void putFloat(String key, float value, boolean persistent) {
		if (persistent) {
			persistentDataHandler.putFloat(key, value);
		}
		else if (persistentDataHandler.contains(key)) {
			throw new IllegalArgumentException("The key already exists for storing data in a persistent way!");
		}
		
		this.floatMap.put(key, value);
		this.notifyObserver(key);	
	}

	@Override
	public void putInt(String key, int value) {
		this.putInt(key, value, false);
	}

	@Override
	public void putInt(String key, int value, boolean persistent) {
		if (persistent) {
			persistentDataHandler.putInteger(key, value);
		}
		else if (persistentDataHandler.contains(key)) {
			throw new IllegalArgumentException("The key already exists for storing data in a persistent way!");
		}
		
		this.integerMap.put(key, value);
		this.notifyObserver(key);
	}

	@Override
	public void putString(String key, String value) {
		this.putString(key, value, false);
	}

	@Override
	public void putString(String key, String value, boolean persistent) {
		if (persistent) {
			persistentDataHandler.putString(key, value);
		}
		else if (persistentDataHandler.contains(key)) {
			throw new IllegalArgumentException("The key already exists for storing data in a persistent way!");
		}
		
		this.stringMap.put(key, value);
		this.notifyObserver(key);
	}

}
