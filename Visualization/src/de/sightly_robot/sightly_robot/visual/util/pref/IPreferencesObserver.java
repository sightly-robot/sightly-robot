package de.sightly_robot.sightly_robot.visual.util.pref;

/**
 * Description of an observer of a preference object.
 * 
 * @author Rico Schrage
 *
 * @param <T>
 *            type of the key which will be used for the preference object
 */
public interface IPreferencesObserver<T extends IPreferencesKey> {

	/**
	 * Will be called when a value has changed
	 * 
	 * @param updatedKey
	 *            key of which the value has been changed
	 * @param value
	 *            new value
	 */
	void onUpdatePreferences(final T updatedKey, final Object value);

}
