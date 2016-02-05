package de.sightly_robot.sightly_robot.visual.util.pref;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * This is an observable implementation of {@link IPreferences}.
 * 
 * @param <T>
 *            type of the key, which will be used for the preference object
 * @see {@link IPreferences}
 * @author Rico Schrage
 */
public class FlexPreferences<T extends IPreferencesKey> extends
		PreferencesObservable<T> implements IPreferences<T> {

	/**
	 * Handler for saving data persistent
	 */
	protected final Preferences persistentDataHandler;

	/**
	 * Map for string values
	 */
	protected final Map<T, String> stringMap;

	/**
	 * Map for boolean values
	 */
	protected final Map<T, Boolean> booleanMap;

	/**
	 * Map for integer values
	 */
	protected final Map<T, Integer> integerMap;

	/**
	 * Map for float values
	 */
	protected final Map<T, Float> floatMap;

	/**
	 * Constructs a {@link FlexPreferences}. <br>
	 * <code>name</code> will be used as filename to store persistent data.
	 * 
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

	/**
	 * Exception for the case the key has already been used.
	 * 
	 * @return exception message
	 */
	private static IllegalArgumentException createPersistentException() {
		return new IllegalArgumentException(
				"The key already exists for storing data in a persistent way!");
	}

	@Override
	public boolean getBoolean(T key) {
		return getBoolean(key, (boolean) key.getDefault());
	}

	@Override
	public boolean getBoolean(T key, boolean def) {
		final Boolean result = booleanMap.get(key);
		if (result == null) {
			if (persistentDataHandler.contains(key.getKey())) {
				final boolean pResult = persistentDataHandler.getBoolean(key
						.getKey());
				booleanMap.put(key, pResult);
				return pResult;
			}
			return def;
		}
		return result;
	}

	@Override
	public float getFloat(T key) {
		return getFloat(key, (float) key.getDefault());
	}

	@Override
	public float getFloat(T key, float def) {
		final Float result = floatMap.get(key);
		if (result == null) {
			if (persistentDataHandler.contains(key.getKey())) {
				final Float pResult = persistentDataHandler.getFloat(key
						.getKey());
				floatMap.put(key, pResult);
				return pResult;
			}
			return def;
		}
		return result;
	}

	@Override
	public int getInt(T key) {
		return getInt(key, (int) key.getDefault());
	}

	@Override
	public int getInt(T key, int def) {
		final Integer result = integerMap.get(key);
		if (result == null) {
			if (persistentDataHandler.contains(key.getKey())) {
				final Integer pResult = persistentDataHandler.getInteger(key
						.getKey());
				integerMap.put(key, pResult);
				return pResult;
			}
			return def;
		}
		return result;
	}

	@Override
	public String getString(T key) {
		return getString(key, (String) key.getDefault());
	}

	@Override
	public String getString(T key, String def) {
		final String result = stringMap.get(key);
		if (result == null) {
			if (persistentDataHandler.contains(key.getKey())) {
				final String pResult = persistentDataHandler.getString(key
						.getKey());
				stringMap.put(key, pResult);
				return pResult;
			}
			return def;
		}
		return result;
	}

	@Override
	public void putBoolean(T key, boolean value) {
		putBoolean(key, value, false);
	}

	@Override
	public void putBoolean(T key, boolean value, boolean persistent) {
		if (persistent) {
			persistentDataHandler.putBoolean(key.getKey(), value);
		} else if (persistentDataHandler.contains(key.getKey())) {
			throw createPersistentException();
		}

		booleanMap.put(key, value);
		notifyObserver(key, value);
	}

	@Override
	public void putFloat(T key, float value) {
		putFloat(key, value, false);
	}

	@Override
	public void putFloat(T key, float value, boolean persistent) {
		if (persistent) {
			persistentDataHandler.putFloat(key.getKey(), value);
		} else if (persistentDataHandler.contains(key.getKey())) {
			throw createPersistentException();
		}

		floatMap.put(key, value);
		notifyObserver(key, value);
	}

	@Override
	public void putInt(T key, int value) {
		putInt(key, value, false);
	}

	@Override
	public void putInt(T key, int value, boolean persistent) {
		if (persistent) {
			persistentDataHandler.putInteger(key.getKey(), value);
		} else if (persistentDataHandler.contains(key.getKey())) {
			throw createPersistentException();
		}

		integerMap.put(key, value);
		notifyObserver(key, value);
	}

	@Override
	public void putString(T key, String value) {
		putString(key, value, false);
	}

	@Override
	public void putString(T key, String value, boolean persistent) {
		if (persistent) {
			persistentDataHandler.putString(key.getKey(), value);
		} else if (persistentDataHandler.contains(key.getKey())) {
			throw createPersistentException();
		}

		stringMap.put(key, value);
		notifyObserver(key, value);
	}

}
