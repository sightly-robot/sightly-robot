package de.unihannover.swp2015.robots2.application.windows;

import java.awt.Color;
import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.TabPane;
import org.apache.pivot.wtk.TextArea;
import org.apache.pivot.wtk.TextArea.Paragraph;
import org.apache.pivot.wtk.TextAreaContentListener;
import org.apache.pivot.wtk.Window;

import de.unihannover.swp2015.robots2.application.models.GeneralOptions;

public class Configurator extends Window implements Bindable
{
	// BXML bindings
	@BXML private TabPane tabs;
	@BXML private TextArea ipTextArea;
	@BXML private TextArea portTextArea;
	
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
		portTextArea.getTextAreaContentListeners().add(portChangeAction);
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
		portTextArea.setText(Integer.toString(generalOptions.getRemotePort()));
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
	
	private TextAreaContentListener portChangeAction = new TextAreaContentListener() {
		@Override
		public void paragraphInserted(TextArea self, int arg1) {}

		@Override
		public void paragraphsRemoved(TextArea self, int arg1, Sequence<Paragraph> arg2) {}

		@Override
		public void textChanged(TextArea self) {
			if (!self.getText().matches("(?:\\b6553[0-5]|655[0-2](?:[0-9]){1}|65[0-4](?:[0-9]){2}|6[0-4](?:[0-9]){3}|[1-5](?:[0-9]){4}|(?:[0-9]){4}|(?:[0-9]){3}|(?:[0-9]){2}|(?:[0-9]){1}\\b)")) {
				// not a valid port
				self.getStyles().put("backgroundColor", new Color(246, 66, 66));
			} 
			else {
				self.getStyles().put("backgroundColor", Color.WHITE);
				generalOptions.setRemotePort(Integer.parseInt(self.getText()));
			}
		}
	};
}