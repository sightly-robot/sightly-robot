package de.unihannover.swp2015.robots2.model.implementation;

import java.util.Vector;

import de.unihannover.swp2015.robots2.external.interfaces.IPosition;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class MetaMap extends Map {

	private Vector<IPosition> startposition;
	private Vector<Vector<Integer>> growingRate;
	private int robotSpeed;
	
}
