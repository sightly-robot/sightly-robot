package de.unihannover.swp2015.robots2.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * A VisualizationOptions class holds all necessary options for the visualization.
 * VisualizationOptions can be read from json and written to json. Only set members
 * are actually serialized and read from. 
 * In order to make this behaviour useful, two more methods are provided two merge 
 * VisualizationOptions classes to fill in missing or overwrite missing members. 
 * Useful for sending only what is necessary. 
 * 
 * @author Tim Ebbeke
 */
public class VisualizationOptions {
	private Optional<Float> abscissaOffset;
	private Optional<Float> ordinateOffset;
	private Optional<Float> abscissaScale;
	private Optional<Float> ordinateScale;
	
	private Optional<Float> perspectiveTransformation;
	
	private Optional<Boolean> renderWalls;
	private Optional<Boolean> renderResources;
	private Optional<Boolean> renderName;
	private Optional<Boolean> renderScore;
	private Optional<Boolean> renderRobots;
	private Optional<Boolean> renderVirtualRobots;
	
	/**
	 * Serializes all set variables to a json string.
	 * @return
	 */
	public String toJson() {		
		JSONObject options = new JSONObject();
		if (!this.abscissaOffset.isPresent())
			options.put("abcissaOffset", this.abscissaOffset.get());
		if (!this.ordinateOffset.isPresent())
			options.put("ordinateOffset", this.ordinateOffset.get());
		if (!this.abscissaScale.isPresent())
			options.put("abscissaOffset", this.abscissaScale.get());
		if (!this.ordinateScale.isPresent())
			options.put("ordinateScale", this.ordinateScale.get());
		
		if (!this.perspectiveTransformation.isPresent())
			options.put("perspectiveTransformation", this.perspectiveTransformation.get());
		
		if (!this.renderWalls.isPresent())
			options.put("renderWalls", this.renderWalls.get());
		if (!this.renderResources.isPresent())
			options.put("renderResources", this.renderResources.get());
		if (!this.renderName.isPresent())
			options.put("renderName", this.renderName.get());
		if (!this.renderScore.isPresent())
			options.put("renderScore", this.renderScore.get());
		if (!this.renderRobots.isPresent())
			options.put("renderRobots", this.renderRobots.get());
		if (!this.renderVirtualRobots.isPresent())
			options.put("renderVirtualRobots", this.renderVirtualRobots.get());

		JSONObject root = new JSONObject();
		root.put("options", options);
		
		return root.toString();
	}
	
	/**
	 * Reads a json string into this class, if the json is valid.
	 * Everything is considered optional.
	 * @param jsonData
	 */
	public void fromJson(String jsonData) {
		InputStream stream = new ByteArrayInputStream(jsonData.getBytes(StandardCharsets.UTF_8));
		JSONTokener tokenizer = new JSONTokener(stream);
		
		JSONObject options = new JSONObject(tokenizer).getJSONObject("options"); 
		
		// X, Y Offset & Scaling
		float xOff = (float) options.optDouble("abscissaOffset");
		float yOff = (float) options.optDouble("ordinateOffset");
		float xSca = (float) options.optDouble("abscissaScale");
		float ySca = (float) options.optDouble("ordinateScale");
		float pT = (float) options.optDouble("perspectiveTransformation");
		
		abscissaOffset = Optional.ofNullable(Float.isNaN(xOff) ? null : xOff);
		ordinateOffset = Optional.ofNullable(Float.isNaN(yOff) ? null : yOff);
		abscissaScale = Optional.ofNullable(Float.isNaN(xSca) ? null : xSca);
		ordinateScale = Optional.ofNullable(Float.isNaN(ySca) ? null : ySca);
		
		// Transformation
		perspectiveTransformation = Optional.ofNullable(Float.isNaN(pT) ? null : pT);
		
		// Boolean flags
		try { renderWalls = Optional.of(options.getBoolean("renderWalls")); } catch (JSONException exc) { renderWalls = Optional.empty(); }
		try { renderResources = Optional.of(options.getBoolean("renderResources")); } catch (JSONException exc) { renderResources = Optional.empty(); }
		try { renderName = Optional.of(options.getBoolean("renderName")); } catch (JSONException exc) { renderName = Optional.empty(); }
		try { renderScore = Optional.of(options.getBoolean("renderScore")); } catch (JSONException exc) { renderScore = Optional.empty(); }
		try { renderRobots = Optional.of(options.getBoolean("renderRobots")); } catch (JSONException exc) { renderRobots = Optional.empty(); }
		try { renderVirtualRobots = Optional.of(options.getBoolean("renderVirtualRobots")); } catch (JSONException exc) { renderVirtualRobots = Optional.empty(); }
	}
	
	/**
	 * Convenience method to convert json directly into VisualizationOptions. 
	 * @param jsonData A json string.
	 * @return A fresh visualization object.
	 * 
	 */
	public static VisualizationOptions createFromJson(String jsonData) {
		VisualizationOptions options = new VisualizationOptions();
		options.fromJson(jsonData);
		return options;
	}
	
	/**
	 * Creates a new VisualizationOptions class with default values.
	 * @return A new VisualizationOptions class with all things set.
	 */
	public static VisualizationOptions createDefault() {
		VisualizationOptions options = new VisualizationOptions();
		
		options.abscissaOffset = Optional.of(1.0f);
		options.ordinateOffset = Optional.of(1.0f);
		options.abscissaScale = Optional.of(1.0f);
		options.ordinateScale = Optional.of(1.0f);
		
		options.perspectiveTransformation = Optional.of(1.0f);
		
		options.renderWalls = Optional.of(true);
		options.renderResources = Optional.of(true);
		options.renderName = Optional.of(true);
		options.renderScore = Optional.of(true);
		options.renderRobots = Optional.of(false);
		options.renderVirtualRobots = Optional.of(true);
		
		return options;
	}
	
