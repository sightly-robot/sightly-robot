package de.unihannover.swp2015.robots2.visual.util;

import java.util.Collections;
import java.util.List;

import de.unihannover.swp2015.robots2.visual.core.RobotGameHandler;
import de.unihannover.swp2015.robots2.visual.entity.IEntity;

/**
 * All utility methods related to {@IEntity}.
 * 
 * @author Rico Schrage
 */
public class EntityUtil {
	
	/**
	 * Should be called, after you have added an entity to the {@link RobotGameHandler}. As alternative you can call {@link EntityUtil#addEntitySorted(IEntity, List)d} 
	 * to add a new entity. 
	 */
	public static void sortEntities(final List<IEntity> entityList) {
		Collections.sort(entityList);
	}
	
	/**
	 * Adds a new entity to <code>renderUnits</code>. The method uses insertion sort, so you don't have to call {@link EntityUtil#sortEntities(List)}
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
	
}
