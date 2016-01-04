package de.unihannover.swp2015.robots2.visual.util.pref;

public interface IPreferencesObservable {

	void notifyObserver(final IPreferencesKey changedKey);
	void addObserver(final IPreferencesObserver obs);
	void removeObserver(final IPreferencesObserver obs);
	
}
