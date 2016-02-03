package de.unihannover.swp2015.robots2.application.dialogs;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Dialog;
import org.apache.pivot.wtk.ListView;
import org.apache.pivot.wtk.PushButton;

import de.unihannover.swp2015.robots2.application.models.ListDialogModel;

/**
 * Dialog class for choosing elements from a table
 * @author Tim Ebbeke
 */
public class ListDialog extends Dialog implements Bindable {

	private @BXML ListView list;
	private @BXML PushButton okButton;
	
	private ListDialogModel elements;
	
	/**
	 * BXML Bindable
	 */
	@Override
	public void initialize(Map<String, Object> arg0, URL arg1, Resources arg2) {
		okButton.getButtonPressListeners().add(closeAction);
	}
	
	/**
	 * Close Button handler. Closes the window.
	 */
	private ButtonPressListener closeAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button button) {
			close();
		}
	};
	
	/**
	 * Sets a model for the ListDialog.
	 * @param elements A list of ListDialog elements.
	 */
	public void setListElements(ListDialogModel elements) {
		this.elements = elements; 
		list.setListData(elements.getPrintables());
	}
	
	/**
	 * Retrieves the selection index of the item.
	 * @return Selection index of the list dialog.
	 */
	public int getSelectedIndex() {
		return list.getSelectedIndex();
	}
	
	/**
	 * Retrieves the selected item of the dialog.
	 * @return Selected item.
	 */
	public String getSelectedElement() {
		return elements.getRobotIds().get(getSelectedIndex());
	}
}
