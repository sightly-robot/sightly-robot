package de.sightly_robot.sightly_robot.controller.test.main;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.sightly_robot.sightly_robot.controller.interfaces.IServerController;
import de.sightly_robot.sightly_robot.controller.main.ServerMainController;
import de.sightly_robot.sightly_robot.controller.mqtt.IMqttController;
import de.sightly_robot.sightly_robot.controller.mqtt.MqttController;

public class ServerMainControllerSendTest {

	private TestReceiveHandler receiveHandler;
	private IMqttController receiver;
	private IServerController serverController;

	private String robotId = UUID.randomUUID().toString().substring(0, 8);

	/**
	 * Prepare the test-environment
	 */
	@Before
	public void prepare() throws Exception {
		// Start Receiver
		this.restartReceiver();
		
		// Start ServerMainController
		this.serverController = new ServerMainController();
		serverController.startMqtt("tcp://localhost");

		// prepare test-data
		this.receiver.sendMessage("robot/type/" + this.robotId, "virtual", true);
		this.receiver.sendMessage("map/walls", "2,2,nw,ne,sw,se", true);
		Thread.sleep(100);
	}

	/**
	 * Test sending the updated score
	 */
	@Test
	public void testUpdateScore() throws Exception {

		int score = 5;

		// Test
		this.serverController.updateScore(this.robotId, score);
		Thread.sleep(100);

		assertEquals("5",
				receiveHandler.getValue("robot/score/" + this.robotId));

		// Test
		this.serverController.updateScore(this.robotId, score);
		Thread.sleep(100);

		assertEquals("5",
				receiveHandler.getValue("robot/score/" + this.robotId));

		// Cleanup retained message
		this.receiver.sendMessage("robot/score/" + this.robotId, null, true);

	}

	/**
	 * Test sending the increased score
	 */
	@Test
	public void testIncreaseScore() throws Exception {
		int score = 5;

		// Test
		this.serverController.increaseScore(this.robotId, score);
		Thread.sleep(100);

		assertEquals("5",
				receiveHandler.getValue("robot/score/" + this.robotId));

		// Test
		this.serverController.increaseScore(this.robotId, score);
		Thread.sleep(100);

		assertEquals("10",
				receiveHandler.getValue("robot/score/" + this.robotId));

		// Cleanup retained message
		this.receiver.sendMessage("robot/score/" + this.robotId, null, true);
	}

	/**
	 * Test sending the updated food
	 */
	@Test
	public void testUpdateFood() throws Exception {
		int food = 5;
		int x = 0;
		int y = 1;

		// Test
		this.serverController.updateFood(x, y, food);
		Thread.sleep(100);

		assertEquals("5", receiveHandler.getValue("map/food/" + x + "-" + y));

		// Test
		this.serverController.updateFood(x, y, food);
		Thread.sleep(100);

		assertEquals("5", receiveHandler.getValue("map/food/" + x + "-" + y));

		this.receiver.sendMessage("map/food/" + x + "-" + y, null, true);
	}

	/**
	 * Test sending the increased food
	 */
	@Test
	public void testIncreaseFood() throws Exception {
		int food = 5;
		int x = 0;
		int y = 1;

		// Test
		this.serverController.increaseFood(x, y, food);
		Thread.sleep(100);

		assertEquals("5", receiveHandler.getValue("map/food/" + x + "-" + y));

		// Test
		this.serverController.increaseFood(x, y, food);
		Thread.sleep(100);

		assertEquals("10", receiveHandler.getValue("map/food/" + x + "-" + y));

		this.receiver.sendMessage("map/food/" + x + "-" + y, null, true);
	}

	/**
	 * Cleanup prepared messages
	 */
	@After
	public void cleanup() throws Exception {
		this.receiver.sendMessage("robot/type/" + this.robotId, null, true);
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
