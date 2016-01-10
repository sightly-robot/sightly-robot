package de.unihannover.swp2015.robots2.visual.test.entity;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.unihannover.swp2015.robots2.model.implementation.Field;
import de.unihannover.swp2015.robots2.model.implementation.Robot;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.visual.core.PrefKey;
import de.unihannover.swp2015.robots2.visual.core.entity.Entity;
import de.unihannover.swp2015.robots2.visual.core.entity.IEntity;
import de.unihannover.swp2015.robots2.visual.core.handler.IGameHandler;
import de.unihannover.swp2015.robots2.visual.util.SortUtil;

public class EntitySortTest {

	public static class TestEntity extends Entity {

		public TestEntity(IGameHandler gameHandler) {super(new Field(0,0), gameHandler);}
		public void draw(Batch b) {}
		public void onModelUpdate(IEvent event) {}
		public void onUpdatePreferences(PrefKey updatedKey, Object value) {}
		public void onManagedModelUpdate(IEvent event) {}

	}	

	@Test
	public void testSortEntities() {
		final IGameHandler mockGameHandler = mock(IGameHandler.class);
		
		final IEntity entity = new TestEntity(mockGameHandler);
		entity.setZIndex(5);

		final IEntity entity_two = new TestEntity(mockGameHandler);
		entity_two.setZIndex(0);

		final IEntity entity_three = new TestEntity(mockGameHandler);
		entity_three.setZIndex(42);

		final IEntity entity_four = new TestEntity(mockGameHandler);
		entity_four.setZIndex(1);

		final IEntity entity_five = new TestEntity(mockGameHandler);
		entity_five.setZIndex(9);

		final List<IEntity> list = new ArrayList<>(5);
		list.add(entity);
		list.add(entity_two);
		list.add(entity_three);
		list.add(entity_four);
		list.add(entity_five);

		final List<IEntity> expected = Arrays.asList(entity_two, entity_four, entity, entity_five, entity_three);

		Entity.sortEntities(list);

		assertTrue("Expect that 'list' and 'expected' are equal \n 'list' = " + list + "\n 'expected = " + expected,
				list.equals(expected));
	}
	
	@Test
	public void testInsertionSortEntities() {
		final IGameHandler mockGameHandler = mock(IGameHandler.class);
		
		final IEntity entity = new TestEntity(mockGameHandler);
		entity.setZIndex(5);

		final IEntity entity_two = new TestEntity(mockGameHandler);
		entity_two.setZIndex(0);

		final IEntity entity_three = new TestEntity(mockGameHandler);
		entity_three.setZIndex(42);

		final IEntity entity_four = new TestEntity(mockGameHandler);
		entity_four.setZIndex(1);

		final IEntity entity_five = new TestEntity(mockGameHandler);
		entity_five.setZIndex(9);

		final List<IEntity> list = new ArrayList<>(5);

		Entity.addEntitySorted(entity, list);
		Entity.addEntitySorted(entity_two, list);
		Entity.addEntitySorted(entity_three, list);
		Entity.addEntitySorted(entity_four, list);
		Entity.addEntitySorted(entity_five, list);

		final List<IEntity> expected = Arrays.asList(entity_two, entity_four, entity, entity_five, entity_three);

		assertTrue("Expect that 'list' and 'expected' are equal \n 'list' = " + list + "\n 'expected = " + expected,
				list.equals(expected));
	}
	
	@Test
	public void testInsertionSortRobot() {
		final Robot roboOne = new Robot("asd",true, true);
		roboOne.setScore(0);

		final Robot roboTwo = new Robot("assd",true, true);
		roboTwo.setScore(1);
		
		final Robot roboThree = new Robot("asssd",true, true);
		roboThree.setScore(42);
		
		final Robot roboFour = new Robot("asssssd",true, true);
		roboFour.setScore(500);
		
		final Robot roboFive = new Robot("asssssssd",true, true);
		roboFive.setScore(4);

		final List<IRobot> list = new ArrayList<>(5);

		SortUtil.addRobotSorted(roboOne, list);
		SortUtil.addRobotSorted(roboTwo, list);
		SortUtil.addRobotSorted(roboThree, list);
		SortUtil.addRobotSorted(roboFour, list);
		SortUtil.addRobotSorted(roboFive, list);

		final List<Robot> expected = Arrays.asList(roboFour, roboThree, roboFive, roboTwo, roboOne);

		assertTrue("Expect that 'list' and 'expected' are equal \n 'list' = " + list + "\n 'expected = " + expected,
				list.equals(expected));
	}	
	
	@Test
	public void testSortRobot() {
		final Robot roboOne = new Robot("asd",true, true);
		roboOne.setScore(0);

		final Robot roboTwo = new Robot("assd",true, true);
		roboTwo.setScore(1);
		
		final Robot roboThree = new Robot("asssd",true, true);
		roboThree.setScore(42);
		
		final Robot roboFour = new Robot("asssssd",true, true);
		roboFour.setScore(500);
		
		final Robot roboFive = new Robot("asssssssd",true, true);
		roboFive.setScore(4);

		final List<IRobot> list = new ArrayList<>(5);

		list.add(roboOne);
		list.add(roboTwo);
		list.add(roboThree);
		list.add(roboFour);
		list.add(roboFive);

		SortUtil.sortRobots(list);
		
		final List<Robot> expected = Arrays.asList(roboFour, roboThree, roboFive, roboTwo, roboOne);

		assertTrue("Expect that 'list' and 'expected' are equal \n 'list' = " + list + "\n 'expected = " + expected,
				list.equals(expected));
	}
}
