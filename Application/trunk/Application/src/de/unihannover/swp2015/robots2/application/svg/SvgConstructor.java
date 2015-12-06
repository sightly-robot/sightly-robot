package de.unihannover.swp2015.robots2.application.svg;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kitfox.svg.Group;
import com.kitfox.svg.ImageSVG;
import com.kitfox.svg.Line;
import com.kitfox.svg.Path;
import com.kitfox.svg.Rect;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGUniverse;
import com.kitfox.svg.animation.AnimationElement;

import de.unihannover.swp2015.robots2.application.util.ResourceLoader;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;

public class SvgConstructor {
	private IGame game;
	private SVGDiagram diagram;
	private SVGUniverse universe;
	
	private URI templateSvg; 
	
	private int svgWidth = 1000;
	private int svgHeight = 1000;
	
	/**
	 * Creates a SvgConstructor. Nothing fancy here
	 * @param state The game state class.
	 * @param diagram A diagram to put the things into.
	 */
	public SvgConstructor(IGame game) {
		this.universe = new SVGUniverse();
		this.game = game;
		resetSvg();
	}
	
	/**
	 * Loads an svg into the universe.
	 * 
	 * @param name The svg name in the resources.
	 */
	private URI loadSvgResource(String name) {
		InputStream stream = ResourceLoader.loadResourceAsInputStream("/de/unihannover/swp2015/robots2/application/" + name + ".svg");		
		Reader rd = new InputStreamReader(stream); 
		return universe.loadSVG(rd, "/" + name);
	}
	
	/**
	 * Generates the svg base
	 */
	public void resetSvg() {				 
		
		// create the svg stub
		universe.clear();
		templateSvg = loadSvgResource("Template");
		
		this.diagram = universe.getDiagram(templateSvg);
	}
	
	/**
	 * Draws resources to svg. Before calling it again, you must call reset svg.
	 */
	public void drawResources() {		
		final class colorStyleBuilder {
			public String getColorStyle(double percentage) {
				StringBuilder builder = new StringBuilder();
				
				// construct string
				builder.append("rgb(").append((int)(percentage * 255)).append(",").append(255)
					   .append(",").append((int)(percentage * 255)).append(")");
				return builder.toString();
			}			
		}
		
		try {
			double width = game.getStage().getWidth();
			double height = game.getStage().getHeight();		
			double fieldWidth = svgWidth / width;
			double fieldHeight = svgHeight / height;
			
			// Draw resources
			for (int y = 0; y < game.getStage().getHeight(); y++) {
				for (int x = 0; x < game.getStage().getWidth(); ++x) {
					IField field = game.getStage().getField(x, y);
					
					Rect rect = new Rect();
					rect.addAttribute("x", AnimationElement.AT_XML, Double.toString(x * fieldWidth));
					rect.addAttribute("y", AnimationElement.AT_XML, Double.toString(y * fieldHeight));
					rect.addAttribute("width", AnimationElement.AT_XML, Double.toString(fieldWidth));
					rect.addAttribute("height", AnimationElement.AT_XML, Double.toString(fieldHeight));
					rect.addAttribute("fill", AnimationElement.AT_XML, (new colorStyleBuilder()).getColorStyle(1. - (double)field.getFood() / 10.));
					
					Group resources = (Group)diagram.getElement("resources");
					resources.loaderAddChild(null, rect);
				}
			}

			//resources.updateTime(0.);
			diagram.updateTime(0.);
		} catch (SVGException e) {
			// ignore
			e.printStackTrace();
		}
	}
	
	/**
	 * Draws all walls onto the svg
	 */
	public void drawWalls() {
		double fieldWidth = svgWidth / game.getStage().getWidth();
		double fieldHeight = svgHeight / game.getStage().getHeight();

		try {
			for (int y = 0; y < game.getStage().getHeight(); y++) {
				for (int x = 0; x < game.getStage().getWidth(); ++x) {
					IField field = game.getStage().getField(x, y);
					
					Set<Orientation> fieldWalls = field.getWalls();
					
					for (Orientation wall : fieldWalls) {
						double Ax = x * fieldWidth;
						double Ay = y * fieldHeight;
						double x1 = Ax + (wall == Orientation.EAST ? fieldWidth : 0);
						double x2 = Ax + (wall == Orientation.WEST ? 0 : fieldWidth);
						double y1 = Ay + (wall == Orientation.SOUTH ? fieldHeight : 0);
						double y2 = Ay + (wall == Orientation.NORTH ? 0 : fieldHeight);
						
						Line line = new Line();
						line.addAttribute("x1", AnimationElement.AT_XML, Double.toString(x1));
						line.addAttribute("x2", AnimationElement.AT_XML, Double.toString(x2));
						line.addAttribute("y1", AnimationElement.AT_XML, Double.toString(y1));
						line.addAttribute("y2", AnimationElement.AT_XML, Double.toString(y2));
						line.addAttribute("stroke", AnimationElement.AT_XML, "rgb(0,0,0)");
						line.addAttribute("stroke-width", AnimationElement.AT_XML, Integer.toString(svgWidth / 200));
						line.addAttribute("stroke-linecap", AnimationElement.AT_XML, "round");
						
						Group walls = (Group)diagram.getElement("walls");
						walls.loaderAddChild(null, line);
					}				
				}
			}
			
			diagram.updateTime(0.);
		} catch (SVGException e) {
			// ignore
			e.printStackTrace();
		}
	}
	
