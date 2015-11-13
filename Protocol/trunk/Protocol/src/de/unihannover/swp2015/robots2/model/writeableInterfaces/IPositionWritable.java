package de.unihannover.swp2015.robots2.model.writeableInterfaces;

import de.unihannover.swp2015.robots2.model.interfaces.IPosition;

public interface IPositionWritable extends IPosition {
	
	public void setPosition(int x, int y);
	
	public void setOrientation(Orientation orientation);
	
	public void setProgress(int progress);
}
