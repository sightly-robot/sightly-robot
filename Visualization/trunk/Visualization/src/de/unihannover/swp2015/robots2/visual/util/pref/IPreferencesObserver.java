package de.unihannover.swp2015.robots2.visual.util.pref;

/**
 * Describes an observer of a preference object.
 * 
 * @author Rico Schrage
 *
 * @param <T> type of the key, which will be used for the preference object.
 */
public interface IPreferencesObserver<T extends IPreferencesKey<T>> {
	
	/**
	 * Will be called when a value has changed
	 * 
	 * @param updatedKey key, whose value has changed
	 * @param value new value
	 */
	void onUpdatePreferences(final T updatedKey, final Object value);

}
