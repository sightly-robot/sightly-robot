package de.unihannover.swp2015.robots2.components;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ClassLoader;
import org.apache.pivot.wtk.Panel;
import org.apache.pivot.wtk.media.Drawing;
import org.apache.pivot.wtk.media.SVGDiagramSerializer;

import com.kitfox.svg.SVGDiagram;

import de.unihannover.swp2015.robots2.GameState;
import de.unihannover.swp2015.robots2.SvgConstructor;

/**
 * An apache pivot component for displaying a game. 
 * @author Tim
 */
public class StrategicVisualization extends Panel {
	private Drawing drawing; // svg drawing
	private GameState state;
	private SvgConstructor svgConstructor;
	
	/**
	 * Constructor loads default svg.
	 * @throws IOException Thrown if svg resource 'loadMap' is invalid or damaged.
	 */
	public StrategicVisualization() throws IOException {		
		super();
		
		state = null;
		
		// load default svg
		loadDefault();
	}
	
	/**
	 * Loads GameState, which contains all robots and the map.
	 * @param state
	 */
	public void loadState(GameState state) {
		this.state = state;
		svgConstructor = new SvgConstructor(state);
		update();
		generateDrawing();
	}
	
	/**
	 * Loads a default svg into the viewport. We cannot display a map before the user loaded any.
	 * @throws IOException Throws if the svg is invalid.
	 */
	private void loadDefault() throws IOException {
		InputStream stream = ClassLoader.class.getResourceAsStream("/de/unihannover/swp2015/robots2/LoadMap.svg");
		
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
		
		SVGDiagram diagram = svgConstructor.getDiagram();
		drawing = new Drawing(diagram);
	}
	
	/**
	 * Reconstructs the svg. Warning! Expensive!
	 */
	public void update() {
		svgConstructor.resetSvg();
		svgConstructor.drawResources();
		svgConstructor.drawWalls();
		svgConstructor.drawStartPositions();
	}

	/**
	 * Overrides Panel paint method. This one paints the loaded svg and scales it to the correct size.
	 * @param graphics AWT Graphics2D painter.
	 */
	@Override
	public void paint(Graphics2D graphics) {
		graphics.scale((double)getWidth() / (double)drawing.getWidth(), (double)getHeight() / (double)drawing.getHeight());
		
		// reconstruct the svg from state.
		if (drawing != null)	
			drawing.paint(graphics);
		
		super.paint(graphics);
	}
}
