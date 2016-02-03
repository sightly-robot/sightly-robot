package de.unihannover.swp2015.robots2.controller.test.mqtt;

import static org.junit.Assert.*;

import de.unihannover.swp2015.robots2.controller.mqtt.MqttTopic;

import org.junit.Test;

public class MqttTopicTest {

	@Test
	public void testSimpleMatch() {
		assertEquals(MqttTopic.CONTROL_VIRTUALSPEED,
				MqttTopic.getBy("robot/virtualspeed"));
		assertEquals(MqttTopic.EVENT_ERROR_SERVER_CONNECTION,
				MqttTopic.getBy("event/error/server/connection"));
		assertEquals(MqttTopic.MAP_FOOD, MqttTopic.getBy("map/food"));
	}

	@Test
	public void testWildcardMatch() {
		assertEquals(MqttTopic.ROBOT_POSITION,
				MqttTopic.getBy("robot/position/1a2b3c4d"));
		assertEquals(MqttTopic.FIELD_FOOD, MqttTopic.getBy("map/food/2-12"));
		assertEquals(MqttTopic.EVENT_ERROR_ROBOT_CONNECTION,
				MqttTopic.getBy("event/error/robot/1a2b3c4d/connection"));
		assertEquals(MqttTopic.EVENT_ERROR_ROBOT_ROBOTICS,
				MqttTopic.getBy("event/error/robot/1a2b3c4d/robotics"));
	}

	@Test
	public void testNoMatch() {
		assertEquals(null, MqttTopic.getBy("event/error/server"));
		assertEquals(null, MqttTopic.getBy("robot"));
		assertEquals(null, MqttTopic.getBy("robot/"));
		assertEquals(null, MqttTopic.getBy("robot/+"));
		assertEquals(null, MqttTopic.getBy("robot/discover/x"));
	}

	@Test
	public void testKeyExtraction() {
		assertEquals("1a2b3c4d",
				MqttTopic.ROBOT_TYPE.getKey("robot/type/1a2b3c4d"));
		assertEquals("1a2b3c4d",
				MqttTopic.EVENT_ERROR_ROBOT_CONNECTION
						.getKey("event/error/robot/1a2b3c4d/connection"));
		assertEquals("",
				MqttTopic.EVENT_ERROR_ROBOT_CONNECTION
						.getKey("event/error/robot//connection"));

	}
}
