package de.unihannover.swp2015.robots2.application.windows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Alert;
import org.apache.pivot.wtk.ApplicationContext;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.FileBrowserSheet;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.Sheet;
import org.apache.pivot.wtk.SheetCloseListener;
import org.apache.pivot.wtk.TableView;
import org.apache.pivot.wtk.Window;
import org.json.JSONException;

import de.unihannover.swp2015.robots2.application.MapLoader;
import de.unihannover.swp2015.robots2.application.components.StrategicVisualization;
import de.unihannover.swp2015.robots2.application.controllers.TableController;
import de.unihannover.swp2015.robots2.application.events.IVisualizationClickEvent;
import de.unihannover.swp2015.robots2.application.exceptions.InvalidMapFile;
import de.unihannover.swp2015.robots2.application.models.TableElement;
import de.unihannover.swp2015.robots2.application.observers.VisualizationUpdater;
import de.unihannover.swp2015.robots2.controller.interfaces.ProtocolException;
import de.unihannover.swp2015.robots2.controller.main.GuiMainController;
import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;

public class ControlPanel extends Window implements Bindable, IVisualizationClickEvent, IModelObserver {
	// Constants
	final private Integer initialWidth = 800;
	final private Integer initialHeight = 600;
	
	// All buttons
	@BXML private PushButton loadMap;
	@BXML private PushButton startGame;
	@BXML private PushButton pauseGame;
	@BXML private PushButton resetGame;
	@BXML private PushButton openConfiguration;
	@BXML private PushButton closeVisualization;
	@BXML private PushButton connectButton;
	@BXML private PushButton deleteRobotButton;
	@BXML private PushButton disableRobotButton;
	@BXML private PushButton placeButton;

	// Participants table
	@BXML private TableView participantTable; 
	
	// Visualization
	@BXML private StrategicVisualization visualization;
	
	// Oberservers
	private VisualizationUpdater visualizationUpdater;
	private TableController tableController;
	
	// Other Controllers
	private GuiMainController controller;
	
	// Windows
	private Configurator configurator;
	
	// flags:
	private boolean placementMode;
	
	/**
	 * initialize is called by pivot after creating the object.
	 * 
	 * @param namespace An apache pivot object.
	 * @param location The bxml file this class stems from.
	 * @param resources resources.
	 */
	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		createConfigurator();
		
