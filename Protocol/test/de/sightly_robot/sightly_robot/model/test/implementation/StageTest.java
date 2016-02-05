package de.sightly_robot.sightly_robot.model.test.implementation;


import static org.junit.Assert.*;
import org.junit.Test;

import de.sightly_robot.sightly_robot.model.implementation.Stage;

/**
 * Simple JUnitTest for the stage-model
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class StageTest {

	@Test
	public void testResize() {
		Stage stage = new Stage();
		
		stage.changeSize(5, 1);
		assertEquals(5, stage.getWidth());
		assertEquals(1, stage.getHeight());
		
		stage.changeSize(4, 14);
		assertEquals(4, stage.getWidth());
		assertEquals(14, stage.getHeight());
	}
	
}
