package de.unihannover.swp2015.robots2.utils.launcher;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Window;

public class Main implements Application {

	private Launcher window = null;

	@Override
	public void resume() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean shutdown(boolean arg0) throws Exception {

		if (window != null) {
			window.killAllProcess();
			window.close();
		}

		return false;
	}

	@Override
	public void startup(Display display, Map<String, String> arg1)
			throws Exception {
		BXMLSerializer bxmlSerializer = new BXMLSerializer();
		window = (Launcher) bxmlSerializer.readObject(getClass().getResource(
				"/de/unihannover/swp2015/robots2/utils/launcher/bxml/launcher.bxml"));
		window.open(display);

	}

	@Override
	public void suspend() throws Exception {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		DesktopApplicationContext.main(Main.class, args);
	}

}
