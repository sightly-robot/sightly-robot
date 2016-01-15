package de.unihannover.swp2015.robots2.visual.core.entity;

import de.unihannover.swp2015.robots2.visual.core.PrefKey;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesObserver;

/**
 * Provides a very basic skeleton for components.
 * 
 * @author Rico Schrage
 */
public abstract class Component implements IComponent, IPreferencesObserver<PrefKey> {

	/** Registered entity */
	protected IEntity entity;
	
	/**
	 * Construct a component with the given {@link IPreferences}
	 * 
	 * @param pref
	 */
	public Component(IPreferences<PrefKey> pref) {
		pref.addObserver(this);
	}
	
	@Override
	public void onRegister(IEntity entity) {
		this.entity = entity;
	}

}
