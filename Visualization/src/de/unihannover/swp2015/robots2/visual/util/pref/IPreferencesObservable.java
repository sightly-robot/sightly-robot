package de.unihannover.swp2015.robots2.visual.util.pref;

/**
 * Description of an observable specialized for {@link IPreferences}.
 * 
 * @author Rico Schrage
 *
 * @param <T>
 *            enum which implements {@link IPreferencesKey}
 */
public interface IPreferencesObservable<T extends IPreferencesKey> {

	/**
	 * Notify all observers that a value has changed.
	 * 
	 * @param changedKey
	 *            key of which the value has been changed
	 * @param value
	 *            new value
	 */
	void notifyObserver(final T changedKey, final Object value);

	/**
	 * Addition of a new observer
	 * 
	 * This observer will receive a notification when a value has changed.
	 * 
	 * @param obs
	 *            new observer
	 */
	void addObserver(final IPreferencesObserver<T> obs);

	/**
	 * Removal of an observer
	 * 
	 * @param obs
	 *            observer to be removed
	 */
	void removeObserver(final IPreferencesObserver<T> obs);

}
