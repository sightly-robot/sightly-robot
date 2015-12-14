package de.unihannover.swp2015.robots2.application.dialogs;

import java.io.IOException;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.wtk.DialogCloseListener;
import org.apache.pivot.wtk.Window;

public class DialogFactory {
	public static InputDialog createInputDialog(Window owner, DialogCloseListener closeListener) {
        BXMLSerializer bxmlSerializer = new BXMLSerializer();
        InputDialog dialog = null;
		try {
			dialog = (InputDialog)bxmlSerializer.readObject(DialogFactory.class.getResource("/de/unihannover/swp2015/robots2/application/bxml/InputDialog.bxml"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SerializationException e) {
			e.printStackTrace();
		}
		dialog.setModal(true);
		dialog.open(owner, closeListener);
		return dialog;
	}
}
