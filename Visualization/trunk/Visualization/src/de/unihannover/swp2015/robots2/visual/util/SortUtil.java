package de.unihannover.swp2015.robots2.visual.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.unihannover.swp2015.robots2.model.interfaces.IRobot;

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
	 * Sort robots on basis of the score (descending). 
	 *
	 * @param robots list of robots you want to sort
	 */
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
