package de.unihannover.swp2015.robots2.visual.test.entity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.visual.core.IGameHandler;
import de.unihannover.swp2015.robots2.visual.entity.Entity;
import de.unihannover.swp2015.robots2.visual.entity.IEntity;
import de.unihannover.swp2015.robots2.visual.resource.IResourceHandler;
import de.unihannover.swp2015.robots2.visual.util.EntityUtil;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;
import de.unihannover.swp2015.robots2.visual.util.pref.Preferences;
import de.unihannover.swp2015.robots2.visual.util.pref.observer.PreferencesObservable;

public class EntitySortTest {

	public static class TestEntity extends Entity {

		public TestEntity(SpriteBatch batch, IGameHandler gameHandler, IPreferences prefs,
				IResourceHandler resHandler) {
			super(batch, gameHandler, prefs, resHandler);
		}

		@Override
		public void render() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onModelUpdate(IEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUpdatePreferences(PreferencesObservable o, String updatedKey) {
			// TODO Auto-generated method stub
			
		}

	}

	@Test
	public void testSortEntities() {
		final IEntity entity = new TestEntity(null, null, new Preferences(), null);
		entity.setZIndex(5);

		final IEntity entity_two = new TestEntity(null, null, new Preferences(), null);
		entity_two.setZIndex(0);

		final IEntity entity_three = new TestEntity(null, null, new Preferences(), null);
		entity_three.setZIndex(42);

		final IEntity entity_four = new TestEntity(null, null, new Preferences(), null);
		entity_four.setZIndex(1);

		final IEntity entity_five = new TestEntity(null, null, new Preferences(), null);
		entity_five.setZIndex(9);

		final List<IEntity> list = new ArrayList<>(5);
		list.add(entity);
		list.add(entity_two);
		list.add(entity_three);
		list.add(entity_four);
		list.add(entity_five);

		final List<IEntity> expected = Arrays.asList(entity_three, entity_five, entity, entity_four, entity_two);

		EntityUtil.sortEntities(list);

		assertTrue("Expect that 'list' and 'expected' are equal \n 'list' = " + list + "\n 'expected = " + expected,
				list.equals(expected));
	}
	
	@Test
	public void testInsertionSortEntities() {
		final IEntity entity = new TestEntity(null, null, new Preferences(), null);
		entity.setZIndex(5);

		final IEntity entity_two = new TestEntity(null, null, new Preferences(), null);
		entity_two.setZIndex(0);

		final IEntity entity_three = new TestEntity(null, null, new Preferences(), null);
		entity_three.setZIndex(42);

		final IEntity entity_four = new TestEntity(null, null, new Preferences(), null);
		entity_four.setZIndex(1);

		final IEntity entity_five = new TestEntity(null, null, new Preferences(), null);
		entity_five.setZIndex(9);

		final List<IEntity> list = new ArrayList<>(5);

		EntityUtil.addEntitySorted(entity, list);
		EntityUtil.addEntitySorted(entity_two, list);
		EntityUtil.addEntitySorted(entity_three, list);
		EntityUtil.addEntitySorted(entity_four, list);
		EntityUtil.addEntitySorted(entity_five, list);

		final List<IEntity> expected = Arrays.asList(entity_three, entity_five, entity, entity_four, entity_two);

		assertTrue("Expect that 'list' and 'expected' are equal \n 'list' = " + list + "\n 'expected = " + expected,
				list.equals(expected));
	}
}
