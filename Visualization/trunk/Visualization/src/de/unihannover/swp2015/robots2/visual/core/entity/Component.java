package de.unihannover.swp2015.robots2.visual.core.entity;

import de.unihannover.swp2015.robots2.visual.core.PrefKey;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;

/**
 * Provides a very basic skeleton for components.
 * 
 * @author Rico Schrage
 */
public abstract class Component implements IComponent {

	/** Registered entity */
	protected IEntity entity;
	
	/** Preference object of the component */
	protected final IPreferences<PrefKey> pref;
	
	/**
	 * Construct a component with the given {@link IPreferences}
	 * 
	 * @param pref
	 */
	public Component(IPreferences<PrefKey> pref) {
		this.pref = pref;
	}
	
	@Override
	public void onRegister(IEntity entity) {
		this.entity = entity;
	}

}
