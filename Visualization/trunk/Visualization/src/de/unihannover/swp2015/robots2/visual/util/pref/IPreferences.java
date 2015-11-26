package de.unihannover.swp2015.robots2.visual.util.pref;

import de.unihannover.swp2015.robots2.visual.util.pref.observer.IPreferencesObservable;

/**
 * The interface describes a combination of a non-persistent and a persistent way to manage several options. It uses an internal {@link com.badlogic.gdx.Preferences} object
 * to handle the data, which should be saved in a persistent way. Furthermore there are several internal map to store settings non-persistent. But the way to access
 * these settings is identical.
 * Important: You can't save non-persistent data with a key, which was already used for persistent data, otherwise you will get an exception.
 * 
 * @author Rico Schrage
 */
public interface IPreferences extends IPreferencesObservable {

	/**
	 * Returns the boolean, which is mapped to <code>key</code>. If there is no value mapped to <code>key</code>, <code>def</code> will be returned.
	 * 
	 * @param key key of the value
	 * @param def value, which should be returned when there is no value mapped to <code>key</code>
	 * @return boolean from the internal map or <code>def</code>
	 */
	boolean getBoolean(final String key, final boolean def);
	
	/**
	 * Returns the float value, which is mapped to <code>key</code>. If there is no value mapped to <code>key</code>, <code>def</code> will be returned.
	 * 
	 * @param key key of the value
	 * @param def value, which should be returned when there is no value mapped to <code>key</code>
	 * @return float value from the internal map or <code>def</code>
	 */
	float getFloat(final String key, final float def);
	
	/**
	 * Returns the integer value, which is mapped to <code>key</code>. If there is no value mapped to <code>key</code>, <code>def</code> will be returned.
	 * 
	 * @param key key of the value
	 * @param def value, which should be returned when there is no value mapped to <code>key</code>
	 * @return integer value from the internal map or <code>def</code>
	 */
	int getInt(final String key, final int def);
	
	/**
	 * Returns the string, which is mapped to <code>key</code>. If there is no value mapped to <code>key</code>, <code>def</code> will be returned.
	 * 
	 * @param key key of the value
	 * @param def value, which should be returned when there is no value mapped to <code>key</code>
	 * @return string from the internal map or <code>def</code>
	 */
	String getString(final String key, final String def);
	
	/**
	 * Puts given <code>value</code> in the map (non-persistent). 
	 * 
	 * @param key - the key, the value should be mapped to
	 * @param value - the value, which should be stored
	 */
	void putBoolean(final String key, final boolean value);
	
	/**
	 * Puts given <code>value</code> in the map. Depending on <code>persistent</code> the value will be stored persistent/non-persistent.
	 * 
	 * @param key - the key, the value should be mapped to
	 * @param value - the value, which should be stored
	 * @param persistent - if true the value will be stored in a persistent way
	 */
	void putBoolean(final String key, final boolean value, final boolean persistent);
	
	/**
	 * Puts given <code>value</code> in the map (non-persistent). 
	 * 
	 * @param key - the key, the value should be mapped to
	 * @param value - the value, which should be stored
	 */
	void putFloat(final String key, final float value);
	
	/**
	 * Puts given <code>value</code> in the map. Depending on <code>persistent</code> the value will be stored persistent/non-persistent.
	 * 
	 * @param key - the key, the value should be mapped to
	 * @param value - the value, which should be stored
	 * @param persistent - if true the value will be stored in a persistent way
	 */
	void putFloat(final String key, final float value, final boolean persistent);
	
	/**
	 * Puts given <code>value</code> in the map (non-persistent). 
	 * 
	 * @param key - the key, the value should be mapped to.
	 * @param value - the value, which should be stored
	 */
	void putInt(final String key, final int value);
	
	/**
	 * Puts given <code>value</code> in the map. Depending on <code>persistent</code> the value will be stored persistent/non-persistent.
	 * 
	 * @param key - the key, the value should be mapped to
	 * @param value - the value, which should be stored
	 * @param persistent - if true the value will be stored in a persistent way
	 */
	void putInt(final String key, final int value, final boolean persistent);
	
	/**
	 * Puts given <code>value</code> in the map (non-persistent). 
	 * 
	 * @param key - the key, the value should be mapped to.
	 * @param value - the value, which should be stored
	 */
	void putString(final String key, final String value);
	
	/**
	 * Puts given <code>value</code> in the map. Depending on <code>persistent</code> the value will be stored persistent/non-persistent.
	 * 
	 * @param key - the key, the value should be mapped to
	 * @param value - the value, which should be stored
	 * @param persistent - if true the value will be stored in a persistent way
	 */
	void putString(final String key, final String value, final boolean persistent);
	
}
