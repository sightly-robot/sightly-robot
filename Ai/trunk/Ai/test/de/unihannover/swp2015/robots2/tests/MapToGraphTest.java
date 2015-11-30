package de.unihannover.swp2015.robots2.tests;

import de.unihannover.swp2015.robots2.exceptions.InvalidStageException;
import de.unihannover.swp2015.robots2.graph.AIGraph;
import junit.framework.TestCase;

public class MapToGraphTest extends TestCase {
	protected FakeStage stage;
	protected AIGraph resultGraph;

	protected void setUp() {
		this.stage = new FakeStage();
	}

	public void testLoad() throws InvalidStageException {
		AIGraph graph = new AIGraph(this.stage);
		assertTrue(graph.getDimX() == this.stage.getWidth());
		assertTrue(graph.getDimY() == this.stage.getHeight());
	}
}
