package de.unihannover.swp2015.robots2.application.dialogs;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.List;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Dialog;
import org.apache.pivot.wtk.ListView;

/**
 * Dialog class for choosing elements from a table
 * @author Tim Ebbeke
 */
public class ListDialog extends Dialog implements Bindable {

	private @BXML ListView list;
	private List <String> elements;
	
	/**
	 * BXML Bindable
	 */
	@Override
	public void initialize(Map<String, Object> arg0, URL arg1, Resources arg2) {		
	}
	
	public void setListElements(List <String> elements) {
		this.elements = elements; 
		list.setListData(elements);
	}
	
	public int getSelectedIndex() {
		return list.getSelectedIndex();
	}
	
	public String getSelectedElement() {
		return elements.get(list.getSelectedIndex());
	}
}
