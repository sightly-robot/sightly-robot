package de.unihannover.swp2015.robots2.visual.util.pref;

/**
 * Describes an observable specialized for {@link IPreferences}.
 * @author Rico Schrage
 *
 * @param <T> enum, which implements {@link IPreferencesKey}
 */
public interface IPreferencesObservable<T extends IPreferencesKey> {

	/**
	 * Notify all observers that a value has changed.
	 * 
	 * @param changedKey key, whose value has changed
	 * @param value new value
	 */
	void notifyObserver(final T changedKey, final Object value);
	
	/**
	 * Add a new observer. This observer will receive a notification when a value has changed.
	 * 
	 * @param obs observer
	 */
	void addObserver(final IPreferencesObserver<T> obs);
	
	/**
	 * Remove an observer.
	 * 
	 * @param obs
	 */
	void removeObserver(final IPreferencesObserver<T> obs);
	
}