		// event handlers:
		loadMap.getButtonPressListeners().add(loadMapAction);
		closeVisualization.getButtonPressListeners().add(closeApp);
		openConfiguration.getButtonPressListeners().add(openConfigurator);
		startGame.getButtonPressListeners().add(startGameAction);
		resetGame.getButtonPressListeners().add(resetGameAction);
		pauseGame.getButtonPressListeners().add(pauseGameAction);
		connectButton.getButtonPressListeners().add(connectAction);
		deleteRobotButton.getButtonPressListeners().add(deleteRobotAction);
		disableRobotButton.getButtonPressListeners().add(disableRobotAction);
		placeButton.getButtonPressListeners().add(placeAction);
		visualization.addClickHandler(this);
		
	}
	
	/**
	 * Button action to delete a robot (bye bye)
	 */
	private ButtonPressListener deleteRobotAction = new ButtonPressListener() {
		
		@Override
		public void buttonPressed(Button button) {
			TableElement elem = tableController.getSelected();
			if (elem != null) {
				controller.deleteRobot(elem.getId());
			}
		}
	};
	
	/**
	 * Button action for placeing a robot.
	 */
	private ButtonPressListener placeAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button arg0) {
			togglePlacementMode();
			if (tableController.getSelected() == null && placementMode) {
				Alert.alert("Please select a robot to place now and click on an arrow.", ControlPanel.this);
			}
		}
	};

	
	/**
	 * Button action to disable a robot (maybe disqualification) 
	 */
	private ButtonPressListener disableRobotAction = new ButtonPressListener() {
		
		@Override
		public void buttonPressed(Button button) {
			TableElement elem = tableController.getSelected();
			if (elem != null) {
				controller.disableRobot(elem.getId());
			}
		}
	};
	
	/**
	 * Creates the configurator controller.
	 */
	private void createConfigurator()
	{
		try {
			BXMLSerializer bxmlSerializer = new BXMLSerializer();
	        bxmlSerializer.getNamespace().put("application", this);
	        this.configurator = (Configurator)bxmlSerializer.readObject(getClass().getResource("/de/unihannover/swp2015/robots2/application/bxml/Configurator.bxml"));
			configurator.setPreferredSize(initialWidth, initialHeight);
		} catch (IOException e) {
			e.printStackTrace();
			Alert.alert(MessageType.INFO, e.getMessage(), ControlPanel.this);
			return;
		} catch (SerializationException e) {
			e.printStackTrace();
			Alert.alert(MessageType.INFO, "Could not parse: \n" + e.getMessage(), ControlPanel.this);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			Alert.alert(MessageType.INFO, "Other exception: \n" + e.getMessage(), ControlPanel.this);
			return;
		}
	}
	
	/**
	 * Sets the network controller.
	 * @param controller GuiController
	 */
	public void setController(GuiMainController controller) {
		this.controller = controller;
	}
	
	/**
	 * Action performed when loadMap button is clicked.
	 * Loads Map and transfers it to the server. Do not do more than that and wait for response.
	 */
	private ButtonPressListener loadMapAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button button) {
			final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet(
				FileBrowserSheet.Mode.valueOf("OPEN"),
				System.getProperty("user.dir")
			);
			
			fileBrowserSheet.open(ControlPanel.this, new SheetCloseListener() {
				@Override
                public void sheetClosed(Sheet sheet) {
					if (sheet.getResult()) {
						File file = fileBrowserSheet.getSelectedFile();
						try {
							// has side effects
							new MapLoader(controller, file.getAbsolutePath());
						} catch (JSONException e) {
							e.printStackTrace();
							Alert.alert(MessageType.ERROR, "The json file is not valid json!\n" + e.getMessage(), ControlPanel.this);
						} catch (InvalidMapFile e) {
							e.printStackTrace();
							Alert.alert(MessageType.ERROR, e.getMessage(), ControlPanel.this);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							Alert.alert(MessageType.ERROR, e.getMessage(), ControlPanel.this);
						}
					}
				}
			});
		}
	};
	
	/**
	 * Start visualization and automatic updating.
	 * Won't restart it if already runs.
	 */
	public void startAutoUpdateOnce() {		
		if (visualizationUpdater != null)
			return;
		
		visualization.setGame(controller, controller.getGame(), configurator.getGeneralOptions());
		visualizationUpdater = new VisualizationUpdater(visualization, controller);
		
		tableController = new TableController(participantTable, 
										  controller, 
										  configurator.getGeneralOptions(),
										  visualization);
		
		controller.getGame().observe(this);
        configurator.setController(controller);
		
		ApplicationContext.scheduleRecurringCallback(new Runnable() {			
			@Override
			public void run() {
				boolean synced = controller.getGame().isSynced();
				
				loadMap.setEnabled(synced);
				startGame.setEnabled(synced);
				pauseGame.setEnabled(synced);
				resetGame.setEnabled(synced);
				deleteRobotButton.setEnabled(synced);
				disableRobotButton.setEnabled(synced);
				connectButton.setEnabled(synced);
				placeButton.setEnabled(synced);
				
				visualization.setConnectionState(synced);
				visualizationUpdater.update();
				tableController.update();
			}
		}, 100);
	}
	
	/**
	 * Button press action to end application.
	 */
	private ButtonPressListener closeApp = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button button) {
			DesktopApplicationContext.exit();
		}
	};
	
	/**
	 * Connects with the MQTT server.
	 */
	private ButtonPressListener connectAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button button) {
			try {
				startAutoUpdateOnce();
				controller.startMqtt(/*"tcp://" + */configurator.getGeneralOptions().getRemoteUrl());
				loadMap.setEnabled(true);
			}
			catch (ProtocolException exc) {
				Alert.alert(MessageType.ERROR, exc.getMessage(), ControlPanel.this);
			}
		}
	};
	
	/**
	 * Button press action to start the game
	 */
	private ButtonPressListener startGameAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button button) {
			controller.startGame();
		}
	};
	
	/**
	 * Button press action to pause the game
	 */
	private ButtonPressListener pauseGameAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button button) {
			controller.stopGame();
		}
	};
	
	/**
	 * Button press action to stop the game
	 */
	private ButtonPressListener resetGameAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button button) {
			controller.resetGame();
		}
	};
	
	/**
	 * The open configurator button creates a new Display which then creates a new window,
	 * which is the configurator window.
	 */
	private ButtonPressListener openConfigurator = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button button) {
	        DesktopApplicationContext.createDisplay(initialWidth + 15, 
	        									    initialHeight + 39, 
												    100, 
												    100, 
												    false, 
												    true, 
												    false, 
												    getDisplay().getHostWindow(), 
												    new DesktopApplicationContext.DisplayListener() 
	        {
				@Override
				public void hostWindowClosed(Display display) {
					configurator.close();
					visualization.setOptions(configurator.getGeneralOptions());
					if (tableController != null)
						tableController.setOptions(configurator.getGeneralOptions());
				}
				@Override
				public void hostWindowOpened(Display display) {
					configurator.open(display);
				}	        	
	        });        			
		}		
	};

	/**
	 * Callback from StrategicVisualization. Called on left mouse click on field.
	 * @param button A pivot button. Useful for button click information.
	 * @param rx Model correct x position of click (field-x).
	 * @param ry Model correct y position of click (field-y)
	 */
	@Override
	public void visualizationClicked(org.apache.pivot.wtk.Mouse.Button button, 
									 int rx, 
									 int ry) 
	{
		IGame game = controller.getGame();
		
		// Was a robot clicked?
		for (IRobot robot : game.getRobots().values()) {
        	IPosition pos = robot.getPosition();
        	if (pos.getX() == rx && pos.getY() == ry) {
        		// robot found.
        		tableController.selectRobotWithId(robot.getId());
        	}
        }		
		
		// Was a start position clicked & are we in placementMode?
		if (placementMode) {
			for (IPosition startPosition : game.getStage().getStartPositions()) {
				if (startPosition.getX() == rx && startPosition.getY() == ry) {
					// checkings are for gui
					if (tableController.getSelected() != null) {
						tableController.placeSelectedRobotAt(startPosition);
						togglePlacementMode();
					}
				}
			}
		}
	}
	
	/**
	 * Shows/Hides StartPositions and changes button caption.
	 */
	private void togglePlacementMode() {
		placementMode = !placementMode;
		visualization.setStartPositionsVisible(placementMode);
		if (placementMode)
			placeButton.setButtonData("Abort Placement");
		else
			placeButton.setButtonData("Assign Position");		
	}

	/**
	 * Bring the UI up to date with this, if restarted.
	 * @param event A model event caused by network.
	 */
	@Override
	public void onModelUpdate(IEvent event) {
		if (event.getType() == IEvent.UpdateType.STAGE_WALL ||
			event.getType() == IEvent.UpdateType.STAGE_SIZE ||
			event.getType() == IEvent.UpdateType.STAGE_GROWINGRATE) {
			
			startGame.setEnabled(true);
			pauseGame.setEnabled(true);
			resetGame.setEnabled(true);			
		}			
	}
}
