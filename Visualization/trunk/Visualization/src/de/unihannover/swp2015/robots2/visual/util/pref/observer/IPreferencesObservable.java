package de.unihannover.swp2015.robots2.visual.util.pref.observer;

public interface IPreferencesObservable {

	void notifyObserver(final String changedKey);
	void addObserver(final IPreferencesObserver obs);
	
}
