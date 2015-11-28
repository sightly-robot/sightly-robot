package de.unihannover.swp2015.robots2.visual.util.pref.observer;

import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesKey;

public interface IPreferencesObserver {
	
	void onUpdatePreferences(final PreferencesObservable o, final IPreferencesKey updatedKey);

}
