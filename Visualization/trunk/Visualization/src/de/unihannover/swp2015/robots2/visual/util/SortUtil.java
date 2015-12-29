package de.unihannover.swp2015.robots2.visual.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.visual.game.RobotGameHandler;
import de.unihannover.swp2015.robots2.visual.game.entity.IEntity;

/**
 * All utility methods related to {@IEntity}.
 * 
 * @author Rico Schrage
 */
public class SortUtil {
	
	private SortUtil() {
		// utility class
	}
	
	/**
	 * Should be called, after you have added an entity to the {@link RobotGameHandler}. As alternative you can call {@link EntityUtil#addEntitySorted(IEntity, List)d} 
	 * to add a new entity. 
	 */
	public static void sortEntities(final List<IEntity> entityList) {
		Collections.sort(entityList);
	}
	
	/**
	 * Adds a new entity to <code>renderUnits</code>. The method uses insertion sort, so you don't have to call {@link SortUtil#sortEntities(List)}
	 * 
	 * @param entity new entity
	 * @param entityList target list
	 */
	public static void addEntitySorted(final IEntity entity, final List<IEntity> entityList) {
		for (int i = 0 ; i < entityList.size() + 1; ++i) {
			if (i == entityList.size()) {
				entityList.add(entity);
				break;
			}
			
			if (entity.getZIndex() < entityList.get(i).getZIndex()) {
				entityList.add(i, entity);
				break;
			}
		}
	}
	
	public static void sortRobots(final List<IRobot> robots) {
		Collections.sort(robots, new Comparator<IRobot>() {
			@Override
			public int compare(IRobot r1, IRobot r2) {
				return r2.getScore()-r1.getScore();
			}
		});
	}
	
	/**
	 * Adds a new robot to <code>roboList</code>. The method uses insertion sort, so you don't have to sort the list after every insertion.
	 * It sorts via {@link IRobot#getScore()}.
	 * 
	 * @param robot robot to be added
	 * @param roboList target list
	 */
	public static void addRobotSorted(final IRobot robot, final List<IRobot> roboList) {
		for (int i = 0 ; i < roboList.size() + 1; ++i) {
			if (i == roboList.size()) {
				roboList.add(robot);
				break;
			}
			
			if (robot.getScore() > roboList.get(i).getScore()) {
				roboList.add(i, robot);
				break;
			}
		}
	}
	
}
