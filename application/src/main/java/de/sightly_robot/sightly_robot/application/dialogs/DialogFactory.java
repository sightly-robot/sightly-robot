package de.sightly_robot.sightly_robot.application.dialogs;

import java.io.IOException;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.wtk.Dialog;
import org.apache.pivot.wtk.DialogCloseListener;
import org.apache.pivot.wtk.Window;

import de.sightly_robot.sightly_robot.application.models.GeneralOptions;
import de.sightly_robot.sightly_robot.application.models.ListDialogModel;
import de.sightly_robot.sightly_robot.model.interfaces.IRobot;

public class DialogFactory {
	public static InputDialog createInputDialog(Window owner, DialogCloseListener closeListener) {
		return DialogFactory.<InputDialog> createDialog (
			owner,
			closeListener,
			"/de/unihannover/swp2015/robots2/application/bxml/InputDialog.bxml"
		);		
	}
	
	public static ListDialog createListDialog(Window owner, DialogCloseListener closeListener,
											  GeneralOptions options,
											  java.util.List<IRobot> robots) {
		ListDialog ld = DialogFactory.<ListDialog> createDialog (
			owner,
			closeListener,
			"/de/unihannover/swp2015/robots2/application/bxml/ListDialog.bxml"
		);
		
		ld.setListElements(new ListDialogModel(options, robots));
		
		return ld;
	}
	
	@SuppressWarnings("unchecked")
	private static <DialogT extends Dialog> DialogT createDialog(Window owner, DialogCloseListener closeListener, String bxml) {
		BXMLSerializer bxmlSerializer = new BXMLSerializer();
		DialogT dialog = null;
		try {
			dialog = (DialogT)bxmlSerializer.readObject(DialogFactory.class.getResource(bxml));
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
