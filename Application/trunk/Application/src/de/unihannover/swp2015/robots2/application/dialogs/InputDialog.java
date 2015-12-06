package de.unihannover.swp2015.robots2.application.dialogs;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Dialog;
import org.apache.pivot.wtk.TextArea;

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
