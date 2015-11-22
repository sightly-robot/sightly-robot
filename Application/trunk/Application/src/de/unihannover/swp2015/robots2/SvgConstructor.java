package de.unihannover.swp2015.robots2;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.List;

import com.kitfox.svg.Group;
import com.kitfox.svg.Line;
import com.kitfox.svg.Rect;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGUniverse;
import com.kitfox.svg.animation.AnimationElement;

public class SvgConstructor {
	private GameState state;
	private SVGDiagram diagram;
	private URI svgUri; 
	private SVGUniverse universe;
	
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
	 * Generates the svg base
	 */
	public void resetSvg() {		
		InputStream stream = ClassLoader.class.getResourceAsStream("/de/unihannover/swp2015/robots2/base.svg");		
		Reader rd = new InputStreamReader(stream); 
		
		// create the svg stub
		universe.clear();
		svgUri = universe.loadSVG(rd, "/visualization");
		
		this.diagram = universe.getDiagram(svgUri);
	}
	
	/**
	 * Draws resources to svg. Before calling it again, you must call reset svg.
	 */
	public void drawResources() {
		Group content = (Group)diagram.getElement("content");
		Group resources = (Group)diagram.getElement("resources");
		
		final class colorStyleBuilder {
			public String getColorStyle(double percentage) {
				StringBuilder builder = new StringBuilder();
				builder.append("rgb(");
				builder.append((int)(percentage * 255));
				builder.append(",");
				builder.append(255);
				builder.append(",");
				builder.append((int)(percentage * 255));
				builder.append(")");
				return builder.toString();
			}			
		}
		
		try {
			int width = state.getMap().getWidth();
			int height = state.getMap().getHeight();		
			int fieldWidth = svgWidth / width;
			int fieldHeight = svgHeight / height;
			
			// Draw resources
			List<List<Field>> fields = state.getMap().getFields();
			for (int y = 0; y < fields.size(); y++) {
				List <Field> row = fields.get(y);
				for (int x = 0; x < row.size(); ++x) {
					Field field = row.get(x);
					
					Rect rect = new Rect();
					rect.addAttribute("x", AnimationElement.AT_XML, Integer.toString(x * fieldWidth));
					rect.addAttribute("y", AnimationElement.AT_XML, Integer.toString(y * fieldHeight));
					rect.addAttribute("width", AnimationElement.AT_XML, Integer.toString(fieldWidth));
					rect.addAttribute("height", AnimationElement.AT_XML, Integer.toString(fieldHeight));
					rect.addAttribute("fill", AnimationElement.AT_XML, (new colorStyleBuilder()).getColorStyle(1. - (double)field.getResources() / 10.));
					//rect.addAttribute("style", AnimationElement.AT_XML, (new colorStyleBuilder()).getColorStyle(.5));
					//rect.addAttribute("fill", AnimationElement.AT_XML, "url(#grad1)");
					resources.loaderAddChild(null, rect);
				}
			}

			//resources.updateTime(0.);
			content.updateTime(0.);
		} catch (SVGException e) {
			// ignore
			e.printStackTrace();
		}
	}
	
	/**
	 * Draws all walls onto the svg
	 */
	public void drawWalls() {
		Group walls = (Group)diagram.getElement("walls");
		
		List<List<Field>> fields = state.getMap().getFields();
		
		int width = state.getMap().getWidth();
		int height = state.getMap().getHeight();		
		int fieldWidth = svgWidth / width;
		int fieldHeight = svgHeight / height;

		try {
			for (int y = 0; y < fields.size(); y++) {
				List <Field> row = fields.get(y);
				for (int x = 0; x < row.size(); ++x) {
					Field field = row.get(x);
					
					int Ax = x * fieldWidth;
					int Ay = y * fieldHeight;
					int x1 = Ax + (field.getPassableDirections().contains(CardinalDirection.EAST) ? fieldWidth : 0);
					int x2 = Ax + (field.getPassableDirections().contains(CardinalDirection.WEST) ? 0 : fieldWidth);
					int y1 = Ay + (field.getPassableDirections().contains(CardinalDirection.SOUTH) ? fieldHeight : 0);
					int y2 = Ay + (field.getPassableDirections().contains(CardinalDirection.NORTH) ? 0 : fieldHeight);
					
					Line line = new Line();
					line.addAttribute("x1", AnimationElement.AT_XML, Integer.toString(x1));
					line.addAttribute("x2", AnimationElement.AT_XML, Integer.toString(x2));
					line.addAttribute("y1", AnimationElement.AT_XML, Integer.toString(y1));
					line.addAttribute("y2", AnimationElement.AT_XML, Integer.toString(y2));
					line.addAttribute("style", AnimationElement.AT_CSS, "stroke:rgb(255,0,0);stroke-width:2");
					walls.loaderAddChild(null, line);
				}
			}
			
			walls.updateTime(0.);
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
		return this.universe.getDiagram(svgUri);
	}
}
