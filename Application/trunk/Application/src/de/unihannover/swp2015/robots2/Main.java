package de.unihannover.swp2015.robots2;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;

import de.unihannover.swp2015.robots2.controller.main.GuiMainController;
import de.unihannover.swp2015.robots2.windows.ControlPanel;

public class Main implements Application {
	
	private ControlPanel window;
	private GuiMainController controller;
	
	/**
	 * Application entry function main.
	 * 
	 * @param args Program arguments
	 */
	public static void main(String[] args) {
	    DesktopApplicationContext.main(Main.class, args);
	}
	
	/**
	 * The startup method is part of the main Window and called after program start.
	 * Usually used to construct the GUI.
	 * 
	 * @param display An Apache Pivot display 
	 */
	@Override
    public void startup(Display display, Map<String, String> properties) throws Exception {		
        BXMLSerializer bxmlSerializer = new BXMLSerializer();
        bxmlSerializer.getNamespace().put("application", this);
        window = (ControlPanel)bxmlSerializer.readObject(getClass().getResource("/de/unihannover/swp2015/robots2/ControlPanel.bxml"));

	    this.controller = new GuiMainController();
        window.setController(controller);
        window.setPreferredSize(900, 600);
        window.open(display);
    }
 
    @Override
    public boolean shutdown(boolean optional) {
        if (window != null)
            window.close();
 
        return false;
    }
 
    @Override
    public void suspend() {
    }
 
    @Override
    public void resume() {
    }
}
