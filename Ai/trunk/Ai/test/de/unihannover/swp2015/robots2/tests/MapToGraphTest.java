package de.unihannover.swp2015.robots2.tests;

import de.unihannover.swp2015.robots2.exceptions.InvalidStageException;
import de.unihannover.swp2015.robots2.graph.AIGraph;
import de.unihannover.swp2015.robots2.graph.Node;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import junit.framework.TestCase;

public class MapToGraphTest extends TestCase {
	protected FakeStage stage;
	protected AIGraph resultGraph;

	protected void setUp() {
		this.stage = new FakeStage(100, 100);
	}

	public void testDimensions() throws InvalidStageException {
		AIGraph graph = new AIGraph(this.stage);
		assertTrue(graph.getDimX() == this.stage.getWidth());
		assertTrue(graph.getDimY() == this.stage.getHeight());
	}

	public void testOrientations() throws InvalidStageException {
		AIGraph graph = new AIGraph(this.stage);
		for (int i = 0; i < graph.getDimX(); i++) {
			for (int j = 0; j < graph.getDimY(); j++) {
				for (Orientation orientation : Orientation.values()) {
					// System.out.println("Node pos.: " + i + "," + j + "
					// Orientation: " + orientation.toString());
					/*
					 * See which random walls were set
					 */
					if (graph.getNodes()[i][j].isWall(orientation) && i != 0 && j != 0 && i != graph.getDimX() - 1
							&& j != graph.getDimY() - 1) {
						System.out.println("Wall at: " + i + "," + j);
					}
					assertEquals(graph.getNodes()[i][j].isWall(orientation),
							this.stage.getField(i, j).isWall(orientation));
				}
			}
		}
	}

	public void testStartPositions() throws InvalidStageException {
		AIGraph graph = new AIGraph(this.stage);
		for (IPosition pos : this.stage.getStartPositions()) {
			int x = pos.getX();
			int y = pos.getY();
			Node graphNode = graph.getNodes()[x][y];
			// System.out.println(graphNode.getRobot());
			assertTrue(graphNode.getRobot() != null);
			/*
			 * Test if robot is myself, fails for now because we haven't set one
			 */
			// assertEquals(graphNode.getRobot(), graph.getMyself());
		}
	}
}
