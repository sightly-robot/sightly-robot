package de.sightly_robot.sightly_robot.application.components;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentMouseButtonListener;
import org.apache.pivot.wtk.Menu;
import org.apache.pivot.wtk.MenuHandler;
import org.apache.pivot.wtk.Panel;
import org.apache.pivot.wtk.media.Drawing;
import org.apache.pivot.wtk.media.SVGDiagramSerializer;

import com.kitfox.svg.SVGDiagram;

import de.sightly_robot.sightly_robot.application.events.IVisualizationClickEvent;
import de.sightly_robot.sightly_robot.application.models.GeneralOptions;
import de.sightly_robot.sightly_robot.application.svg.SvgConstructor;
import de.sightly_robot.sightly_robot.controller.main.GuiMainController;
import de.sightly_robot.sightly_robot.model.interfaces.IGame;

/**
 * An apache pivot component for displaying a game. 
 * @author Tim
 */
public class StrategicVisualization extends Panel implements Bindable {
	/** The drawing displaying the game **/
	private Drawing drawing; // svg drawing
	
	/** The SVG creator class **/ 
	private SvgConstructor svgConstructor;
	
	/** The internal game state **/
	private IGame game;
	
	/** THE controller **/
	private GuiMainController controller;
	
	/** A SVG for displaying a connection loss **/
	private Drawing errorDrawing;

	/** Are we connected and in sync with the broker? **/
	private boolean synced = true;
	
	/** Show arrows on field for placeing a robot? **/
	private boolean showStartPositions;	
	
	/** Click Event Handler for clicking on the visualization **/
	private Set <IVisualizationClickEvent> clickEventHandlers;
	
	/** Reference to the options object. **/
	private GeneralOptions options;
	
	/** Used for special highlighting **/
	private String selectedRobotId;
	
	/**
	 * Constructor loads default svg.
	 * @throws IOException Thrown if svg resource 'loadMap' is invalid or damaged.
	 */
	public StrategicVisualization() throws IOException {		
		super();
		
		clickEventHandlers = new HashSet<>();
		selectedRobotId = ""; // no selection
		
		// load default svg
		loadDefault();	
		
		// load error svg
		loadConnectionLost();
	}
	
	/**
	 * Puts the game into the visualization.
	 * @param game
	 */
	public void setGame(GuiMainController controller, IGame game, GeneralOptions options) {
		this.game = game;
		this.controller = controller;
		this.options = options;
		
		svgConstructor = new SvgConstructor(game);
		generateDrawing();
		setMenuHandler(menuHandler);
		this.getComponentMouseButtonListeners().add(clickAction);
	}
	
	/**
	 * Registers a click listener.
	 * @param event A click event listener.
	 */
	public void addClickHandler(IVisualizationClickEvent event) {
		clickEventHandlers.add(event);
	}
	
	/**
	 * Resets the selected robot. Needed if selected robot is changed from the list and not the visualization. 
	 * @param id The selected robot id.
	 */
	public void setSelectedRobotId(String id) {
		selectedRobotId = id;
	}
	
	/**
	 * Show or hide the start positions.
	 * @param visible Shall show start positions
	 */
	public void setStartPositionsVisible(boolean visible) {
		showStartPositions = visible;
		update();
		repaint();
	}
	
	/**
	 * Called when user clicks into the visualization
	 */
	private ComponentMouseButtonListener clickAction = new ComponentMouseButtonListener() {
		@Override
		public boolean mouseClick(Component component, org.apache.pivot.wtk.Mouse.Button button, int x, int y,
				int count) {
			final int rx = (int)(game.getStage().getWidth() * ((double)x / (double)StrategicVisualization.this.getWidth()));
            final int ry = (int)(game.getStage().getHeight() * ((double)y / (double)StrategicVisualization.this.getHeight()));
            
            for (IVisualizationClickEvent event : clickEventHandlers) {
            	event.visualizationClicked(button, rx, ry);
            }
            
			return false;
		}

		@Override
		public boolean mouseDown(Component component, org.apache.pivot.wtk.Mouse.Button button, int x, int y) {
			// not needed method
			return false;
		}

		@Override
		public boolean mouseUp(Component component, org.apache.pivot.wtk.Mouse.Button button, int x, int y) {
			// not needed method
			return false;
		}
	};
	
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
            
