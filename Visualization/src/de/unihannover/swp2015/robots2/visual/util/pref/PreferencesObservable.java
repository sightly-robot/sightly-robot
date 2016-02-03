package de.unihannover.swp2015.robots2.visual.util.pref;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple implementation of {@link IPreferencesObservable}
 * 
 * @author Rico Schrage
 *
 * @param <T>
 *            class which has been used by the preference object for mapping
 */
public class PreferencesObservable<T extends IPreferencesKey> implements
		IPreferencesObservable<T> {

	/** Contains all observers */
	private final List<IPreferencesObserver<T>> observerList = new ArrayList<>();

	@Override
	public void notifyObserver(final T changedKey, final Object value) {
		for (int i = 0; i < observerList.size(); ++i) {
			observerList.get(i).onUpdatePreferences(changedKey, value);
		}
	}

	@Override
	public void addObserver(final IPreferencesObserver<T> obs) {
		observerList.add(obs);
	}

	@Override
	public void removeObserver(final IPreferencesObserver<T> obs) {
		observerList.remove(obs);
	}

}
