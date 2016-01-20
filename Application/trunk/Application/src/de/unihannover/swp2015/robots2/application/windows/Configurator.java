package de.unihannover.swp2015.robots2.application.windows;

import java.awt.Color;
import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonStateListener;
import org.apache.pivot.wtk.Checkbox;
import org.apache.pivot.wtk.TabPane;
import org.apache.pivot.wtk.TextArea;
import org.apache.pivot.wtk.TextArea.Paragraph;
import org.apache.pivot.wtk.TextAreaContentListener;
import org.apache.pivot.wtk.Window;
import org.apache.pivot.wtk.Button.State;

import de.unihannover.swp2015.robots2.application.models.GeneralOptions;

public class Configurator extends Window implements Bindable
{
	// BXML bindings
	@BXML private TabPane tabs;
	@BXML private TextArea ipTextArea;
	@BXML private Checkbox showIdsNotNames;
	@BXML private Checkbox debugMode;
	
	// Models
	GeneralOptions generalOptions;
	
	/**
	 * Apache pivot initialize method. Called on initialization of window. 
	 */
	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		generalOptions = new GeneralOptions();
		setGeneralOptions(generalOptions); // set to defaults
		
		ipTextArea.getTextAreaContentListeners().add(ipChangeAction);
		showIdsNotNames.getButtonStateListeners().add(showIdNotNameChangeAction);
		debugMode.getButtonStateListeners().add(debugModeChangeAction);
	}

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
		
		if (generalOptions.isShowIdNotName())
			showIdsNotNames.setState(State.SELECTED);
		else
			showIdsNotNames.setState(State.UNSELECTED);
		
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
	 * Listener for showIdsNotNames checkbox.
	 */
	private ButtonStateListener showIdNotNameChangeAction = new ButtonStateListener() {
		@Override
		public void stateChanged(Button arg0, State state) {
			generalOptions.setShowIdNotName(state.equals(State.SELECTED));
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
}