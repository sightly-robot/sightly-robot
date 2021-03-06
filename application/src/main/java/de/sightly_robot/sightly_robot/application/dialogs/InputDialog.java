package de.sightly_robot.sightly_robot.application.dialogs;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Dialog;
import org.apache.pivot.wtk.TextArea;

/**
 * Dialog for inputting a string.
 * @author Tim Ebbeke
 */
public class InputDialog extends Dialog implements Bindable {
	@BXML private TextArea textBox; 
	
	public String getText() {
		return textBox.getText();
	}

	@Override
	public void initialize(Map<String, Object> arg0, URL arg1, Resources arg2) {
		// TODO Auto-generated method stub		
	}
}