	/**
	 * Takes all values from parameter "options" that aren't specified in this instance.
	 * Does not overwrite any set variables of this instance.
	 * @param options Another VisualizationOptions object to take values from.
	 */
	public void leftMerge(VisualizationOptions options) {
		if (!this.abscissaOffset.isPresent())
			this.abscissaOffset = options.abscissaOffset;
		if (!this.ordinateOffset.isPresent())
			this.ordinateOffset = options.ordinateOffset;
		if (!this.abscissaScale.isPresent())
			this.abscissaOffset = options.abscissaScale;
		if (!this.ordinateScale.isPresent())
			this.ordinateScale = options.ordinateScale;
		
		if (!this.perspectiveTransformation.isPresent())
			this.perspectiveTransformation = options.perspectiveTransformation;
		
		if (!this.renderWalls.isPresent())
			this.renderWalls = options.renderWalls;
		if (!this.renderResources.isPresent())
			this.renderResources = options.renderResources;
		if (!this.renderName.isPresent())
			this.renderName = options.renderName;
		if (!this.renderScore.isPresent())
			this.renderScore = options.renderScore;
		if (!this.renderRobots.isPresent())
			this.renderRobots = options.renderRobots;
		if (!this.renderVirtualRobots.isPresent())
			this.renderVirtualRobots = options.renderVirtualRobots;
	}
	
	/**
	 * Overwrites all members, that are present in parameter options.
	 * @param options Another VisualizationOptions object to take values from.
	 */
	public void rightMerge(VisualizationOptions options) {
		if (options.abscissaOffset.isPresent())
			this.abscissaOffset = options.abscissaOffset;
		if (options.ordinateOffset.isPresent())
			this.ordinateOffset = options.ordinateOffset;
		if (options.abscissaScale.isPresent())
			this.abscissaOffset = options.abscissaScale;
		if (options.ordinateScale.isPresent())
			this.ordinateScale = options.ordinateScale;
		
		if (options.perspectiveTransformation.isPresent())
			this.perspectiveTransformation = options.perspectiveTransformation;
		
		if (options.renderWalls.isPresent())
			this.renderWalls = options.renderWalls;
		if (options.renderResources.isPresent())
			this.renderResources = options.renderResources;
		if (options.renderName.isPresent())
			this.renderName = options.renderName;
		if (options.renderScore.isPresent())
			this.renderScore = options.renderScore;
		if (options.renderRobots.isPresent())
			this.renderRobots = options.renderRobots;
		if (options.renderVirtualRobots.isPresent())
			this.renderVirtualRobots = options.renderVirtualRobots;
	}

	public Optional<Float> getAbscissaOffset() {
		return abscissaOffset;
	}

	public void setAbscissaOffset(float abscissaOffset) {
		this.abscissaOffset = Optional.<Float>of(abscissaOffset);
	}

	public Optional<Float> getOrdinateOffset() {
		return ordinateOffset;
	}

	public void setOrdinateOffset(float ordinateOffset) {
		this.ordinateOffset = Optional.<Float>of(ordinateOffset);
	}

	public Optional<Float> getAbscissaScale() {
		return abscissaScale;
	}

	public void setAbscissaScale(float abscissaScale) {
		this.abscissaScale = Optional.<Float>of(abscissaScale);
	}

	public Optional<Float> getOrdinateScale() {
		return ordinateScale;
	}

	public void setOrdinateScale(float ordinateScale) {
		this.ordinateScale = Optional.<Float>of(ordinateScale);
	}

	public Optional<Float> getPerspectiveTransformation() {
		return perspectiveTransformation;
	}

	public void setPerspectiveTransformation(float perspectiveTransformation) {
		this.perspectiveTransformation = Optional.<Float>of(perspectiveTransformation);
	}

	public Optional<Boolean> doesRenderWalls() {
		return renderWalls;
	}

	public void setRenderWalls(boolean renderWalls) {
		this.renderWalls = Optional.<Boolean>of(renderWalls);
	}

	public Optional<Boolean> doesRenderResources() {
		return renderResources;
	}

	public void setRenderResources(boolean renderResources) {
		this.renderResources = Optional.<Boolean>of(renderResources);
	}

	public Optional<Boolean> doesRenderName() {
		return renderName;
	}

	public void setRenderName(boolean renderName) {
		this.renderName = Optional.<Boolean>of(renderName);
	}

	public Optional<Boolean> doesRenderScore() {
		return renderScore;
	}

	public void setRenderScore(boolean renderScore) {
		this.renderScore = Optional.<Boolean>of(renderScore);
	}

	public Optional<Boolean> doesRenderRobots() {
		return renderRobots;
	}

	public void setRenderRobots(boolean renderRobots) {
		this.renderRobots = Optional.<Boolean>of(renderRobots);
	}

	public Optional<Boolean> doesRenderVirtualRobots() {
		return renderVirtualRobots;
	}

	public void setRenderVirtualRobots(boolean renderVirtualRobots) {
		this.renderVirtualRobots = Optional.<Boolean>of(renderVirtualRobots);
	}	
}
