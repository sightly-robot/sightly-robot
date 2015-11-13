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
	public IStage getStage();

	/**
	 * 
	 * @return
	 */
	public boolean isRunning();

	public float getVRobotSpeed();

	public int getHesitationTime();

	/**
	 * 
	 * @return
	 */
	public Map<String, ? extends IRobot> getRobots();

}
