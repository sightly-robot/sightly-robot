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
	 * @throws InvalidMapFile Thrown if the map data is inconsistent or nonsensical
	 */
	public GameMap(String fileName) throws JSONException, FileNotFoundException, InvalidMapFile
	{		
		loadFromFile(fileName);
	}
	
	/**
	 * Does not initialize the map. Must load a map before use.
	 */
	public GameMap()
	{
		
	}
	
	/**
	 * Reads a json file that contains a "map" object.
	 * Does automatic validation checks and throws on failure.
	 * 
	 * @param fileName A json file to read the map from 
	 * @return 
	 * @throws FileNotFoundException Thrown when fileName could not be found.
	 * @throws JSONException Thrown if the json is not valid.
	 * @throws InvalidMapFile Thrown if the map data is inconsistent or nonsensical
	 */
	public void loadFromFile(String fileName) throws JSONException, FileNotFoundException, InvalidMapFile
	{
		resetMap();
		
		JSONTokener tokenizer = new JSONTokener(new FileInputStream(fileName));
		JSONObject mapObject = new JSONObject(tokenizer).getJSONObject("map"); 
		
		// int version = mapObject.getInt("version");
		
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
				if (fieldObject.has("initialResources"))
					field.setResources(fieldObject.getInt("initialResources"));
				if (fieldObject.has("possibleDirections"))
					field.setPassableDirections(fieldObject.getString("possibleDirections"));
				if (fieldObject.has("startPosition")) {
					String dir = fieldObject.getString("startPosition");
					if (!dir.isEmpty())
						field.setStartDirection(dir.charAt(0));
				}
				
				fields.get(fields.size() - 1).add(field); // insert field into list.
			}
		}
		
		if (fields.size() != mapObject.getInt("height") || fields.isEmpty())
			throw new InvalidMapFile("The field height is inconsitent with the array, or zero");
		
		if (fields.get(0).size() != mapObject.getInt("width") || fields.get(0).isEmpty())
			throw new InvalidMapFile("The field width is inconsistent with the array, or zero");
		
		fixBorders();
	}
	
	/**
	 * Creates walls around the map borders. Even though a robot would never go beyond,
	 * a fix up is safer.
	 */
	public void fixBorders()
	{
		// fields cannot be null
		
		// map has height
		if (!fields.isEmpty()) {
			// fix north
			List<Field> list = fields.get(0);
			for (int i = 0; i < list.size(); i++)
				list.get(i).removePassableDirection(CardinalDirection.NORTH);
			
			// fix south
			list = fields.get(fields.size() - 1);
			for (int i = 0; i < list.size(); ++i) 
				list.get(i).removePassableDirection(CardinalDirection.SOUTH);
		}			
		
		for (int i = 0; i < fields.size(); ++i) {
			List <Field> list = fields.get(i);

			if (!list.isEmpty()) { // map has width
				// fix west
				list.get(0).removePassableDirection(CardinalDirection.WEST);
				
				// fix east
				list.get(list.size() - 1).removePassableDirection(CardinalDirection.EAST);
			}
		}
	}
	
	public List <Position> getStartPositions() {
		List <Position> positions = new ArrayList <>();
		
		for (int y = 0; y < fields.size(); ++y) {
			List <Field> row = fields.get(y);
			for (int x = 0; x < row.size(); ++x) {
				Field field = row.get(x);
				
				if (field.isStartPosition()) {
					field.getStartPosition();
					positions.add(new Position(x, y, field.getStartPosition()));
				}
			}
		}
		
		return positions;
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

	public List<List<Field>> getFields() {
		return fields;
	}

	public GameParameters getGameParameters() {
		return gameParameters;
	}

	public void setGameParameters(GameParameters gameParameters) {
		this.gameParameters = gameParameters;
	}	
}
