package de.unihannover.swp2015.robots2.visual.util.pref;

public interface IPreferencesObserver {
	
	void onUpdatePreferences(final PreferencesObservable o, final IPreferencesKey updatedKey);

}
