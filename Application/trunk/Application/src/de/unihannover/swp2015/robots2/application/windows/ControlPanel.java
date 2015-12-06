package de.unihannover.swp2015.robots2.application.windows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.eclipse.paho.client.mqttv3.MqttException;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Alert;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
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
import de.unihannover.swp2015.robots2.application.exceptions.InvalidMapFile;
import de.unihannover.swp2015.robots2.controller.main.GuiMainController;

import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;

public class ControlPanel extends Window implements Bindable {
	// Constants
	final private Integer initialWidth = 800;
	final private Integer initialHeight = 600;
	
	// All buttons
	@BXML private PushButton loadMap;
	@BXML private PushButton startGame;
	@BXML private PushButton pauseGame;
	@BXML private PushButton stopGame;
	@BXML private PushButton startProjection;
	@BXML private PushButton openConfiguration;
	@BXML private PushButton closeVisualization;
	@BXML private PushButton connectButton;

	// Participants table
	@BXML private TableView participantTable; 
	
	// Visualization
	@BXML private StrategicVisualization visualization;
	
	private GuiMainController controller;
	private Configurator configurator;
	
	/**
	 * initialize is called by pivot after creating the object.
	 * 
	 * @param namespace An apache pivot object.
	 * @param location The bxml file this class stems from.
	 * @param resources resources.
	 */
	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		createControlPanel();
		
		loadMap.getButtonPressListeners().add(loadMapAction);
		closeVisualization.getButtonPressListeners().add(closeApp);
		openConfiguration.getButtonPressListeners().add(openConfigurator);
		startGame.getButtonPressListeners().add(startGameAction);
		stopGame.getButtonPressListeners().add(stopGameAction);
		pauseGame.getButtonPressListeners().add(pauseGameAction);
		connectButton.getButtonPressListeners().add(connectAction);
	}	
	
	/**
	 * Creates the control panel controller.
	 */
	private void createControlPanel()
	{
		try {
			BXMLSerializer bxmlSerializer = new BXMLSerializer();
	        bxmlSerializer.getNamespace().put("application", this);
	        this.configurator = (Configurator)bxmlSerializer.readObject(getClass().getResource("/de/unihannover/swp2015/robots2/application/Configurator.bxml"));
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
							
							visualization.setGame(controller, controller.getGame());
							visualization.repaint();
							
							startGame.setEnabled(true);
							pauseGame.setEnabled(true);
							stopGame.setEnabled(true);
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
				controller.startMqtt("tcp://" + configurator.getGeneralOptions().getRemoteUrl());
				loadMap.setEnabled(true);
			}
			catch (MqttException exc) {
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
	private ButtonPressListener stopGameAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button button) {
			controller.stopGame();
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
				}
				@Override
				public void hostWindowOpened(Display display) {
					configurator.open(display);
				}	        	
	        });        			
		}		
	};
}
