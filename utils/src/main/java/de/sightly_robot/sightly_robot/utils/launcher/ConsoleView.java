package de.sightly_robot.sightly_robot.utils.launcher;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.TextArea;
import org.apache.pivot.wtk.Window;

public class ConsoleView extends Window implements Bindable {

	@BXML TextArea out;
	
	@Override
	public void initialize(Map<String, Object> arg0, URL arg1, Resources arg2) {
		//annotation does the job
	}

	public TextArea getOutputArea() {
		return out;
	}
}
