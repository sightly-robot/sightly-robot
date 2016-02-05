package de.sightly_robot.sightly_robot.visual.util.pref;

/**
 * This interface describes a combination of a non-persistent and persistent way
 * to manage several options.
 * 
 * It uses an internal {@link com.badlogic.gdx.Preferences} object to handle the
 * data, which should be saved in a persistent way. Furthermore there are
 * several internal maps to store settings non-persistently. The way to access
 * these settings is identical to the other. <br>
 * Important: You can't save non-persistent data with a key which was already
 * used for persistent data, otherwise you'll get an exception.
 * 
 * @author Rico Schrage
 */
public interface IPreferences<T extends IPreferencesKey> extends
		IPreferencesObservable<T> {

	/**
	 * Returns the boolean which is mapped to <code>key</code>.
	 * 
	 * If there is no value mapped to <code>key</code>, <code>def</code> will be
	 * returned.
	 * 
	 * @param key
	 *            key of the value
	 * @param def
	 *            default value which should be returned when no value is mapped
	 *            to <code>key</code>
	 * @return boolean from the internal map or <code>def</code>
	 */
	boolean getBoolean(final T key, final boolean def);

	/**
	 * Calls {@link #getBoolean(IPreferencesKey, boolean)} with false as default
	 * value.
	 * 
	 * @param key
	 *            key of the value
	 * @return boolean from the internal map or <code>false</code>
	 */
	boolean getBoolean(final T key);

	/**
	 * Returns the float value which is mapped to <code>key</code>.
	 * 
	 * If there is no value mapped to <code>key</code>, <code>def</code> will be
	 * returned.
	 * 
	 * @param key
	 *            key of the value
	 * @param def
	 *            default value which should be returned when there is no value
	 *            mapped to <code>key</code>
	 * @return float value from the internal map or <code>def</code>
	 */
	float getFloat(final T key, final float def);

	/**
	 * Calls {@link #getFloat(IPreferencesKey, float)} with 0.0 as default
	 * value.
	 * 
	 * @param key
	 *            key of the value
	 * @return float from the internal map or <code>0.0</code>
	 */
	float getFloat(final T key);

	/**
	 * Returns the integer value which is mapped to <code>key</code>.
	 * 
	 * If there is no value mapped to <code>key</code>, <code>def</code> will be
	 * returned.
	 * 
	 * @param key
	 *            key of the value
	 * @param def
	 *            default value which should be returned when there is no value
	 *            mapped to <code>key</code>
	 * @return integer value from the internal map or <code>def</code>
	 */
	int getInt(final T key, final int def);

	/**
	 * Calls {@link #getInt(IPreferencesKey, int)} with 0 as default value.
	 * 
	 * @param key
	 *            key of the value
	 * @return integer from the internal map or <code>0</code>
	 */
	int getInt(final T key);

	/**
	 * Returns the string which is mapped to <code>key</code>.
	 * 
	 * If there is no value mapped to <code>key</code>, <code>def</code> will be
	 * returned.
	 * 
	 * @param key
	 *            key of the value
	 * @param def
	 *            default value which should be returned when there is no value
	 *            mapped to <code>key</code>
	 * @return string from the internal map or <code>def</code>
	 */
	String getString(final T key, final String def);

	/**
	 * Calls {@link #getString(IPreferencesKey, String)} with <code>""</code> as
	 * default value.
	 * 
	 * @param key
	 *            key of the value
	 * @return string from the internal map or <code>""</code>
	 */
	String getString(final T key);

	/**
	 * Puts given <code>value</code> in the map (non-persistently).
	 * 
	 * @param key
	 *            the key to which the value should be mapped
	 * @param value
	 *            the value which should be stored
	 */
	void putBoolean(final T key, final boolean value);

	/**
	 * Puts given <code>value</code> in the map.
	 * 
	 * Depending on <code>persistent</code> the value will be stored
	 * persistently/non-persistently.
	 * 
	 * @param key
	 *            the key to which the value should be mapped
	 * @param value
	 *            the value which should be stored
	 * @param persistent
	 *            if true, the value will be stored persistently
	 */
	void putBoolean(final T key, final boolean value, final boolean persistent);

	/**
	 * Puts given <code>value</code> in the map (non-persistently).
	 * 
	 * @param key
	 *            the key to which the value should be mapped
	 * @param value
	 *            the value which should be stored
	 */
	void putFloat(final T key, final float value);

	/**
	 * Puts given <code>value</code> in the map.
	 * 
	 * Depending on <code>persistent</code> the value will be stored
	 * persistently/non-persistently.
	 * 
	 * @param key
	 *            the key to which the value should be mapped
	 * @param value
	 *            the value which should be stored
	 * @param persistent
	 *            if true, the value will be stored persistently
	 */
	void putFloat(final T key, final float value, final boolean persistent);

	/**
	 * Puts given <code>value</code> in the map (non-persistently).
	 * 
	 * @param key
	 *            the key to which the value should be mapped
	 * @param value
	 *            the value which should be stored
	 */
	void putInt(final T key, final int value);

	/**
	 * Puts given <code>value</code> in the map.
	 * 
	 * Depending on <code>persistent</code> the value will be stored
	 * persistently/non-persistently.
	 * 
	 * @param key
	 *            the key to which the value should be mapped
	 * @param value
	 *            the value which should be stored
	 * @param persistent
	 *            if true, the value will be stored persistently
	 */
	void putInt(final T key, final int value, final boolean persistent);

	/**
	 * Puts given <code>value</code> in the map (non-persistent).
	 * 
	 * @param key
	 *            the key to which the value should be mapped
	 * @param value
	 *            the value which should be stored
	 */
	void putString(final T key, final String value);

	/**
	 * Puts given <code>value</code> in the map.
	 * 
	 * Depending on <code>persistent</code> the value will be stored
	 * persistently/non-persistently.
	 * 
	 * @param key
	 *            the key to which the value should be mapped
	 * @param value
	 *            the value which should be stored
	 * @param persistent
	 *            if true, the value will be stored persistently
	 */
	void putString(final T key, final String value, final boolean persistent);

}
