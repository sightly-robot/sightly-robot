package de.unihannover.swp2015.robots2.controller.test.main;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.controller.main.RobotMainController;
import de.unihannover.swp2015.robots2.controller.mqtt.IMqttController;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttController;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

public class RobotMainControllerSendTest {

	private TestReceiveHandler receiveHandler;
	private IMqttController receiver;
	private IRobotController robotController;

	private String robotId;

	/**
	 * Prepare the test-environment
	 */
	@Before
	public void prepare() throws Exception {
		// Start Receiver
		this.restartReceiver();

		// Start ServerMainController
		this.robotController = new RobotMainController(false);
		this.robotController.startMqtt("tcp://localhost");
		this.robotId = this.robotController.getMyself().getId();

		// prepare test-data
		this.receiver.sendMessage("map/walls", "2,2,nw,ne,sw,se", true);
		Thread.sleep(300);
	}

	/**
	 * Test sending the updated position
	 */
	@Test
	public void testUpdatePosition() throws Exception {
		int x = 1;
		int y = 0;
		Orientation o = Orientation.WEST;

		// Test
		this.robotController.updatePosition(x, y, o);
		Thread.sleep(100);

		assertEquals(x + "," + y + "," + o.toString(),
				this.receiveHandler.getValue("robot/position/" + this.robotId));

		// cleanup retained messages
		this.receiver.sendMessage("robot/position/" + this.robotId, null, true);
	}

	/**
	 * Test sending the updated position progress
	 */
	@Test
	public void testUpdatePositionProgress() throws Exception {
		int progress = 42;

		// Test
		this.robotController.updatePositionProgress(progress);
		Thread.sleep(100);

		assertEquals("42", this.receiveHandler
				.getValue("extension/2/robot/progress/" + this.robotId));

		// cleanup retained messages
		this.receiver.sendMessage("extension/2/robot/progress/" + this.robotId,
				null, true);
	}

	/**
	 * Test sending the robot ready state
	 */
	@Test
	public void testSetRobotReady() throws Exception {

		// Test connected
		assertEquals("", this.receiveHandler
				.getValue("extension/2/robot/state/" + this.robotId));

		// Send setposition
		this.receiver.sendMessage("robot/setposition/" + this.robotId, "0,0,s",
				false);
		Thread.sleep(200);

		// Test setupstate
		assertEquals("s", this.receiveHandler
				.getValue("extension/2/robot/state/" + this.robotId));

		// Test setRobotReady
		this.robotController.setRobotReady();
		Thread.sleep(100);

		assertEquals("e", this.receiveHandler
				.getValue("extension/2/robot/state/" + this.robotId));

		// cleanup retained messages
		this.receiver.sendMessage("extension/2/robot/state/" + this.robotId,
				null, true);
	}

	/**
	 * Test sending robotics error report
	 */
	@Test
	public void testReportRoboticsError() throws Exception {

		// Test
		this.robotController.reportRoboticsError();
		Thread.sleep(100);

		assertEquals("r", this.receiveHandler
				.getValue("extension/2/robot/state/" + this.robotId));
		assertEquals("error", this.receiveHandler
				.getValue("event/error/robot/" + this.robotId + "/robotics"));
	}

	/**
	 * Test sending disable myself state
	 */
	@Test
	public void testDisableMyself() throws Exception {

		// Test
		this.robotController.disableMyself();
		Thread.sleep(100);

		assertEquals("x", this.receiveHandler
				.getValue("extension/2/robot/state/" + this.robotId));
	}
	
	/**
	 * Test sending delete myself
	 */
	@Test
	public void testDeleteMyself() throws Exception {
		
		// Test
		this.robotController.deleteMyself();
		Thread.sleep(100);
		
		assertEquals("", this.receiveHandler.getValue("robot/type/"+this.robotId));
		assertEquals("", this.receiveHandler.getValue("extension/2/robot/state/"+this.robotId));
		assertEquals("", this.receiveHandler.getValue("robot/position/"+this.robotId));
	}

	/**
	 * Cleanup prepared messages
	 */
	@After
	public void cleanup() throws Exception {
		this.receiver.sendMessage("map/walls", null, true);
	}

	/**
	 * Start the Test MqttController for receiving and its TestMessageHandler.
	 */
	private void restartReceiver() throws Exception {
		this.receiveHandler = new TestReceiveHandler();
		this.receiver = new MqttController(
				"junit_receiver" + UUID.randomUUID().toString(), receiveHandler,
				Arrays.asList(new String[] { "#" }));
		this.receiver.connect("tcp://localhost");
	}

}
