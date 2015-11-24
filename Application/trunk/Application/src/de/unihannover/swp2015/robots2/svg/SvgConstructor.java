package de.unihannover.swp2015.robots2.svg;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
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

import de.unihannover.swp2015.robots2.CardinalDirection;
import de.unihannover.swp2015.robots2.Field;
import de.unihannover.swp2015.robots2.GameState;
import de.unihannover.swp2015.robots2.Position;
import de.unihannover.swp2015.robots2.Robot;
import de.unihannover.swp2015.robots2.util.ResourceLoader;

public class SvgConstructor {
	private GameState state;
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
	public SvgConstructor(GameState state) {
		this.universe = new SVGUniverse();
		this.state = state;
		resetSvg();
	}
	
	/**
	 * Loads an svg into the universe.
	 * 
	 * @param name The svg name in the resources.
	 */
	private URI loadSvgResource(String name) {
		InputStream stream = ResourceLoader.loadResourceAsInputStream("/de/unihannover/swp2015/robots2/" + name + ".svg");		
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
			double width = state.getMap().getWidth();
			double height = state.getMap().getHeight();		
			double fieldWidth = svgWidth / width;
			double fieldHeight = svgHeight / height;
			
			// Draw resources
			List<List<Field>> fields = state.getMap().getFields();
			for (int y = 0; y < fields.size(); y++) {
				List <Field> row = fields.get(y);
				for (int x = 0; x < row.size(); ++x) {
					Field field = row.get(x);
					
					Rect rect = new Rect();
					rect.addAttribute("x", AnimationElement.AT_XML, Double.toString(x * fieldWidth));
					rect.addAttribute("y", AnimationElement.AT_XML, Double.toString(y * fieldHeight));
					rect.addAttribute("width", AnimationElement.AT_XML, Double.toString(fieldWidth));
					rect.addAttribute("height", AnimationElement.AT_XML, Double.toString(fieldHeight));
					rect.addAttribute("fill", AnimationElement.AT_XML, (new colorStyleBuilder()).getColorStyle(1. - (double)field.getResources() / 10.));
					
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
		List<List<Field>> fields = state.getMap().getFields();
		
		double width = state.getMap().getWidth();
		double height = state.getMap().getHeight();		
		double fieldWidth = svgWidth / width;
		double fieldHeight = svgHeight / height;

		try {
			for (int y = 0; y < fields.size(); y++) {
				List <Field> row = fields.get(y);
				for (int x = 0; x < row.size(); ++x) {
					Field field = row.get(x);
					
					Set<CardinalDirection> fieldWalls = field.getWalls();
					
					for (CardinalDirection wall : fieldWalls) {
						double Ax = x * fieldWidth;
						double Ay = y * fieldHeight;
						double x1 = Ax + (wall == CardinalDirection.EAST ? fieldWidth : 0);
						double x2 = Ax + (wall == CardinalDirection.WEST ? 0 : fieldWidth);
						double y1 = Ay + (wall == CardinalDirection.SOUTH ? fieldHeight : 0);
						double y2 = Ay + (wall == CardinalDirection.NORTH ? 0 : fieldHeight);
						
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
		List <Robot> robots = state.getRobots();

		double width = state.getMap().getWidth();
		double height = state.getMap().getHeight();		
		double fieldWidth = svgWidth / width;
		double fieldHeight = svgHeight / height;
		
		try {			
			for (Robot r : robots) {
				if (r.isVirtual())
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
					TransformationStringBuilder.getObjectRotationTransformation(r.getPosition(), CardinalDirection.NORTH, fieldWidth, fieldHeight)
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
		List <Robot> robots = state.getRobots();

		double width = state.getMap().getWidth();
		double height = state.getMap().getHeight();		
		double fieldWidth = svgWidth / width;
		double fieldHeight = svgHeight / height;
		
		try {			
			for (Robot r : robots) {
				if (!r.isVirtual())
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
					TransformationStringBuilder.getObjectRotationTransformation(r.getPosition(), CardinalDirection.WEST, fieldWidth, fieldHeight)
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
		
		
		List <Position> startPositions = state.getMap().getStartPositions();
		
		double width = state.getMap().getWidth();
		double height = state.getMap().getHeight();		
		double fieldWidth = svgWidth / width;
		double fieldHeight = svgHeight / height;
		
		try {
			for (Position pos : startPositions) {
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
					TransformationStringBuilder.getObjectRotationTransformation(pos, CardinalDirection.EAST, fieldWidth, fieldHeight)
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
