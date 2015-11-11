package de.unihannover.swp2015.robots2.model.interfaces;

import java.util.Map;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IGame extends IAbstractModel {
	
	/**
	 * 
	 * @return
	 */
	public abstract IMap getMap();
	
	/**
	 * 
	 * @return
	 */
	public abstract boolean isRunning();
	
	/**
	 * 
	 * @return
	 */
	public abstract Map<String, IRobot> getRobots();

}
