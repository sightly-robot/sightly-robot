package de.sightly_robot.sightly_robot.model.test.implementation;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;

import de.sightly_robot.sightly_robot.model.implementation.Field;
import de.sightly_robot.sightly_robot.model.interfaces.IField.State;
import de.sightly_robot.sightly_robot.model.interfaces.IPosition.Orientation;

/**
 * Simple JUnitTest for the field-model
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class FieldTest {

	@Test
	public void testInit() {
		Field field = new Field(3, 4);
		
		assertEquals(3, field.getX());
		assertEquals(4, field.getY());
		assertEquals(State.FREE, field.getState());
		assertEquals(false, field.isWall(Orientation.NORTH));
		assertEquals(false, field.isWall(Orientation.EAST));
		assertEquals(false, field.isWall(Orientation.SOUTH));
		assertEquals(false, field.isWall(Orientation.WEST));
		assertEquals(0, field.getFood());
	}
	
	@Test
	public void testWalls() {
		Field field = new Field(3, 4);
		
		field.setWall(Orientation.EAST, true);
		field.setWall(Orientation.SOUTH, true);
		assertEquals(false, field.isWall(Orientation.NORTH));
		assertEquals(true, field.isWall(Orientation.EAST));
		assertEquals(true, field.isWall(Orientation.SOUTH));
		assertEquals(false, field.isWall(Orientation.WEST));
	}
	
	@Test
	public void testState() {
		Field field = new Field(3, 4);
		
		field.setState(State.LOCKED);
		assertEquals(State.LOCKED, field.getState());
		
		field.setState(State.FREE);
		assertEquals(State.FREE, field.getState());
		
		field.setState(State.OCCUPIED);
		assertEquals(State.OCCUPIED, field.getState());
	}
	
	@Test
	public void testFood() {
		Field field = new Field(3, 4);
		
		field.setFood(6);
		assertEquals(6, field.getFood());
		
		field.setFood(2);
		assertEquals(2, field.getFood());
		
		field.incrementFood(1);
		assertEquals(3, field.getFood());
		
		field.incrementFood(5);
		assertEquals(8, field.getFood());
	}
	
	@Test
	public void testGrowingRate() {
		Field field = new Field(3, 4);
		
		field.setGrowingRate(5);
		assertEquals(5, field.getGrowingRate());
		
		field.setGrowingRate(2);
		assertEquals(2, field.getGrowingRate());
	}
	
	@Test
	public void testLockedBy() {
		Field field = new Field(3, 4);
		
		String uuid = UUID.randomUUID().toString().substring(0, 8);
		field.setLockedBy(uuid);
		assertEquals(uuid, field.getLockedBy());
	}
}
