package de.unihannover.swp2015.robots2.components;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ClassLoader;
import java.net.URL;

import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Action;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Menu;
import org.apache.pivot.wtk.MenuHandler;
import org.apache.pivot.wtk.Panel;
import org.apache.pivot.wtk.media.Drawing;
import org.apache.pivot.wtk.media.SVGDiagramSerializer;
import org.apache.pivot.beans.Bindable;

import com.kitfox.svg.SVGDiagram;

import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.svg.SvgConstructor;

/**
 * An apache pivot component for displaying a game. 
 * @author Tim
 */
public class StrategicVisualization extends Panel implements Bindable {
	private Drawing drawing; // svg drawing
	private SvgConstructor svgConstructor;
	private IGame game;
	
	/**
	 * Constructor loads default svg.
	 * @throws IOException Thrown if svg resource 'loadMap' is invalid or damaged.
	 */
	public StrategicVisualization() throws IOException {		
		super();
		
		// load default svg
		loadDefault();	
	}
	
	/**
	 * Puts the game into the visualization.
	 * @param game
	 */
	public void setGame(IGame game) {
		this.game = game;
		svgConstructor = new SvgConstructor(game);
		update();
		generateDrawing();
		setMenuHandler(menuHandler);
	}
	
	/**
	 * Context menu for visualization
	 */
	private MenuHandler menuHandler = new MenuHandler.Adapter() {
		@Override
		public boolean configureContextMenu(Component component, Menu menu, final int x, final int y) {
			//final Component descendant = getDescendantAt(x, y);
			
			// Section for field control
			Menu.Section menuSection = new Menu.Section();
            menu.getSections().add(menuSection);
			
            Menu.Item getCoord = new Menu.Item("Get Coordinate");
            getCoord.setAction(new Action() {
                @Override
                public void perform(Component source) {
                	System.out.print((int)(game.getStage().getWidth() * (double)x / (double)StrategicVisualization.this.getWidth()));
                	System.out.print(":");
                	System.out.println((int)(game.getStage().getWidth() * (double)y / (double)StrategicVisualization.this.getHeight()));
                }
            });
 
            menuSection.add(getCoord);
			return false;
		}
	};
	
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
		if (game == null)
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
		
		if (!game.isRunning())
			svgConstructor.drawStartPositions();
		
		svgConstructor.drawRobots();
		svgConstructor.drawVirtualRobots();
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

	/**
	 * Called when panel is created.
	 * See Apache Pivot documentation.
	 */
	@Override
	public void initialize(Map<String, Object> arg0, URL arg1, Resources arg2) {	
	}
}
