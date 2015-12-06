package de.unihannover.swp2015.robots2.controller.test.main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.unihannover.swp2015.robots2.controller.mqtt.IMqttMessageHandler;

class TestReceiveHandler implements IMqttMessageHandler {
	Map<String, String> values = new HashMap<String, String>();

	@Override
	public void handleMqttMessage(String topic, String message) {
		this.values.put(topic, message);
	}

	public String getValue(String key) {
		return this.values.get(key);
	}

	public String[] getKeysSorted() {
		String[] keys = this.values.keySet().toArray(new String[1]);
		Arrays.sort(keys);
		return keys;
	}

	public void removeValue(String key) {
		this.values.remove(key);
	}

	@Override
	public void onMqttStateChange(boolean connected) {
	}

}