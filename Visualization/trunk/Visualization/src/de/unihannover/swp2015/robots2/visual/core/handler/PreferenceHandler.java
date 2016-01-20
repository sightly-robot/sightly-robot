package de.unihannover.swp2015.robots2.visual.core.handler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import de.unihannover.swp2015.robots2.controller.externalInterfaces.IVisualization;
import de.unihannover.swp2015.robots2.visual.core.PrefKey;
import de.unihannover.swp2015.robots2.visual.util.pref.IPreferences;

/**
 * Parses messages from the GUI and updates global preference object with the received JSON strings.
 * 
 * @author Rico Schrage
 */
public class PreferenceHandler implements IVisualization {

	/** Logger (log4j) */
	private static final Logger log = LogManager.getLogger();
	
	/** First index of the keys sent by the GUI */
	private static final int GO_START_INDEX = 6;

	/** Name of the root element */
	private static final String ROOT_NAME = "options";
	
	/** Contains local preferences */
	private final IPreferences<PrefKey> pref;
	
	/** Id of the visualization */
	private final UUID id;
	
	/**
	 * Construct handler for the given preference object.
	 * 
	 * @param pref preference object to be handled
	 */
	public PreferenceHandler(IPreferences<PrefKey> pref, UUID id) {
		this.pref = pref;
		this.id = id;
	}

	@Override
	public void setSettings(String settings) {
		InputStream stream = new ByteArrayInputStream(settings.getBytes(StandardCharsets.UTF_8));
		JSONTokener tokenizer = new JSONTokener(stream);
		JSONObject options = new JSONObject(tokenizer).getJSONObject(ROOT_NAME); 
		
		if (options.getString("id") != id.toString())
			return; 
		
		PrefKey[] keys = PrefKey.values();
		for (int i = GO_START_INDEX; i < keys.length; ++i) {
			try {
				final PrefKey key = keys[i];
				final Object value = options.get(key.getKey());
				if (value instanceof Boolean) {
					pref.putBoolean(key, (boolean) value);
				}
				else if (value instanceof Double) {
					final Float floatValue = (float) value;
					pref.putFloat(key, Float.isNaN(floatValue) ? null : floatValue);
				}
			}
			catch (JSONException e) {
				log.trace(e);
				continue;
			}
		}
		System.out.println(options.toString());
	}

	@Override
	public String getSettings() {
		JSONObject options = new JSONObject();
		options.put("id", id.toString());
		PrefKey[] keys = PrefKey.values();
		for (int i = GO_START_INDEX; i < keys.length; ++i) {
			final PrefKey key = keys[i];
			final Object defaultValue = key.getDefault();
			if (key.getDefault() instanceof Boolean) {
				options.put(key.getKey(), pref.getBoolean(key));
			}
			else if (defaultValue instanceof Float) {
				options.put(key.getKey(), pref.getFloat(key));
			}
		}
		JSONObject root = new JSONObject();
		root.put("options", options);
		System.out.println(root.toString());
		return root.toString();
	}
	
}
