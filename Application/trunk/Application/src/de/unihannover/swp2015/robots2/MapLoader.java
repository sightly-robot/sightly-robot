package de.unihannover.swp2015.robots2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import de.unihannover.swp2015.robots2.controller.main.GuiMainController;
import de.unihannover.swp2015.robots2.exceptions.InvalidMapFile;
import de.unihannover.swp2015.robots2.model.implementation.Position;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

public class MapLoader {
	private GuiMainController controller;
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
	public MapLoader(GuiMainController controller, String fileName) throws JSONException, FileNotFoundException, InvalidMapFile
	{		
		this.controller = controller;
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
	 * @throws InvalidMapFile Thrown if the map data is inconsistent or nonsensical
	 */
	public void loadFromFile(String fileName) throws JSONException, FileNotFoundException, InvalidMapFile
	{		
		JSONTokener tokenizer = new JSONTokener(new FileInputStream(fileName));
		JSONObject mapObject = new JSONObject(tokenizer).getJSONObject("map"); 
		
		// int version = mapObject.getInt("version");
		
		// load game parameters, which are map dependent.
		this.gameParameters = new GameParameters (
			mapObject.getJSONObject("gameParameters").getDouble("movementSpeed"),
			mapObject.getJSONObject("gameParameters").getInt("maxStayTime")
		);
		
		List<List<Integer>> food = new ArrayList<>();
		List<List<Set<Orientation>>> walls = new ArrayList<>();
		List<List<Integer>> growingRates = new ArrayList<>();
		List<IPosition> startPositions = new ArrayList<>();
			
		// load fields from json:
		JSONArray fieldsArray = mapObject.getJSONArray("fields");
		for (int y = 0; y < fieldsArray.length(); ++y) {
			JSONArray row = fieldsArray.getJSONArray(y);
			
			// consistency check
			if (row.length() != mapObject.getInt("width") || row.length() == 0)
				throw new InvalidMapFile("The field width is inconsistent with the array, or zero");
			
			food.add(new ArrayList <Integer>());
			walls.add(new ArrayList<Set<Orientation>>());
			growingRates.add(new ArrayList<Integer>());
			
			for (int x = 0; x < row.length(); ++x) {
				JSONObject fieldObject = row.getJSONObject(x); // gef field object 
				
				if (fieldObject.has("growthRate"))
					growingRates.get(growingRates.size() - 1).add(new Integer(fieldObject.getInt("growthRate")));
				if (fieldObject.has("initialResources"))
					food.get(food.size() - 1).add(new Integer(fieldObject.getInt("initialResources")));
				if (fieldObject.has("possibleDirections")) {
					String str = fieldObject.getString("possibleDirections");
					String w = "nesw";
					Set<Orientation> fieldWalls = new HashSet<>();
					// working around the interface and stay with map format
					for (int c = 0, n = w.length(); c < n; ++c)
						fieldWalls.add(Orientation.getBy(w.charAt(c)));

					for (int c = 0, n = str.length(); c < n; ++c)
						fieldWalls.remove(Orientation.getBy(str.charAt(c)));
					walls.get(walls.size() - 1).add(fieldWalls);
				}
				if (fieldObject.has("startPosition")) {
					String dir = fieldObject.getString("startPosition");
					if (!dir.isEmpty()) {
						startPositions.add(new Position(x, y, Orientation.getBy(dir)));
					}
				}
			}
		}
		
		if (fieldsArray.length() != mapObject.getInt("height") || fieldsArray.length() == 0)
			throw new InvalidMapFile("The field height is inconsitent with the array, or zero");
		
		walls = fixBorders(walls);
		
		controller.sendGameParameters((float)gameParameters.getMovementSpeed(), gameParameters.getMaxStayTime());
		controller.sendFood(food);
		controller.sendWalls(walls);
		controller.sendGrowingRates(growingRates);
	}
	
	/**
	 * Creates walls around the map borders. Even though a robot would never go beyond,
	 * a fix up is safer.
	 */
	public List<List<Set<Orientation>>> fixBorders(List<List<Set<Orientation>>> walls)
	{
		if (!walls.isEmpty()) {
			// fix north
			List<Set<Orientation>> list = walls.get(0);
			for (int i = 0; i < list.size(); i++)
				list.get(i).add(Orientation.NORTH);
			
			// fix south
			list = walls.get(walls.size() - 1);
			for (int i = 0; i < list.size(); ++i) 
				list.get(i).add(Orientation.SOUTH);
		}			
		
		for (int i = 0; i < walls.size(); ++i) {
			List <Set<Orientation>> list = walls.get(i);

			if (!list.isEmpty()) { // map has width
				// fix west
				list.get(0).add(Orientation.WEST);
				
				// fix east
				list.get(list.size() -  1).add(Orientation.EAST);
			}
		}
		
		return walls;
	}	
}
