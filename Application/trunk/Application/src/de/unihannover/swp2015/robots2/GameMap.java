package de.unihannover.swp2015.robots2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class GameMap {
	private List<List<Field>> fields;
	private GameParameters gameParameters;
	
	/**
	 * Constructs the class with an initialized internal state.
	 * Can fail!
	 * 
	 * @param fileName A json file to read the map from 
	 * @throws FileNotFoundException see loadFromFile
	 * @throws JSONException see loadFromFile
	 */
	public GameMap(String fileName) throws JSONException, FileNotFoundException
	{		
		loadFromFile(fileName);
	}
	
	/**
	 * Reads a json file that contains a "map" object.
	 * Does automatic validation checks and throws on failure.
	 * 
	 * @param fileName A json file to read the map from 
	 * @return 
	 * @throws FileNotFoundException Thrown when fileName could not be found.
	 * @throws JSONException Thrown if the json is not valid.
	 */
	public void loadFromFile(String fileName) throws JSONException, FileNotFoundException
	{
		resetMap();
		
		JSONTokener tokenizer = new JSONTokener(new FileInputStream(fileName));
		JSONObject mapObject = new JSONObject(tokenizer).getJSONObject("map"); 
		
		int version = mapObject.getInt("version");
		System.out.print(version);
		
		// load game parameters, which are map dependent.
		this.gameParameters = new GameParameters (
			mapObject.getJSONObject("gameParameters").getDouble("movementSpeed"),
			mapObject.getJSONObject("gameParameters").getInt("maxStayTime")
		);	
		
		fields = new ArrayList<List <Field>>();
			
		// load fields from json:
		JSONArray fieldsArray = mapObject.getJSONArray("fields");
		for (int i = 0; i < fieldsArray.length(); ++i) {
			JSONArray row = fieldsArray.getJSONArray(i);
			fields.add(new ArrayList <Field>()); // create new row
			for (int j = 0; j < row.length(); ++j) {
				JSONObject fieldObject = row.getJSONObject(j); // gef field object
				
				Field field = new Field(); 
				
				if (fieldObject.has("growthRate"))
					field.setGrowthRate(fieldObject.getDouble("growthRate"));
				else if (fieldObject.has("initialResources"))
					field.setResources(fieldObject.getInt("initialResources"));
				else if (fieldObject.has("possibleDirections"))
					field.setPassableDirections(fieldObject.getString("possibleDirections"));
				else if (fieldObject.has("startPosition")) {
					String dir = fieldObject.getString("startPosition");
					if (!dir.isEmpty())
						field.setStartDirection(dir.charAt(0));
				}
				
				fields.get(fields.size() - 1).add(field); // insert field into list.
			}
		}
	}
	
	/**
	 * Resets all internal state. The map is now empty.
	 */
	public void resetMap()
	{
		if (fields != null)
			fields.clear();
	}
	
	/**
	 * Get the map width.
	 * @return Returns the map width.
	 */
	public int getWidth()
	{
		// there can not be any width if there is no height
		if (fields.isEmpty())
			return 0;	
		
		// every element has the same width, so returning the width of the first is sufficient.
		return fields.get(0).size();
	}
	
	/**
	 * Get the height of the map.
	 * @return Returns the height of the map.
	 */
	public int getHeight()
	{
		return fields.size();
	}

	public GameParameters getGameParameters() {
		return gameParameters;
	}

	public void setGameParameters(GameParameters gameParameters) {
		this.gameParameters = gameParameters;
	}	
}
