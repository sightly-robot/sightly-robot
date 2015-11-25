package de.unihannover.swp2015.robots2.controller.mqtt;

/**
 * 
 * @author Patrick Kawczynski
 * @version 0.3
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
	    
	    int end = input.indexOf('/',begin);
	    if (end == -1)
	        return input.substring(begin);
	    else
	        return input.substring(begin,end);
	}

}
