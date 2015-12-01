package de.unihannover.swp2015.robots2.model.test.implementation;

import static org.junit.Assert.*;
import org.junit.Test;

import de.unihannover.swp2015.robots2.model.implementation.Position;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

/**
 * Simple JUnitTest for the position
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class PositionTest {

	@Test
	public void testInit() {
		Position position = new Position(5, 1, Orientation.EAST);
		
		assertEquals(5, position.getX());
		assertEquals(1, position.getY());
		assertEquals(Orientation.EAST, position.getOrientation());
		assertEquals(0, position.getProgress());
	}
	
	@Test
	public void testXY() {
		Position position = new Position(5, 1, Orientation.EAST);
		
		position.setPosition(7, 3);
		assertEquals(7, position.getX());
		assertEquals(3, position.getY());
		
		position.setPosition(0, 11);
		assertEquals(0, position.getX());
		assertEquals(11, position.getY());
	}
	
	@Test
	public void testOrientation() {
		Position position = new Position(5, 1, Orientation.EAST);
		
		position.setOrientation(Orientation.NORTH);
		assertEquals(Orientation.NORTH, position.getOrientation());
		
		position.setOrientation(Orientation.EAST);
		assertEquals(Orientation.EAST, position.getOrientation());
		
		position.setOrientation(Orientation.SOUTH);
		assertEquals(Orientation.SOUTH, position.getOrientation());
		
		position.setOrientation(Orientation.WEST);
		assertEquals(Orientation.WEST, position.getOrientation());
	}
	
	@Test
	public void testProgress() {
		Position position = new Position(5, 1, Orientation.EAST);
		
		position.setProgress(7);
		assertEquals(7, position.getProgress());
		
		position.setProgress(0);
		assertEquals(0, position.getProgress());
		
		position.setProgress(823);
		assertEquals(832, position.getProgress());
	}
}
