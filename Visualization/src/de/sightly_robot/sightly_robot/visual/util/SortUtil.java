package de.sightly_robot.sightly_robot.visual.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.sightly_robot.sightly_robot.model.interfaces.IRobot;

/**
 * Sort robots by score
 * 
 * All utility methods are related to {@IEntity}.
 * 
 * @author Daphne Schössow
 */
public class SortUtil {

	private SortUtil() {
		// utility class
	}

	/**
	 * Sort robots descending based on the score.
	 *
	 * @param robots
	 *            list of robots to sort
	 */
	public static void sortRobots(final List<IRobot> robots) {
		Collections.sort(robots, new Comparator<IRobot>() {
			@Override
			public int compare(IRobot r1, IRobot r2) {
				return r2.getScore() - r1.getScore();
			}
		});
	}

	/**
	 * Adds a new robot to <code>roboList</code>.
	 * 
	 * The method uses insertion sort, thus the list doesn't have to be sorted
	 * after every insertion. It sorts according to {@link IRobot#getScore()}.
	 * 
	 * @param robot
	 *            robot to be added
	 * @param roboList
	 *            target list
	 */
	public static void addRobotSorted(final IRobot robot,
			final List<IRobot> roboList) {
		for (int i = 0; i < roboList.size() + 1; ++i) {
			if (i == roboList.size()) {
				roboList.add(robot);
				break;
			}

			if (robot.getScore() > roboList.get(i).getScore()) {
				roboList.add(i, robot);
				return;
			}
		}
	}

}
