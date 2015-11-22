package de.unihannover.swp2015.robots2.controller.mqtt;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Patrick Kawczynski
 * @version 0.1
 */
public enum MqttTopic {

	ROBOT_DISCOVER("robot/discover"), 
	ROBOT_NEW("robot/new"), 
	ROBOT_TYPE("robot/type/+"), 
	ROBOT_POSITION("robot/position/+"), 
	ROBOT_SETPOSITION("robot/setposition/+"), 
	ROBOT_VIRTUALSPEED("robot/virtualspeed"), 
	MAP_WALLS("map/walls"), 
	MAP_FOOD("map/food"), 
	FIELD_FOOD("map/food/+"), 
	FIELD_OCCUPIED_LOCK("map/occupied/lock/+"), 
	FIELD_OCCUPIED_SET("map/occupied/set/+"), 
	FIELD_OCCUPIED_RELEASE("map/occupied/release/+"), 
	CONTROL_STATE("control/state"), 
	EVENT_ERROR_SERVER_CONNECTION("event/error/server/connection"), 
	EVENT_ERROR_ROBOT_CONNECTION("event/error/robot/+/connection"), 
	EVENT_ERROR_ROBOT_ROBOTICS("event/error/robot/+/robotics");

	private final String topic;
	// Reverse-lookup map for getting a MqttTopic-Object from an Topic-String
	private static final Map<String, MqttTopic> lookup = new HashMap<String, MqttTopic>();

	/**
	 * Creates the reverse-lookup map
	 */
	static {
		for (MqttTopic topic : MqttTopic.values()) {
			lookup.put(topic.toString(), topic);
		}
	}

	/**
	 * Returns the MqttTopic-Object by the given topic. This method can also
	 * find topics, which contains keys instead of a "+" or a "#".
	 * 
	 * @param topic
	 *            The topic
	 * @return A mqtt-topic object or null, if the given topic does not exists
	 *         in this enumeration.
	 */
	public static MqttTopic getBy(String topic) {

		String[] topicParts = topic.split("/");

		Set<String> topics = lookup.keySet();
		for (String currentTopic : topics) {
			String[] currentTopicParts = currentTopic.split("/");

			for (int i = 0; i < currentTopicParts.length; i++) {
				if (currentTopicParts[i].equals("+") || currentTopicParts[i].equals(topicParts[i])) {
					if (i == currentTopicParts.length - 1) {
						return lookup.get(currentTopic);
					} else {
						continue;
					}
				} else {
					break;
				}
			}
		}

		return null;
	}

	private MqttTopic(String topic) {
		this.topic = topic;
	}

	@Override
	public String toString() {
		return this.topic;
	}

	/**
	 * Returns the String-value of the topic and replace the "+" with the given
	 * key.
	 * 
	 * @param topicPart
	 *            The key, which will be inserted in the topic-string
	 * @return The complete topic-string
	 */
	public String toString(String topicPart) {
		return this.topic.replace("+", topicPart);
	}

}
