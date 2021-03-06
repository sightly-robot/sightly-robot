package de.sightly_robot.sightly_robot.application.models;

import java.util.HashMap;
import java.util.Map;

import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.List;

import de.sightly_robot.sightly_robot.controller.main.GuiMainController;
import org.json.JSONException;

/**
 * Model holding all Visualizations for the control panel (visualization options).
 * @author Tim Ebbeke
 */
public class VisualizationRegister {
	private Map <String, VisualizationOptions> visualizations;
	private GuiMainController controller;
	private String selected;
	
	public VisualizationRegister(GuiMainController controller) {
		this.controller = controller;
		visualizations = new HashMap <>();
	}
	
	/**
	 * Adds another visualization to the map.
	 * Initializes all unset parameters with a default.
	 * @param id The visualization id.
	 * @param state The visualization options.
	 */
	public void addVisualization(String id, VisualizationOptions state) {
		state.leftMerge(VisualizationOptions.createDefault(id));
		visualizations.put(id, state);
	}
	
	/**
	 * Sets or adds another visualization to the map.
	 * @param id The visualization id.
	 * @param state The visualization options.
	 */
	public void setVisualization(String id, VisualizationOptions state) {
		visualizations.put(id, state);
	}
	
	/**
	 * Returns the visualization options
	 * @param id The id of the visualization client.
	 * @return The visualization options of the client.
	 */
	public VisualizationOptions getVisualization(String id) {
		return visualizations.get(id);
	}
	
	/**
	 * Sets the selected id of visualization.
	 * @param id Id of the "to be" selected visualization.
	 */
	public void setSelection(String id) {
		this.selected = id;
	}
	
	/**
	 * Sets the value of the selected visualization.  
	 * @param value The visualization options to set (warning: this overwrites all).
	 */
	public void setSelected(VisualizationOptions value) {
		setVisualization(selected, value);
	}
	
	/**
	 * Returns the visualization options of the selected visualization.
	 * @return Returns the visualization options of the selected visualization. 
	 */
	public VisualizationOptions getSelected() {
		return getVisualization(selected);
	}
	
	/**
	 * Get an id list from the map for the view.
	 * @return List of visualization ids.
	 */
	public List <String> getIdList() {
		// must iterate by myself, because of conversion from java.util.Map to apache list.
		List <String> list = new ArrayList <String>();
		for (String id : visualizations.keySet()) {
			list.add(id);
		}
		return list;
	}
	
	/**
	 * Clears all visualizations from the list.
	 */
	public void clear() {
		visualizations.clear();
	}
	
	/**
	 * 
	 */
	public void cycleTexturepackForSelected() {
		VisualizationOptions cycler = new VisualizationOptions(getSelected().getId());
		cycler.setCycleTexturePack("next");
		try {
			controller.setVisualizationSettings(cycler.toJson());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Takes the partial option definition and merges it into the complete options state
	 * working on the selected visualization.
	 * @param options A partial options change that is to be inserted into the local state storage.
	 */
	public void performMergeOnSelected(VisualizationOptions options) {
		VisualizationOptions temp = getSelected();
		temp.rightMerge(options);
		setVisualization(selected, temp);
		try {
			System.out.println(options.toJson());
			controller.setVisualizationSettings(options.toJson());
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
