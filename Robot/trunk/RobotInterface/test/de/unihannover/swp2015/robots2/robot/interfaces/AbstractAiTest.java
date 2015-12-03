package de.unihannover.swp2015.robots2.robot.interfaces;

import static org.junit.Assert.*;

import org.junit.Test;

import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

/**
 * Tests the AbstractAI.
 * 
 * @author Philipp Rohde
 */
public class AbstractAiTest {

	public static boolean nextOrientationFired = false;
	public static boolean nextButOneOrientationFired = false;
	
	/**
	 * Tests if the AbstractAI events are fired correctly.
	 */
	@Test
	public void test() {		
		AiEventObserver obs = new AiEventObserver(){
						
			@Override
			public boolean nextOrientationEvent(Orientation orientation) {
				AbstractAiTest.nextOrientationFired = true;
				return false;
			}

			@Override
			public void nextButOneOrientationEvent(Orientation orientation) {
				AbstractAiTest.nextButOneOrientationFired = true;
			}
		};
		
		AbstractAi ai = new AbstractAi(null) {	// the iRobotController is not needed for the test
			
			@Override
			public void setRelativeSpeed(double forwards, double sidewards, double backwards) {
				// not needed for this test case
			}
			
		};
		
		ai.setAiEventObserver(obs);
		
		ai.fireNextOrientationEvent(Orientation.NORTH);
		assertEquals("fire next orientation event", true, nextOrientationFired);
		
		ai.fireNextButOneOrientationEvent(Orientation.EAST);
		assertEquals("fire next but one orientation event", true, nextButOneOrientationFired);
	}

}