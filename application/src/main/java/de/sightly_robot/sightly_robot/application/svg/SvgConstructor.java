package de.sightly_robot.sightly_robot.application.svg;

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

import de.sightly_robot.sightly_robot.application.util.ResourceLoader;
import de.sightly_robot.sightly_robot.model.implementation.Position;
import de.sightly_robot.sightly_robot.model.interfaces.IField;
import de.sightly_robot.sightly_robot.model.interfaces.IGame;
import de.sightly_robot.sightly_robot.model.interfaces.IPosition;
import de.sightly_robot.sightly_robot.model.interfaces.IRobot;
import de.sightly_robot.sightly_robot.model.interfaces.IField.State;
import de.sightly_robot.sightly_robot.model.interfaces.IPosition.Orientation;

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
		InputStream stream = ResourceLoader.loadResourceAsInputStream("/de/unihannover/swp2015/robots2/application/svg/" + name + ".svg");		
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
		
		if (game.getStage().getWidth() == 0 || game.getStage().getHeight() == 0)
			return;
		
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
		if (game.getStage().getWidth() == 0 || game.getStage().getHeight() == 0)
			return;
		
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
	 * @param selectedId The selected robot in the UI. this one is displayed differently.
	 */
	public void drawRobots(String selectedId) {
		if (game.getStage().getWidth() == 0 || game.getStage().getHeight() == 0)
			return;
		
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
					String resource;
					if (r.getId().equals(selectedId)) {
						resource = "/de/unihannover/swp2015/robots2/application/svg/SelectedLawnmower.svg";
					} else {
						resource = "/de/unihannover/swp2015/robots2/application/svg/Lawnmower.svg";
					}
					robot.addAttribute(
						"xlink:href", 
						AnimationElement.AT_XML, 
						SvgConstructor.class.getResource(resource).toURI().toString()
					);
				} catch (URISyntaxException e) {}
				
				robot.addAttribute("transform", AnimationElement.AT_XML, 
					TransformationStringBuilder.getObjectTransformation(r.getPosition(), Orientation.NORTH, fieldWidth, fieldHeight)
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
	 * Draws colored crosses where robots have locked fields.
	 */
	public void drawLockedFields() {
		if (game.getStage().getWidth() == 0 || game.getStage().getHeight() == 0)
			return;

		double fieldWidth = svgWidth / game.getStage().getWidth();
		double fieldHeight = svgHeight / game.getStage().getHeight();
		
		try {			
			for (int y = 0; y < game.getStage().getHeight(); y++) {
				for (int x = 0; x < game.getStage().getWidth(); ++x) {
					IField field = game.getStage().getField(x, y);
					
					if (field.getState() == State.LOCKED ||
						field.getState() == State.LOCK_WAIT ||
						field.getState() == State.RANDOM_WAIT ||
						field.getState() == State.OCCUPIED) 
					{	

						ImageSVG robot = new ImageSVG();			
						
						robot.addAttribute("width", AnimationElement.AT_XML, "100");
						robot.addAttribute("height", AnimationElement.AT_XML, "100");
						
						try {
							String resource;
							if (field.getState() == State.LOCKED)
								resource = "/de/unihannover/swp2015/robots2/application/svg/Lock.svg";
							else if (field.getState() == State.LOCK_WAIT) 
								resource = "/de/unihannover/swp2015/robots2/application/svg/Wait.svg";
							else if (field.getState() == State.OCCUPIED)
								resource = "/de/unihannover/swp2015/robots2/application/svg/Occupied.svg";
							else 
								resource = "/de/unihannover/swp2015/robots2/application/svg/RandomWait.svg";
							robot.addAttribute(
								"xlink:href", 
								AnimationElement.AT_XML, 
								SvgConstructor.class.getResource(resource).toURI().toString()
							);
						} catch (URISyntaxException e) {}
						
						robot.addAttribute("transform", AnimationElement.AT_XML, 
							TransformationStringBuilder.getObjectTransformation(new Position(x, y, Orientation.NORTH), Orientation.NORTH, fieldWidth, fieldHeight)
						);
						
						Group locks = (Group)diagram.getElement("locks");
						locks.loaderAddChild(null, robot);				
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
	 * Draws a grid to counteract the random lines between fields... which are ugly.
	 */
	public void drawGrid() {
		if (game.getStage().getWidth() == 0 || game.getStage().getHeight() == 0)
			return;
		
		double fieldWidth = svgWidth / game.getStage().getWidth();
		double fieldHeight = svgHeight / game.getStage().getHeight();
		
		try {
			for (int y = 0; y < game.getStage().getHeight() - 1; ++y) {
				Line line = new Line();
				
				line.addAttribute("x1", AnimationElement.AT_XML, "0");
				line.addAttribute("y1", AnimationElement.AT_XML, Double.toString((y+1) * fieldHeight));
				line.addAttribute("x2", AnimationElement.AT_XML, Integer.toString(svgWidth));
				line.addAttribute("y2", AnimationElement.AT_XML, Double.toString((y+1) * fieldHeight));
				line.addAttribute("stroke", AnimationElement.AT_XML, "rgb(240,240,240)");
				line.addAttribute("stroke-width", AnimationElement.AT_XML, Integer.toString(1));
				
				Group grid = (Group)diagram.getElement("grid");
				grid.loaderAddChild(null, line);
			}
			
			for (int x = 0; x < game.getStage().getWidth() - 1; ++x) {
				Line line = new Line();
				
				line.addAttribute("x1", AnimationElement.AT_XML, Double.toString((x+1) * fieldWidth));
				line.addAttribute("y1", AnimationElement.AT_XML, "0");
				line.addAttribute("x2", AnimationElement.AT_XML, Double.toString((x+1) * fieldWidth));
				line.addAttribute("y2", AnimationElement.AT_XML, Integer.toString(svgHeight));	
				line.addAttribute("stroke", AnimationElement.AT_XML, "rgb(240,240,240)");
				line.addAttribute("stroke-width", AnimationElement.AT_XML, Integer.toString(1));
				
				Group grid = (Group)diagram.getElement("grid");
				grid.loaderAddChild(null, line);		
			}
			
			diagram.updateTime(0.);
		} catch (SVGException e) {
			// ignore
			e.printStackTrace();
		}
	}
	
	/**
	 * Draws all virtual robots onto the svg
	 * @param selectedId The selected robot in the UI. this one is displayed differently.
	 */
	public void drawVirtualRobots(String selectedId) {	
		if (game.getStage().getWidth() == 0 || game.getStage().getHeight() == 0)
			return;
		
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
					String resource;
					if (r.getId().equals(selectedId)) {
						resource = "/de/unihannover/swp2015/robots2/application/svg/SelectedVirtual.svg";
					} else {
						resource = "/de/unihannover/swp2015/robots2/application/svg/Virtual.svg";
					}
					robot.addAttribute(
						"xlink:href", 
						AnimationElement.AT_XML, 
						SvgConstructor.class.getResource(resource).toURI().toString()
					);
				} catch (URISyntaxException e) {}
				
				robot.addAttribute("transform", AnimationElement.AT_XML, 
					TransformationStringBuilder.getObjectTransformation(r.getPosition(), Orientation.WEST, fieldWidth, fieldHeight)
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
		if (game.getStage().getWidth() == 0 || game.getStage().getHeight() == 0)
			return;
		
		List <IPosition> startPositions = game.getStage().getStartPositions();

		double fieldWidth = svgWidth / game.getStage().getWidth();
		double fieldHeight = svgHeight / game.getStage().getHeight();
		
		try {
			for (IPosition pos : startPositions) {
				Path path = new Path();
				try {
					path.addAttribute("d", AnimationElement.AT_XML, ResourceLoader.loadResourceAsString("/de/unihannover/swp2015/robots2/application/svg/Arrow.txt"));
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
					TransformationStringBuilder.getObjectTransformation(pos, Orientation.EAST, fieldWidth, fieldHeight)
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
