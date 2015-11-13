package de.unihannover.swp2015.robots2.model.writeableInterfaces;

import de.unihannover.swp2015.robots2.model.interfaces.*;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IStageWriteable extends IStage, IAbstractModelWriteable {
	
	/**
	 * Returns all IFieldWriteables of the Map
	 * 
	 * @return
	 */
	public IFieldWriteable getFieldWriteable(int x, int y);
	
	public void clearStartPositions();
	
	public void addStartPosition(int x, int y, Orientation orientation);
	
	public void changeSize(int width, int height);

}
