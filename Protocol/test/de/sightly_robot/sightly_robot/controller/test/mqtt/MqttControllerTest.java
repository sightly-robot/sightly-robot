package de.sightly_robot.sightly_robot.controller.test.mqtt;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.Test;

import de.sightly_robot.sightly_robot.controller.mqtt.IMqttController;
import de.sightly_robot.sightly_robot.controller.mqtt.IMqttMessageHandler;
import de.sightly_robot.sightly_robot.controller.mqtt.MqttController;

/**
 * Unit Test for the MQTTController.
 * 
 * You must have an MQTT broker running at localhost, default port for this
 * test.
 * 
 * @author Michael Thies
 */
public class MqttControllerTest {
	private static class TestReceiveHandler implements IMqttMessageHandler {
		Map<String, String> values = new HashMap<String, String>();
		final String id;

		public TestReceiveHandler(String id) {
			this.id = id;
		}

		@Override
		public void handleMqttMessage(String topic, String message) {
			System.out.println("( " + this.id + " ) message: " + topic + ": "
					+ message);
			this.values.put(topic, message);
		}

		public String getValue(String key) {
			return this.values.get(key);
		}

		@Override
		public void onMqttStateChange(boolean connected) {
		}

	}

	@Test
	public void test() throws Exception {
		TestReceiveHandler messageHandler1 = new TestReceiveHandler("1");
		IMqttController mqttController1 = new MqttController(
				"junit_testclient1", messageHandler1,
				Arrays.asList(new String[] { "test/x", "test/b" }));
		assertNotNull("1st MQTT controller was not constructed propelery",
				mqttController1);
		mqttController1.connect("tcp://localhost");

		TestReceiveHandler messageHandler2 = new TestReceiveHandler("2");
		IMqttController mqttController2 = new MqttController(
				"junit_testclient2", messageHandler2,
				Arrays.asList(new String[] { "test/y", "test/b" }));
		mqttController2.connect("tcp://localhost");

		for (int i = 0; i < 100; i++) {
			mqttController2.sendMessage("test/x", Integer.toString(i), false);
		}
		mqttController2.sendMessage("test/x", "x", false);
		mqttController1.sendMessage("test/y", "y", false);
		mqttController2.sendMessage("test/b", "b", false);

		Thread.sleep(500);

		assertEquals("x", messageHandler1.getValue("test/x"));
		assertEquals("y", messageHandler2.getValue("test/y"));
		assertEquals("b", messageHandler1.getValue("test/b"));
		assertEquals("b", messageHandler2.getValue("test/b"));
		assertEquals(null, messageHandler2.getValue("test/x"));
		assertEquals(null, messageHandler1.getValue("test/y"));
	}

	@Test
	public void testRetained() throws Exception {
		TestReceiveHandler messageHandler3 = new TestReceiveHandler("3");
		IMqttController mqttController3 = new MqttController(
				"junit_testclient3", messageHandler3,
				Arrays.asList(new String[] { "test/a" }));
		mqttController3.connect("tcp://localhost");

		mqttController3.sendMessage("test/a", "blub", true);

		Thread.sleep(100);

		messageHandler3 = new TestReceiveHandler("4");
		mqttController3 = new MqttController("junit_testclient3",
				messageHandler3, Arrays.asList(new String[] { "test/a" }));
		mqttController3.connect("tcp://localhost");

		Thread.sleep(100);

		assertEquals("blub", messageHandler3.getValue("test/a"));
		mqttController3.sendMessage("test/a", null, true);

		Thread.sleep(100);

		messageHandler3 = new TestReceiveHandler("5");
		mqttController3 = new MqttController("junit_testclient3",
				messageHandler3, Arrays.asList(new String[] { "test/a" }));
		mqttController3.connect("tcp://localhost");

		Thread.sleep(100);

		assertEquals(null, messageHandler3.getValue("test/a"));
	}

	@Test
	public void testBrokerChangeAndSendWait() throws Exception {
		// Start 4th MQTTController
		TestReceiveHandler messageHandler4 = new TestReceiveHandler("4");
		IMqttController mqttController4 = new MqttController(
				"junit_testclient3", messageHandler4,
				Arrays.asList(new String[] { }));
		
		// Try connecting to wrong broker url
		try {
			mqttController4.connect("tcp://wibblywobbly");
			fail("No Exception on wrong broker URL");
		} catch (MqttException e) {
		}
		
		// Start 5th MQTTController
		TestReceiveHandler messageHandler5 = new TestReceiveHandler("2");
		IMqttController mqttController5 = new MqttController(
				"junit_testclient5", messageHandler5,
				Arrays.asList(new String[] { "test/x" }));
		mqttController5.connect("tcp://localhost");
		

		// Send message from 4, which should not arrive
		mqttController4.sendMessage("test/x", "x", false);
		
		Thread.sleep(100);
		assertEquals(null, messageHandler5.getValue("text/x"));
		
		// Correctly connect MQTTController 4
		mqttController4.connect("tcp://localhost");
		
		// Message should arrive now
		assertEquals(null, messageHandler5.getValue("text/x"));
	}

}
