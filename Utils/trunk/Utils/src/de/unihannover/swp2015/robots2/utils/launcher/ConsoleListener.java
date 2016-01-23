package de.unihannover.swp2015.robots2.utils.launcher;

import org.apache.pivot.wtk.DesktopApplicationContext.DisplayListener;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Window;

public class ConsoleListener implements DisplayListener{

	private Window console;
	
	public ConsoleListener(Window console) {
		super();
		this.console = console;
	}
	
	@Override
	public void hostWindowClosed(Display display) {
		console.close();
	}

	@Override
	public void hostWindowOpened(Display display) {
		console.open(display);
	}
	
}
