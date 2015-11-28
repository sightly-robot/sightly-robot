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
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferencesKey;
import de.unihannover.swp2015.robots2.visual.util.pref.observer.IPreferencesObserver;
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
		public void onUpdatePreferences(PreferencesObservable o, IPreferencesKey updatedKey) {
			// TODO Auto-generated method stub
			
		}

	}	
	
	public static class TestPref implements IPreferences {

		public void notifyObserver(IPreferencesKey changedKey) {}
		public void addObserver(IPreferencesObserver obs) {}
		public boolean getBoolean(IPreferencesKey key, boolean def) {return false;}
		public float getFloat(IPreferencesKey key, float def) {return 0;}
		public int getInt(IPreferencesKey key, int def) {return 0;}
		public String getString(IPreferencesKey key, String def) {return null;}
		public void putBoolean(IPreferencesKey key, boolean value) {}
		public void putBoolean(IPreferencesKey key, boolean value, boolean persistent) {}
		public void putFloat(IPreferencesKey key, float value) {}
		public void putFloat(IPreferencesKey key, float value, boolean persistent) {}
		public void putInt(IPreferencesKey key, int value) {}
		public void putInt(IPreferencesKey key, int value, boolean persistent) {}
		public void putString(IPreferencesKey key, String value) {}
		public void putString(IPreferencesKey key, String value, boolean persistent) {}
		
	}

	@Test
	public void testSortEntities() {
		final IEntity entity = new TestEntity(null, null, new TestPref(), null);
		entity.setZIndex(5);

		final IEntity entity_two = new TestEntity(null, null, new TestPref(), null);
		entity_two.setZIndex(0);

		final IEntity entity_three = new TestEntity(null, null, new TestPref(), null);
		entity_three.setZIndex(42);

		final IEntity entity_four = new TestEntity(null, null, new TestPref(), null);
		entity_four.setZIndex(1);

		final IEntity entity_five = new TestEntity(null, null, new TestPref(), null);
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
		final IEntity entity = new TestEntity(null, null, new TestPref(), null);
		entity.setZIndex(5);

		final IEntity entity_two = new TestEntity(null, null, new TestPref(), null);
		entity_two.setZIndex(0);

		final IEntity entity_three = new TestEntity(null, null, new TestPref(), null);
		entity_three.setZIndex(42);

		final IEntity entity_four = new TestEntity(null, null, new TestPref(), null);
		entity_four.setZIndex(1);

		final IEntity entity_five = new TestEntity(null, null, new TestPref(), null);
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
