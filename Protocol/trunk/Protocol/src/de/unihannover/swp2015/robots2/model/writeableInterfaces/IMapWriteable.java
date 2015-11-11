package de.unihannover.swp2015.robots2.model.writeableInterfaces;

import java.util.Vector;
import de.unihannover.swp2015.robots2.model.interfaces.IMap;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IMapWriteable extends IMap, IAbstractModelWriteable {
	
	/**
	 * Returns all IFieldWriteables of the Map
	 * 
	 * @return
	 */
	public abstract Vector<Vector<IFieldWriteable>> getFieldsWriteable();

}
