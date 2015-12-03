package de.unihannover.swp2015.robots2.abstractrobot;

import static org.junit.Assert.*;

import org.junit.Test;

import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

/**
 * Tests the moving direction enumeration.
 * 
 * @see Direction
 * 
 * @author Philipp Rohde
 */
public class DirectionTest {

	// the moving directions
	private final static Direction FORWARDS = Direction.FORWARDS;
	private final static Direction RIGHT = Direction.RIGHT;
	private final static Direction BACKWARDS = Direction.BACKWARDS;
	private final static Direction LEFT = Direction.LEFT;
	
	// the orientations
	private final static Orientation NORTH = Orientation.NORTH;
	private final static Orientation EAST = Orientation.EAST;
	private final static Orientation SOUTH = Orientation.SOUTH;
	private final static Orientation WEST = Orientation.WEST;
	
	/**
	 * Tests the moving directions for moving north.
	 */
	@Test
	public void testMoveNorth() {
		assertEquals("moving north facing north", FORWARDS, Direction.calcDirection(NORTH, NORTH));
		assertEquals("moving north facing east", LEFT, Direction.calcDirection(EAST, NORTH));
		assertEquals("moving north facing south", BACKWARDS, Direction.calcDirection(SOUTH, NORTH));
		assertEquals("moving north facing west", RIGHT, Direction.calcDirection(WEST, NORTH));
	}
	
	/**
	 * Tests the moving directions for moving east.
	 */
	@Test
	public void testMoveEast() {
		assertEquals("moving east facing north", RIGHT, Direction.calcDirection(NORTH, EAST));
		assertEquals("moving east facing east", FORWARDS, Direction.calcDirection(EAST, EAST));
		assertEquals("moving east facing south", LEFT, Direction.calcDirection(SOUTH, EAST));
		assertEquals("moving east facing west", BACKWARDS, Direction.calcDirection(WEST, EAST));
	}

	/**
	 * Tests the moving directions for moving south.
	 */
	@Test
	public void testMoveSouth() {
		assertEquals("moving south facing north", BACKWARDS, Direction.calcDirection(NORTH, SOUTH));
		assertEquals("moving south facing east", RIGHT, Direction.calcDirection(EAST, SOUTH));
		assertEquals("moving south facing south", FORWARDS, Direction.calcDirection(SOUTH, SOUTH));
		assertEquals("moving south facing west", LEFT, Direction.calcDirection(WEST, SOUTH));
	}
	
	/**
	 * Tests the moving directions for moving west.
	 */
	@Test
	public void testMoveWest() {
		assertEquals("moving west facing north", LEFT, Direction.calcDirection(NORTH, WEST));
		assertEquals("moving west facing east", BACKWARDS, Direction.calcDirection(EAST, WEST));
		assertEquals("moving west facing south", RIGHT, Direction.calcDirection(SOUTH, WEST));
		assertEquals("moving west facing west", FORWARDS, Direction.calcDirection(WEST, WEST));
	}
}
