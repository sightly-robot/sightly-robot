package de.unihannover.swp2015.robots2.model.implementation;

import java.util.Vector;

import de.unihannover.swp2015.robots2.external.interfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IMap;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IFieldWriteable;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IMapWriteable;

public class Map extends AbstractModel implements IMap, IMapWriteable {
	
	private int width;
	private int height;
	private Vector<Vector<IFieldWriteable>> fields;

	@Override
	public void observe(IModelObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void emitEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vector<Vector<IFieldWriteable>> getFieldsWriteable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public Vector<Vector<IField>> getFields() {
		// TODO Auto-generated method stub
		return null;
	}

}
