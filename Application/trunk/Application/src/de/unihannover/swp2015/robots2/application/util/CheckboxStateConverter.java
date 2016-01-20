package de.unihannover.swp2015.robots2.application.util;

import org.apache.pivot.wtk.Button.State;
import org.apache.pivot.wtk.Checkbox;

public class CheckboxStateConverter {
	public static void setChecked(Checkbox box, boolean checked) {
		if (checked)
			box.setState(State.SELECTED);
		else
			box.setState(State.UNSELECTED);
	}
}
