package de.sightly_robot.sightly_robot.robot.interfaces;

import static org.junit.Assert.*;

import org.junit.Test;

import de.sightly_robot.sightly_robot.model.interfaces.IPosition.Orientation;
import de.sightly_robot.sightly_robot.robot.interfaces.AbstractAI;
import de.sightly_robot.sightly_robot.robot.interfaces.AiEventObserver;

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
		
		AbstractAI ai = new AbstractAI(null) {	// the iRobotController is not needed for the test
			
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