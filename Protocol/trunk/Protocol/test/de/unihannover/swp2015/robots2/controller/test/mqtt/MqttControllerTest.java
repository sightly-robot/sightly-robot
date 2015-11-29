package de.unihannover.swp2015.robots2.controller.test.mqtt;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.unihannover.swp2015.robots2.controller.mqtt.IMqttController;
import de.unihannover.swp2015.robots2.controller.mqtt.IMqttMessageHandler;
import de.unihannover.swp2015.robots2.controller.mqtt.MqttController;

/**
 * Unit Test for the MQTTController.
 * 
 * You must have an MQTT broker running at localhost, default port for this test.
 * 
 * @author Michael Thies
 */
public class MqttControllerTest {
	class TestReceiveHandler implements IMqttMessageHandler {
		Map<String, String> values = new HashMap<String, String>();
		final String id;
		
		public TestReceiveHandler (String id) {
			this.id = id;
		}

		@Override
		public void handleMqttMessage(String topic, String message) {
			System.out.println("( " + this.id + " ) message: " + topic + ": " + message);
			this.values.put(topic, message);
		}
		
		public String getValue(String key) {
			return this.values.get(key);
		}

	}

	@Test
	public void test() throws Exception {
		TestReceiveHandler messageHandler1 = new TestReceiveHandler("1");
		IMqttController mqttController1 = new MqttController("tcp://localhost",
				"junit_testclient1", messageHandler1,
				Arrays.asList(new String[] { "test/x", "test/b" }));
		assertNotNull("1st MQTT controller was not constructed propelery",
				mqttController1);

		TestReceiveHandler messageHandler2 = new TestReceiveHandler("2");
		IMqttController mqttController2 = new MqttController("tcp://localhost",
				"junit_testclient2", messageHandler2,
				Arrays.asList(new String[] { "test/y", "test/b" }));
		
		for (int i=0; i<100; i++) {
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
		IMqttController mqttController3 = new MqttController("tcp://localhost",
				"junit_testclient3", messageHandler3,
				Arrays.asList(new String[] { }));
		
		mqttController3.sendMessage("test/a", "blub", true);
		
		
		Thread.sleep(100);

		messageHandler3 = new TestReceiveHandler("3");
		mqttController3 = new MqttController("tcp://localhost",
				"junit_testclient3", messageHandler3,
				Arrays.asList(new String[] { "test/a" }));

		Thread.sleep(100);
		
		assertEquals("blub", messageHandler3.getValue("test/a"));
		mqttController3.deleteRetainedMessage("test/a");
		
		
		Thread.sleep(100);

		messageHandler3 = new TestReceiveHandler("3");
		mqttController3 = new MqttController("tcp://localhost",
				"junit_testclient3", messageHandler3,
				Arrays.asList(new String[] { "test/a" }));

		Thread.sleep(100);
		
		assertEquals(null, messageHandler3.getValue("test/a"));
	}

}
