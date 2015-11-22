package de.unihannover.swp2015.robots2.visual.util.pref.observer;

public interface IPreferencesObserver {
	
	void onUpdatePreferences(final PreferencesObservable o, final String updatedKey);

}
