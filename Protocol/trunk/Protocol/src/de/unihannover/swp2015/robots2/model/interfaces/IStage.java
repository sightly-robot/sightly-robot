package de.unihannover.swp2015.robots2.model.interfaces;

import java.util.List;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IStage extends IAbstractModel {

	/**
	 * Returns the number of IFields on the x-axis
	 * 
	 * @return
	 */
	public int getWidth();

	/**
	 * Returns the number of IFields on the y-axis
	 * 
	 * @return
	 */
	public int getHeight();

	/**
	 * Returns the Field at position x, y
	 * 
	 * @return
	 */
	public IField getField(int x, int y);

	/**
	 * Only available on Server and GUI.
	 * 
	 * @return
	 */
	public List<IPosition> getStartPositions();
}
