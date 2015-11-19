package de.unihannover.swp2015.robots2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class MapReader {
	/**
	 * Reads a map from a file that is in json format.
	 * 
	 * @param fileName the file path to read from.
	 * @throws FileNotFoundException When file was not found
	 * @throws JSONException When json is not valid json
	 */
	public static GameMap readFromFile(String fileName) throws JSONException, FileNotFoundException
	{		
		JSONTokener tokenizer = new JSONTokener(new FileInputStream(fileName));
		JSONObject mapObject = new JSONObject(tokenizer).getJSONObject("map"); 
		
		Integer version = mapObject.getInt("version");
		System.out.print(version);
		
		return null;		
	}
}