            final int rx = (int)(game.getStage().getWidth() * ((double)x / (double)StrategicVisualization.this.getWidth()));
            final int ry = (int)(game.getStage().getHeight() * ((double)y / (double)StrategicVisualization.this.getHeight()));
            
            ContextMenuActionProvider contextMenu = new ContextMenuActionProvider(rx, 
            																	  ry, 
            																	  options, 
            																	  game, 
            																	  controller, 
            																	  StrategicVisualization.this);
            
            // Place Robot
            Menu.Item placeRobot = new Menu.Item("Place Robot Here");
            placeRobot.setAction(contextMenu.placeRobotHereAction);            
            menuSection.add(placeRobot);
            
            // Free Field
            Menu.Item freeField = new Menu.Item("Free Field");
            freeField.setAction(contextMenu.freeFieldAction);
            menuSection.add(freeField);
			return false;
		}
	};
	
	public void setConnectionState(boolean synced) {
		this.synced = synced; 
	}
	
	/**
	 * Loads a default svg into the viewport. We cannot display a map before the user loaded any.
	 * @throws IOException Throws if the svg is invalid.
	 */
	private void loadDefault() throws IOException {
		InputStream stream = ClassLoader.class.getResourceAsStream("/de/unihannover/swp2015/robots2/application/svg/ConnectionLost.svg");
		
		SVGDiagramSerializer serializer = new SVGDiagramSerializer();
		SVGDiagram diagram;
		diagram = serializer.readObject(stream);
		errorDrawing = new Drawing(diagram);
	}
	
	private void loadConnectionLost() throws IOException {
		InputStream stream = ClassLoader.class.getResourceAsStream("/de/unihannover/swp2015/robots2/application/svg/LoadMap.svg");
		
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
	public synchronized void update() {		
		svgConstructor.resetSvg();
		
		// NOTE! The order is not important, the template dictates the order.
		svgConstructor.drawResources();
		svgConstructor.drawGrid();
		svgConstructor.drawWalls();
		
		if (showStartPositions)
			svgConstructor.drawStartPositions();
		
		svgConstructor.drawRobots(selectedRobotId);
		svgConstructor.drawVirtualRobots(selectedRobotId);
		
		if (options.isInDebugMode())
			svgConstructor.drawLockedFields();
	}

	/**
	 * Overrides Panel paint method. This one paints the loaded svg and scales it to the correct size.
	 * @param graphics AWT Graphics2D painter.
	 */
	@Override
	public synchronized void paint(Graphics2D graphics) {
		
		if (synced) {
			generateDrawing();
			if (drawing != null) {
				graphics.scale((double)getWidth() / (double)drawing.getWidth(), (double)getHeight() / (double)drawing.getHeight());
				drawing.paint(graphics);
			}
		} else {
			if (errorDrawing != null) {
				graphics.scale((double)getWidth() / (double)errorDrawing.getWidth(), (double)getHeight() / (double)errorDrawing.getHeight());
				errorDrawing.paint(graphics);
			}			
		}
		
		super.paint(graphics);
	}

	/**
	 * Called when panel is created.
	 * See Apache Pivot documentation.
	 */
	@Override
	public void initialize(Map<String, Object> arg0, URL arg1, Resources arg2) {	
	}

	/**
	 * Sets the new options.
	 * @param generalOptions A new generalOptions object.
	 */
	public void setOptions(GeneralOptions generalOptions) {
		this.options = generalOptions;		
	}
}
