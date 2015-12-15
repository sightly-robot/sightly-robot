package de.unihannover.swp2015.robots2.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class VisualizationOptions {
	private Optional<Double> abscissaOffset;
	private Optional<Double> ordinateOffset;
	private Optional<Double> abscissaScale;
	private Optional<Double> ordinateScale;
	
	// FIXME: how should this be represented?
	private Optional<Double> perspectiveTransformation;
	
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
		double xOff = options.optDouble("abscissaOffset");
		double yOff = options.optDouble("ordinateOffset");
		double xSca = options.optDouble("abscissaScale");
		double ySca = options.optDouble("ordinateScale");
		double pT = options.optDouble("perspectiveTransformation");
		
		abscissaOffset = Optional.ofNullable(Double.isNaN(xOff) ? null : xOff);
		ordinateOffset = Optional.ofNullable(Double.isNaN(yOff) ? null : yOff);
		abscissaScale = Optional.ofNullable(Double.isNaN(xSca) ? null : xSca);
		ordinateScale = Optional.ofNullable(Double.isNaN(ySca) ? null : ySca);
		
		// Transformation
		perspectiveTransformation = Optional.ofNullable(Double.isNaN(pT) ? null : pT);
		
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

	public Optional<Double> getAbscissaOffset() {
		return abscissaOffset;
	}

	public void setAbscissaOffset(double abscissaOffset) {
		this.abscissaOffset = Optional.<Double>of(abscissaOffset);
	}

	public Optional<Double> getOrdinateOffset() {
		return ordinateOffset;
	}

	public void setOrdinateOffset(double ordinateOffset) {
		this.ordinateOffset = Optional.<Double>of(ordinateOffset);
	}

	public Optional<Double> getAbscissaScale() {
		return abscissaScale;
	}

	public void setAbscissaScale(double abscissaScale) {
		this.abscissaScale = Optional.<Double>of(abscissaScale);
	}

	public Optional<Double> getOrdinateScale() {
		return ordinateScale;
	}

	public void setOrdinateScale(double ordinateScale) {
		this.ordinateScale = Optional.<Double>of(ordinateScale);
	}

	public Optional<Double> getPerspectiveTransformation() {
		return perspectiveTransformation;
	}

	public void setPerspectiveTransformation(double perspectiveTransformation) {
		this.perspectiveTransformation = Optional.<Double>of(perspectiveTransformation);
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
