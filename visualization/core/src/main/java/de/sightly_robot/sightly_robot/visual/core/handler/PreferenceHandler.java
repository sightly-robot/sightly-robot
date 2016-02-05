package de.sightly_robot.sightly_robot.visual.core.handler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.badlogic.gdx.Gdx;

import de.sightly_robot.sightly_robot.controller.externalInterfaces.IVisualization;
import de.sightly_robot.sightly_robot.visual.core.PrefKey;
import de.sightly_robot.sightly_robot.visual.util.pref.IPreferences;

/**
 * Parses messages from the GUI and updates global preference object with the
 * received JSON strings.
 * 
 * @author Rico Schrage
 */
public class PreferenceHandler implements IVisualization {

	/** logger (log4j) */
	private static final Logger LOGGER = LogManager.getLogger();

	/** first index of the keys sent by the GUI */
	private static final int GO_START_INDEX = 6;

	/** name of the root element */
	private static final String ROOT_NAME = "options";

	/** contains local preferences */
	private final IPreferences<PrefKey> pref;

	/** ID of the visualization */
	private final UUID id;

	/**
	 * Constructs handler for the given preference object.
	 * 
	 * @param pref
	 *            preference object to be handled
	 * @param id
	 *            ID of the visualization
	 */
	public PreferenceHandler(IPreferences<PrefKey> pref, UUID id) {
		this.pref = pref;
		this.id = id;
	}

	@Override
	public void setSettings(final String settings) {
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				try {
					setManagedSettings(settings);
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Call will be initiated when {@link #setSettings(String)} will be called
	 * by the protocol.
	 * 
	 * In difference to {@link #setSettings(String)} the method will be called
	 * in the render thread of libGDX.
	 * 
	 * @param settings
	 *            settings as JSON
	 */
	protected void setManagedSettings(String settings) throws JSONException {
		InputStream stream = new ByteArrayInputStream(
				settings.getBytes(StandardCharsets.UTF_8));
		JSONTokener tokenizer = new JSONTokener(stream);
		JSONObject options = new JSONObject(tokenizer).getJSONObject(ROOT_NAME);

		if (!options.has("id")
				|| !options.getString("id").equals(id.toString())) {
			return;
		}

		PrefKey[] keys = PrefKey.values();
		for (int i = GO_START_INDEX; i < keys.length; ++i) {
			try {
				final PrefKey key = keys[i];
				final Object value = options.get(key.getKey());
				if (value instanceof Boolean) {
					pref.putBoolean(key, (boolean) value);
				} else if (value instanceof Double) {
					final double doubleValue = (double) value;
					pref.putFloat(key, Double.isNaN(doubleValue) ? 0f
							: (float) doubleValue);
				} else if (value instanceof Integer) {
					final int intValue = (int) value;
					pref.putFloat(key, intValue);
				} else if (value instanceof String) {
					final String stringValue = (String) value;
					pref.putString(key, stringValue);
				}
				LOGGER.debug("Received option: {}, value: {}", key.getKey(),
						value);
			} catch (JSONException e) {
				LOGGER.trace(e);
			}
		}
	}

	@Override
	public String getSettings() {
        try {
            JSONObject options = new JSONObject();
            options.put("id", id.toString());

            PrefKey[] keys = PrefKey.values();
            for (int i = GO_START_INDEX; i < keys.length; ++i) {
                final PrefKey key = keys[i];
                final Object defaultValue = key.getDefault();
                if (key.getDefault() instanceof Boolean) {
                    boolean value = pref.getBoolean(key);
                    options.put(key.getKey(), value);
                    LOGGER.debug("Send option: {}, value: {}", key.getKey(), value);
                } else if (defaultValue instanceof Float) {
                    float value = pref.getFloat(key);
                    options.put(key.getKey(), value);
                    LOGGER.debug("Send option: {}, value: {}", key.getKey(), value);
                }
            }
            JSONObject root = new JSONObject();
            root.put("options", options);
            return root.toString();
        }
        catch (JSONException e) {
            LOGGER.trace(e);
        }
        return "{}";
    }

}
