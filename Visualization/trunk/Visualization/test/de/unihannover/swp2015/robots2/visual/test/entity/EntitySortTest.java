package de.unihannover.swp2015.robots2.visual.test.entity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.unihannover.swp2015.robots2.model.implementation.Field;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
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

		public TestEntity(SpriteBatch batch, IGameHandler gameHandler) {
			super(new Field(0,0), batch, gameHandler);
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

		@Override
		public void onManagedModelUpdate(IEvent event) {
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
	
	public static class GH implements IGameHandler {

		public void update() {}
		public void onModelUpdate(IEvent event) {}
		public void render() {}
		public void dispose() {}
		public void setResourceHandler(IResourceHandler resourceHandler) {}
		public IResourceHandler getResourceHandler() {return null;}
		public void dispatchEvent(IEvent event, IEntity source) {}
		public IPreferences getPreferences() {	return new TestPref();}
		public void resize(int width, int height) {	}
		@Override
		public int getRanking(IRobot robo) {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public void onManagedModelUpdate(IEvent event) {
			// TODO Auto-generated method stub
			
		}
		
	}

	@Test
	public void testSortEntities() {
		final IEntity entity = new TestEntity(null, new GH());
		entity.setZIndex(5);

		final IEntity entity_two = new TestEntity(null, new GH());
		entity_two.setZIndex(0);

		final IEntity entity_three = new TestEntity(null, new GH());
		entity_three.setZIndex(42);

		final IEntity entity_four = new TestEntity(null, new GH());
		entity_four.setZIndex(1);

		final IEntity entity_five = new TestEntity(null, new GH());
		entity_five.setZIndex(9);

		final List<IEntity> list = new ArrayList<>(5);
		list.add(entity);
		list.add(entity_two);
		list.add(entity_three);
		list.add(entity_four);
		list.add(entity_five);

		final List<IEntity> expected = Arrays.asList(entity_two, entity_four, entity, entity_five, entity_three);

		EntityUtil.sortEntities(list);

		assertTrue("Expect that 'list' and 'expected' are equal \n 'list' = " + list + "\n 'expected = " + expected,
				list.equals(expected));
	}
	
	@Test
	public void testInsertionSortEntities() {
		final IEntity entity = new TestEntity(null, new GH());
		entity.setZIndex(5);

		final IEntity entity_two = new TestEntity(null, new GH());
		entity_two.setZIndex(0);

		final IEntity entity_three = new TestEntity(null, new GH());
		entity_three.setZIndex(42);

		final IEntity entity_four = new TestEntity(null, new GH());
		entity_four.setZIndex(1);

		final IEntity entity_five = new TestEntity(null, new GH());
		entity_five.setZIndex(9);

		final List<IEntity> list = new ArrayList<>(5);

		EntityUtil.addEntitySorted(entity, list);
		EntityUtil.addEntitySorted(entity_two, list);
		EntityUtil.addEntitySorted(entity_three, list);
		EntityUtil.addEntitySorted(entity_four, list);
		EntityUtil.addEntitySorted(entity_five, list);

		final List<IEntity> expected = Arrays.asList(entity_two, entity_four, entity, entity_five, entity_three);

		assertTrue("Expect that 'list' and 'expected' are equal \n 'list' = " + list + "\n 'expected = " + expected,
				list.equals(expected));
	}
}
