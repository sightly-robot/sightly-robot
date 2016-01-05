package de.unihannover.swp2015.robots2.visual.core.entity;

/**
 * Provides a very basic skeleton for components.
 * 
 * @author Rico Schrage
 */
public abstract class Component implements IComponent {

	/**
	 * Registered entity
	 */
	protected IEntity entity;
	
	@Override
	public void onRegister(IEntity entity) {
		this.entity = entity;
	}

}
