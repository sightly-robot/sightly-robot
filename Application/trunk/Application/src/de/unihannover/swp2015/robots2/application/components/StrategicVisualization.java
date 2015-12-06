package de.unihannover.swp2015.robots2.application.components;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ClassLoader;
import java.net.URL;
import java.util.List;

import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Action;
import org.apache.pivot.wtk.Alert;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Dialog;
import org.apache.pivot.wtk.DialogCloseListener;
import org.apache.pivot.wtk.Menu;
import org.apache.pivot.wtk.MenuHandler;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.Panel;
import org.apache.pivot.wtk.Window;
import org.apache.pivot.wtk.media.Drawing;
import org.apache.pivot.wtk.media.SVGDiagramSerializer;
import org.apache.pivot.beans.Bindable;

import com.kitfox.svg.SVGDiagram;

import de.unihannover.swp2015.robots2.application.dialogs.DialogFactory;
import de.unihannover.swp2015.robots2.application.dialogs.InputDialog;
import de.unihannover.swp2015.robots2.application.svg.SvgConstructor;
import de.unihannover.swp2015.robots2.application.windows.ControlPanel;
import de.unihannover.swp2015.robots2.controller.main.GuiMainController;
import de.unihannover.swp2015.robots2.model.implementation.Position;
import de.unihannover.swp2015.robots2.model.implementation.Robot;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

/**
 * An apache pivot component for displaying a game. 
 * @author Tim
 */
public class StrategicVisualization extends Panel implements Bindable {
	private Drawing drawing; // svg drawing
	private SvgConstructor svgConstructor;
	private IGame game;
	private GuiMainController controller;
	
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
	public void setGame(GuiMainController controller, IGame game) {
		this.game = game;
		this.controller = controller;
		
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
            
            final int rx = (int)(game.getStage().getWidth() * (double)x / (double)StrategicVisualization.this.getWidth());
            final int ry = (int)(game.getStage().getWidth() * (double)y / (double)StrategicVisualization.this.getHeight());
			
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
        			// debug
        			
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
            		found = true;
            		startPos = new Position(2, 3, Orientation.EAST);
            		
            		if (!found) {
						Alert.alert(MessageType.ERROR, "Cannot place robot on field, that is not a start position", StrategicVisualization.this.getWindow());
            		} else {
            			final IPosition sp = startPos;
            			DialogFactory.createInputDialog(StrategicVisualization.this.getWindow(), new DialogCloseListener() {
							@Override
							public void dialogClosed(Dialog dialog, boolean modal) { 
		            			Robot robo = new Robot(((InputDialog)dialog).getText(), false, false);
		            			StrategicVisualization.this.controller.setRobotPosition(rx, ry, sp.getOrientation(), robo);							
							}
						});
            		}
            	}
            });
            
            menuSection.add(getCoord);
            menuSection.add(placeRobot);
			return false;
		}
	};
	
	/**
	 * Loads a default svg into the viewport. We cannot display a map before the user loaded any.
	 * @throws IOException Throws if the svg is invalid.
	 */
	private void loadDefault() throws IOException {
		InputStream stream = ClassLoader.class.getResourceAsStream("/de/unihannover/swp2015/robots2/application/LoadMap.svg");
		
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
