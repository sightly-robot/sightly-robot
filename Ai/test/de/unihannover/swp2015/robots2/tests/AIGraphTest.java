package de.unihannover.swp2015.robots2.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.unihannover.swp2015.robots2.core.Robot;
import de.unihannover.swp2015.robots2.exceptions.NoValidOrientationException;
import de.unihannover.swp2015.robots2.graph.AIGraph;
import de.unihannover.swp2015.robots2.graph.Node;
import de.unihannover.swp2015.robots2.model.implementation.Position;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

public class AIGraphTest {
	
	AIGraph testGraph;
	
	@Before
	public void initial() {
		testGraph = new AIGraph(3, 3);
		Robot robot = new Robot("testBot");
		Node node = new Node(2, 2);
		robot.setPosition(node);
		robot.setOrientation(Orientation.SOUTH);
		Position pos = new Position(2, 2, Orientation.SOUTH);
		testGraph.setRobotPosition(robot, pos);
		testGraph.setMyself(robot);
	}

	@Test
	public void testGetRandomOrientation() throws NoValidOrientationException {
		assertEquals(Orientation.WEST, testGraph.getRandomOrientation());
	}

}
