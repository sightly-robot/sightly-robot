package de.sightly_robot.sightly_robot.visual.core.entity;

import de.sightly_robot.sightly_robot.visual.core.PrefKey;
import de.sightly_robot.sightly_robot.visual.util.pref.IPreferences;
import de.sightly_robot.sightly_robot.visual.util.pref.IPreferencesObserver;

/**
 * This provides a very basic skeleton for components.
 * 
 * @author Rico Schrage
 */
public abstract class Component<T extends IEntity> implements IComponent<T>,
		IPreferencesObserver<PrefKey> {

	/** Registered entity */
	protected T entity;

	/**
	 * Constructs a component with the given {@link IPreferences}.
	 * 
	 * @param pref
	 *            preferences
	 */
	public Component(IPreferences<PrefKey> pref) {
		pref.addObserver(this);
	}

	@Override
	public void onRegister(T entity) {
		this.entity = entity;
	}

}
