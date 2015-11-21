package de.unihannover.swp2015.robots2.model.interfaces;

import java.awt.Color;

import de.unihannover.swp2015.robots2.model.interfaces.IPosition;

/**
 * Interface for all the read-only actions on a Robot object to be used by the
 * controllers.
 * 
 * A Robot objects represents any kind of Robot that takes part in the game.
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public interface IRobot extends IAbstractModel {

	/**
	 * Returns the Robot-Identification-String
	 * 
	 * @return the Robot-Id
	 */
	public String getId();

	/**
	 * Returns the Robot-Name
	 * 
	 * @return the Robot-Name
	 */
	public String getName();

	/**
	 * Returns true if it's a hardware-robot and false, if the robot is a
	 * virtual one.
	 * 
	 * @return true: the robot exists physically; false: if not
	 */
	public boolean isHardwareRobot();

	/**
	 * Returns the current read-only position of the robot.
	 * 
	 * @return an IPosition object with the current position
	 */
	public IPosition getPosition();

	/**
	 * Returns the current score of the robot.
	 * 
	 * @return the current score
	 */
	public int getScore();

	/**
	 * Returns true if an setup state is set and false else.
	 * 
	 * This should happen after the robot got its start position and should be
	 * revoked when the game is started or the user indicates the robot is ready
	 * and in position.
	 * 
	 * @return true: setup state is set; false: if not
	 */
	public boolean isSetupState();

	/**
	 * Returns true, if the instance of the class run on the robot who's
	 * described by the instance.
	 * 
	 * @return true: the instance described myself; false: if not
	 */
	public boolean isMyself();

	/**
	 * Returns the color of the robot.
	 * 
	 * @return A generated color.
	 */
	public Color getColor();

	/**
	 * Returns true if an error state is set and false else.
	 * 
	 * @return true: error state is set; false: if not
	 */
	public boolean isErrorState();

}
