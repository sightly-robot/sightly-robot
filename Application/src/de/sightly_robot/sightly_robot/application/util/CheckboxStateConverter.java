package de.sightly_robot.sightly_robot.application.util;

import org.apache.pivot.wtk.Button.State;
import org.apache.pivot.wtk.Checkbox;

public class CheckboxStateConverter {
	public static void setChecked(Checkbox box, boolean checked) {
		if (checked)
			box.setState(State.SELECTED);
		else
			box.setState(State.UNSELECTED);
	}
	
	public static boolean isChecked(Checkbox box) {
		if (box.getState().equals(State.SELECTED))
			return true;
		else
			return false; // MIXED = false
	}
}
