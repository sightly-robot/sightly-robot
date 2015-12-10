package de.unihannover.swp2015.robots2.visual.util.pref.observer;

import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesKey;

public interface IPreferencesObservable {

	void notifyObserver(final IPreferencesKey changedKey);
	void addObserver(final IPreferencesObserver obs);
	void removeObserver(final IPreferencesObserver obs);
	
}
