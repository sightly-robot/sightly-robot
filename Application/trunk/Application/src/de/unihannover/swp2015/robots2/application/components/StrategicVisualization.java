package de.unihannover.swp2015.robots2.application.components;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ClassLoader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Action;
import org.apache.pivot.wtk.Alert;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentMouseButtonListener;
import org.apache.pivot.wtk.Dialog;
import org.apache.pivot.wtk.DialogCloseListener;
import org.apache.pivot.wtk.Menu;
import org.apache.pivot.wtk.MenuHandler;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.Panel;
import org.apache.pivot.wtk.media.Drawing;
import org.apache.pivot.wtk.media.SVGDiagramSerializer;
import org.apache.pivot.beans.Bindable;

import com.kitfox.svg.SVGDiagram;

import de.unihannover.swp2015.robots2.application.dialogs.DialogFactory;
import de.unihannover.swp2015.robots2.application.dialogs.ListDialog;
import de.unihannover.swp2015.robots2.application.events.IVisualizationClickEvent;
import de.unihannover.swp2015.robots2.application.svg.SvgConstructor;
import de.unihannover.swp2015.robots2.controller.main.GuiMainController;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;

/**
 * An apache pivot component for displaying a game. 
 * @author Tim
 */
public class StrategicVisualization extends Panel implements Bindable {
	private Drawing drawing; // svg drawing
	private SvgConstructor svgConstructor;
	private IGame game;
	private GuiMainController controller;
	private boolean synced = true;
	private Drawing errorDrawing;
	private Set <IVisualizationClickEvent> clickEventHandlers;	
	
	/**
	 * Constructor loads default svg.
	 * @throws IOException Thrown if svg resource 'loadMap' is invalid or damaged.
	 */
	public StrategicVisualization() throws IOException {		
		super();
		
		clickEventHandlers = new HashSet<>();
		
		// load default svg
		loadDefault();	
		
		// load error svg
		loadConnectionLost();
	}
	
	/**
	 * Puts the game into the visualization.
	 * @param game
	 */
	public void setGame(GuiMainController controller, IGame game) {
		this.game = game;
		this.controller = controller;
		
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
			
            // Get Coordinate
            Menu.Item getCoord = new Menu.Item("Get Coordinate");
            getCoord.setAction(new Action() {
                @Override
                public void perform(Component source) {
                	System.out.print(rx);
                	System.out.print(":");
                	System.out.println(ry);
                }
            }); 
            
            // Place Robot
            Menu.Item placeRobot = new Menu.Item("Place Robot Here");
            placeRobot.setAction(new Action() {
            	@Override
            	public void perform(Component source) {        			
            		List <IPosition> startPositions = game.getStage().getStartPositions();
            		// does not allow if the selected position is no start position!
            		Boolean found = false;
            		IPosition startPos = null;
            		for (IPosition pos : startPositions) {
            			found = (pos.getX() == rx && pos.getY() == ry);
            			if (found) {
            				startPos = pos;
            				break;
            			}
            		}
            		
            		if (!found) {
						Alert.alert(MessageType.ERROR, "Cannot place robot on field, that is not a start position", StrategicVisualization.this.getWindow());
            		} else {            			
            			// compile robot list
            			final List<IRobot> robots = new ArrayList<IRobot>();
            			org.apache.pivot.collections.List <String> shownList = new org.apache.pivot.collections.ArrayList <String>();
            			
            			for (IRobot robot : game.getRobots().values()) {
            				shownList.add(robot.getId());
            				robots.add(robot);
            			}
            			
            			//Robot robot = new Robot("223344", true, false);
            			//shownList.add(robot.getId());
            			
            			final IPosition sp = startPos;
            			DialogFactory.createListDialog(StrategicVisualization.this.getWindow(), new DialogCloseListener() {
							@Override
							public void dialogClosed(Dialog dialog, boolean modal) {								
								ListDialog listDialog = (ListDialog)dialog;
								
								// do nothing if there is no selection
								if (listDialog.getSelectedIndex() == -1)
									return;
								
								controller.getGame().getRobots().get(robots.get(listDialog.getSelectedIndex()));
		            			//Robot robo = new Robot(((InputDialog)dialog).getText(), false, false);
								IRobot robo = game.getRobots().get(((ListDialog)dialog).getSelectedElement());
		            			StrategicVisualization.this.controller.setRobotPosition(rx, ry, sp.getOrientation(), robo);						
							}
            			}, shownList);
            		}
            	}
            });
            
            menuSection.add(getCoord);
            menuSection.add(placeRobot);
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
}