	/**
	 * Draws all real robots onto the svg
	 */
	public void drawRobots() {
		Map<String, ? extends IRobot> robots = game.getRobots();

		double fieldWidth = svgWidth / game.getStage().getWidth();
		double fieldHeight = svgHeight / game.getStage().getHeight();
		
		try {			
			for (IRobot r : robots.values()) {
				if (!r.isHardwareRobot())
					continue; 
				
				ImageSVG robot = new ImageSVG();			
				
				robot.addAttribute("width", AnimationElement.AT_XML, "100");
				robot.addAttribute("height", AnimationElement.AT_XML, "100");
				
				try {
					robot.addAttribute(
						"xlink:href", 
						AnimationElement.AT_XML, 
						SvgConstructor.class.getResource("/de/unihannover/swp2015/robots2/Roboter.svg").toURI().toString()
					);
				} catch (URISyntaxException e) {}
				
				robot.addAttribute("transform", AnimationElement.AT_XML, 
					TransformationStringBuilder.getObjectRotationTransformation(r.getPosition(), Orientation.NORTH, fieldWidth, fieldHeight)
				);
				
				Group robos = (Group)diagram.getElement("robots");
				robos.loaderAddChild(null, robot);
			}
			
			diagram.updateTime(0.);
		} catch (SVGException e) {
			// ignore
			e.printStackTrace();
		}
	}
	
	/**
	 * Draws all virtual robots onto the svg
	 */
	public void drawVirtualRobots() {		
		Map<String, ? extends IRobot> robots = game.getRobots();

		double fieldWidth = svgWidth / game.getStage().getWidth();
		double fieldHeight = svgHeight / game.getStage().getHeight();
		
		try {			
			for (IRobot r : robots.values()) {
				if (r.isHardwareRobot())
					continue; 
				
				ImageSVG robot = new ImageSVG();			
				
				robot.addAttribute("width", AnimationElement.AT_XML, "100");
				robot.addAttribute("height", AnimationElement.AT_XML, "100");
				
				try {
					robot.addAttribute(
						"xlink:href", 
						AnimationElement.AT_XML, 
						SvgConstructor.class.getResource("/de/unihannover/swp2015/robots2/Virtual.svg").toURI().toString()
					);
				} catch (URISyntaxException e) {}
				
				robot.addAttribute("transform", AnimationElement.AT_XML, 
					TransformationStringBuilder.getObjectRotationTransformation(r.getPosition(), Orientation.WEST, fieldWidth, fieldHeight)
				);
				
				Group robos = (Group)diagram.getElement("virtualRobots");
				robos.loaderAddChild(null, robot);
			}
			
			diagram.updateTime(0.);
		} catch (SVGException e) {
			// ignore
			e.printStackTrace();
		}
	}
	
	/**
	 * Draws all start positions as arrows into the svg.
	 */
	public void drawStartPositions() {
		List <IPosition> startPositions = game.getStage().getStartPositions();

		double fieldWidth = svgWidth / game.getStage().getWidth();
		double fieldHeight = svgHeight / game.getStage().getHeight();
		
		try {
			for (IPosition pos : startPositions) {
				Path path = new Path();
				try {
					path.addAttribute("d", AnimationElement.AT_XML, ResourceLoader.loadResourceAsString("/de/unihannover/swp2015/robots2/Arrow.txt"));
				} catch (IOException | URISyntaxException e) {
					// ignore - cannot happen
					e.printStackTrace();
				}
				path.addAttribute("cx", AnimationElement.AT_XML, Double.toString(pos.getX() * fieldWidth));
				path.addAttribute("cy", AnimationElement.AT_XML, Double.toString(pos.getY() * fieldHeight));
				path.addAttribute("stroke-width", AnimationElement.AT_XML, "6");
				path.addAttribute("stroke", AnimationElement.AT_XML, "#000000");
				path.addAttribute("stroke-linejoin", AnimationElement.AT_XML, "round");
				path.addAttribute("fill", AnimationElement.AT_XML, "#FF0000");
				path.addAttribute("transform", AnimationElement.AT_XML, 
					TransformationStringBuilder.getObjectRotationTransformation(pos, Orientation.EAST, fieldWidth, fieldHeight)
				);
				
				Group positions = (Group)diagram.getElement("startPositions");
				positions.loaderAddChild(null, path);
			}
			
			diagram.updateTime(0.);
		} catch (SVGException e) {
			// ignore
			e.printStackTrace();
		}
	}	
	
	/**
	 * Gets the svg diagram
	 * @return Returns the svg diagram.
	 */
	public SVGDiagram getDiagram() {
		return this.universe.getDiagram(templateSvg);
	}
}
