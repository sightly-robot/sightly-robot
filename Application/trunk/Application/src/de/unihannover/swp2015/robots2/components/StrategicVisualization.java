package de.unihannover.swp2015.robots2.components;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.ClassLoader;
import java.net.URI;

import org.apache.pivot.wtk.Panel;
import org.apache.pivot.wtk.media.Drawing;
import org.apache.pivot.wtk.media.SVGDiagramSerializer;

import com.kitfox.svg.Circle;
import com.kitfox.svg.Group;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGElementException;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGRoot;
import com.kitfox.svg.SVGUniverse;
import com.kitfox.svg.animation.AnimationElement;

import de.unihannover.swp2015.robots2.GameState;

/**
 * An apache pivot component for displaying a game. 
 * @author Tim
 */
public class StrategicVisualization extends Panel {
	private Drawing drawing; // svg drawing
	private GameState state;
	private SVGUniverse universe;
	private URI svgUri; 
	
	/**
	 * Constructor loads default svg.
	 * @throws IOException Thrown if svg resource 'loadMap' is invalid or damaged.
	 */
	public StrategicVisualization() throws IOException {		
		super();
		
		state = null;
		universe = new SVGUniverse();
		
		// create the svg stub
		Reader rd = new StringReader("<svg width=\"1000\" height=\"1000\"><g id=\"mainGroup\"><rect width=\"1000\" height=\"1000\" style=\"fill:rgb(255,255,255);\"/></g></svg>");
		svgUri = universe.loadSVG(rd, "/visualization");
		
		// load default svg
		loadDefault();
	}
	
	/**
	 * Loads GameState, which contains all robots and the map.
	 * @param state
	 */
	public void loadState(GameState state) {
		this.state = state;
	}
	
	/**
	 * Loads a default svg into the viewport. We cannot display a map before the user loaded any.
	 * @throws IOException Throws if the svg is invalid.
	 */
	private void loadDefault() throws IOException {
		InputStream stream = ClassLoader.class.getResourceAsStream("/de/unihannover/swp2015/robots2/loadMap.svg");
		
		SVGDiagramSerializer serializer = new SVGDiagramSerializer();
		SVGDiagram diagram;
		diagram = serializer.readObject(stream);
		drawing = new Drawing(diagram);
	}
	
	/**
	 * Generates an svg graphic from data.
	 */
	private void generateDrawing() {
		if (state == null)
			return;
		
		SVGDiagram diagram = this.universe.getDiagram(svgUri);
		Group mainGroup = (Group)diagram.getElement("mainGroup");
		
		/*
		try {
			//mainGroup.loaderAddChild(null, circle);
			//mainGroup.updateTime(0.);
		} catch (SVGException e) {
			e.printStackTrace();
		}
		*/
		
		drawing = new Drawing(diagram);
	}

	/**
	 * Overrides Panel paint method. This one paints the loaded svg and scales it to the correct size.
	 * @param graphics AWT Graphics2D painter.
	 */
	@Override
	public void paint(Graphics2D graphics) {
		graphics.scale((double)getWidth() / (double)drawing.getWidth(), (double)getHeight() / (double)drawing.getHeight());
		
		// reconstruct the svg from state.
		generateDrawing();
		if (drawing != null)	
			drawing.paint(graphics);
		
		super.paint(graphics);
	}
}
