package de.unihannover.swp2015.robots2.windows;

import java.io.File;
import java.io.IOException;
import java.net.URL;

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

	// Participants table
	@BXML private TableView participantTable;
	
	// Configurator Window
	private Configurator configurator = null; 
	
	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		loadMap.getButtonPressListeners().add(loadMapAction);
		closeVisualization.getButtonPressListeners().add(closeApp);
		openConfiguration.getButtonPressListeners().add(openConfigurator);
	}	
	
	/**
	 * Action performed when loadMap button is clicked.
	 * Loads Map and transfers it to the server. Do not do more than that and wait for response.
	 */
	private ButtonPressListener loadMapAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button button) {
			final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet();
			fileBrowserSheet.setMode(FileBrowserSheet.Mode.valueOf("OPEN"));
			
			fileBrowserSheet.open(ControlPanel.this, new SheetCloseListener() {
				@Override
                public void sheetClosed(Sheet sheet) {
					if (sheet.getResult()) {
						File file = fileBrowserSheet.getSelectedFile();
						
						Alert.alert(MessageType.INFO, file.getAbsolutePath(), ControlPanel.this);
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
					try {
						BXMLSerializer bxmlSerializer = new BXMLSerializer();
				        bxmlSerializer.getNamespace().put("application", this);
				        configurator = (Configurator)bxmlSerializer.readObject(getClass().getResource("/de/unihannover/swp2015/robots2/Configurator.bxml"));
						configurator.setPreferredSize(initialWidth, initialHeight);
						configurator.open(display);		
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
	        });        			
		}		
	};
}
