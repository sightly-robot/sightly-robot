package de.unihannover.swp2015.robots2.application.windows;

import java.awt.Color;
import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.ButtonStateListener;
import org.apache.pivot.wtk.Checkbox;
import org.apache.pivot.wtk.ListButton;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.TabPane;
import org.apache.pivot.wtk.TextArea;
import org.apache.pivot.wtk.TextArea.Paragraph;
import org.apache.pivot.wtk.TextAreaContentListener;
import org.apache.pivot.wtk.Window;
import org.apache.pivot.wtk.Button.State;

import de.unihannover.swp2015.robots2.application.models.GeneralOptions;
import de.unihannover.swp2015.robots2.application.models.TableElement;
import de.unihannover.swp2015.robots2.controller.externalInterfaces.IVisualizationControl;
import de.unihannover.swp2015.robots2.controller.main.GuiMainController;
import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.utils.VisualizationOptions;

public class Configurator extends Window implements Bindable, IVisualizationControl
{
	// BXML bindings (General Options)
	@BXML private TabPane tabs;
	@BXML private TextArea ipTextArea;
	@BXML private Checkbox debugMode;
	
	// BXML bindings (Visualization Options)
	@BXML private ListButton vizualizationList;
	@BXML private PushButton updateButton;
	
	// Models
	GeneralOptions generalOptions;
	GuiMainController controller;
	
	/**
	 * Apache pivot initialize method. Called on initialization of window. 
	 */
	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		generalOptions = new GeneralOptions();
		setGeneralOptions(generalOptions); // set to defaults
		
		ipTextArea.getTextAreaContentListeners().add(ipChangeAction);
		debugMode.getButtonStateListeners().add(debugModeChangeAction);
		updateButton.getButtonPressListeners().add(updateVisualizationListAction);
	}

	/**
	 * Button to update the visualization list.
	 */
	private ButtonPressListener updateVisualizationListAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button button) {
			controller.getVisualizationSettings();
		}
	};

	/**
	 * Get generalOptions.
	 * @return Returns all generalOptions. 
	 */
	public GeneralOptions getGeneralOptions() {
		return generalOptions;
	}

	/**
	 * Set generalOptions.
	 * @param generalOptions see GeneralOptions class.
	 */
	public void setGeneralOptions(GeneralOptions generalOptions) {
		this.generalOptions = generalOptions;
		ipTextArea.setText(generalOptions.getRemoteUrl());
		
		if (generalOptions.isInDebugMode())
			debugMode.setState(State.SELECTED);
		else
			debugMode.setState(State.UNSELECTED);
	}	
	
	/**
	 * Updates generalOptions on changing ip text immediately.
	 */
	private TextAreaContentListener ipChangeAction = new TextAreaContentListener() {
		@Override
		public void paragraphInserted(TextArea self, int arg1) {}

		@Override
		public void paragraphsRemoved(TextArea self, int arg1, Sequence<Paragraph> arg2) {}

		@Override
		public void textChanged(TextArea self) {
			generalOptions.setRemoteUrl(self.getText());
		}		
	};


	/**
	 * Listener for debugMode checkbox.
	 */
	private ButtonStateListener debugModeChangeAction = new ButtonStateListener() {
		@Override
		public void stateChanged(Button arg0, State state) {
			generalOptions.setInDebugMode(!state.equals(State.SELECTED));
		}
	};

	/**
	 * Set GuiMainController for internal use.
	 * @param controller THE GUI controller.
	 */
	void setController(GuiMainController controller) {
		this.controller = controller;
		controller.registerVisualizationControl(this);
		updateButton.setEnabled(true);
	}

	/**
	 * This is called whenever a request for all visualization settings is requested.
	 * For each and every visualization.
	 */
	@Override
	public void receiveSettings(String settings) {
		// TODO Auto-generated method stub	
		System.out.println(settings);
	}
}