package de.unihannover.swp2015.robots2.controller.mqtt;

/**
 * An enumeration of all MQTT topics we use, equipped with methods to parse
 * topic from string and generate string from topic.
 * 
 * @author Patrick Kawczynski
 * @author Michael Thies
 * @version 0.3
 */
public enum MqttTopic {

	ROBOT_DISCOVER("robot/discover", false),

	ROBOT_NEW("robot/new", false),

	ROBOT_TYPE("robot/type/+", true),

	ROBOT_POSITION("robot/position/+", true),

	ROBOT_SETPOSITION("robot/setposition/+", false),

	/**
	 * Let Robot with given ID (+) blink in given color.
	 * 
	 * Message format = <R>,<G>,<B> with R,G,B = [0-255] From standard
	 * extensions, v1.0
	 */
	ROBOT_BLINK("robot/blink/+", false),

	ROBOT_VIRTUALSPEED("robot/virtualspeed", true),

	MAP_WALLS("map/walls", true),

	MAP_FOOD("map/food", false),

	FIELD_FOOD("map/food/+", true),

	MAP_INIT_FOOD("extension/2/map/setfood", false),
			
	MAP_INIT_GROWINGRATE("extension/2/map/setgrowrate", true),

	FIELD_OCCUPIED_LOCK("map/occupied/lock/+", false),

	FIELD_OCCUPIED_SET("map/occupied/set/+", false),

	FIELD_OCCUPIED_RELEASE("map/occupied/release/+", false),

	CONTROL_STATE("control/state", true),

	EVENT_ERROR_SERVER_CONNECTION("event/error/server/connection", false),

	EVENT_ERROR_ROBOT_CONNECTION("event/error/robot/+/connection", false),

	EVENT_ERROR_ROBOT_ROBOTICS("event/error/robot/+/robotics", false),

	/** Get the current hardware settings of the given robot. */
	SETTINGS_ROBOT_REQUEST("extension/2/settings/robot/+/request", false),

	/** Answer of the robot to SETTINGS_ROBOT_REQUEST. */
	SETTINGS_ROBOT_RESPONSE("extension/2/settings/robot/+/response", false),

	/** Set hardware settings of the given robot. */
	SETTINGS_ROBOT_SET("extension/2/settings/robot/+/set", false),

	/** Get the current hardware settings of the given robot. */
	SETTINGS_VISU_REQUEST("extension/2/settings/visu/request", false),

	/** Answer of the robot to SETTINGS_ROBOT_REQUEST. */
	SETTINGS_VISU_RESPONSE("extension/2/settings/visu/response", false),

	/** Set hardware settings of the given robot. */
	SETTINGS_VISU_SET("extension/2/settings/visu/set", false);

	private final String topic;
	private final boolean retained;

	/**
	 * Returns the MqttTopic-Object by the given topic string. This method is
	 * able to expand mqtt wildcards (+, #) used in stored topics.
	 * 
	 * @param input
	 *            The topic
	 * @return A mqtt-topic object or null, if the given topic does not exists
	 *         in this enumeration.
	 */
	public static MqttTopic getBy(String input) {

		for (MqttTopic topic : MqttTopic.values()) {
			String expression = topic.toString();
			int i = 0, j = 0;
			while (i < input.length() && j < expression.length()) {
				if (input.charAt(i) == expression.charAt(j)) {
					i++;
					j++;
				} else if (expression.charAt(j) == '#') {
					// Wildcard for any following string => accept
					return topic;
				} else if (expression.charAt(j) == '+') {
					// Wildcard for any string before next slash
					while (i < input.length() && input.charAt(i) != '/')
						i++;
					j++;
				} else {
					// Strings to not match
					break;
				}
			}
			if (i == input.length() && j == expression.length()) {
				// Exact match
				return topic;
			}
		}

		return null;
	}

	private MqttTopic(String topic, boolean retained) {
		this.topic = topic;
		this.retained = retained;
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

	/**
	 * Returns the key in the given topic, which replaced the "+" wildcard of
	 * the general topic.
	 * 
	 * @param input
	 *            the topic which holds the key
	 * @return the extracted key; or null, if no key was found
	 */
	public String getKey(String input) {
		int begin = this.topic.indexOf('+');

		if (begin == -1)
			return null;

		int end = input.indexOf('/', begin);
		if (end == -1)
			return input.substring(begin);
		else
			return input.substring(begin, end);
	}

	/**
	 * Check whether messages published to this topic should be sent as retained
	 * messages, that are persisted at the MQTT broker.
	 * 
	 * @return Whether to retain messages to this topic
	 */
	public boolean isRetained() {
		return this.retained;
	}

}
