package de.unihannover.swp2015.robots2.visual.util.pref;

import java.util.ArrayList;
import java.util.List;

public class PreferencesObservable implements IPreferencesObservable {

	private final List<IPreferencesObserver> observerList = new ArrayList<>();
	
	@Override
	public void notifyObserver(final IPreferencesKey changedKey) {
		for (int i = 0; i < observerList.size(); ++i) {
			this.observerList.get(i).onUpdatePreferences(this, changedKey);
		}
		
	}

	@Override
	public void addObserver(final IPreferencesObserver obs) {
		this.observerList.add(obs);
	}
	
	@Override
	public void removeObserver(final IPreferencesObserver obs) {
		this.observerList.remove(obs);
	}
	
}
