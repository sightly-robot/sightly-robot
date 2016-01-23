package de.unihannover.swp2015.robots2.utils.launcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.TextArea;
import org.apache.pivot.wtk.Window;

public class RobotConsole extends Window implements Bindable, Runnable {

	private Process process;
	
	private TextArea consoleOutput;
	private BufferedReader bf;
	
	@Override
	public void initialize(Map<String, Object> namespace, URL arg1, Resources arg2) {
		this.consoleOutput = (TextArea)namespace.get("consoleRobotOutput");
	}
	
	@Override
	public void run() {
		this.bf = new BufferedReader( new InputStreamReader(process.getInputStream()) );
		
		while( true ) {
			this.update();
		}
	}
	
	private void update() {
		String output = null;
		try {
			output = this.bf.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if( output != null && output.length() > 0 ) {
			//this.consoleOutput.setText(this.consoleOutput.getText() + output);
			System.out.println(output);
		}
	}
	
	public void setProcess(Process process) {
		this.process = process;
		
	}
}
