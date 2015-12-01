package de.unihannover.swp2015.robots2.windows;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.TabPane;
import org.apache.pivot.wtk.Window;
import org.apache.pivot.wtk.skin.terra.TerraTabPaneSkin;

import de.unihannover.swp2015.robots2.models.GeneralOptions;

public class Configurator extends Window implements Bindable
{
	// BXML bindings
	@BXML private TabPane tabs;
	
	// Models
	GeneralOptions generalOptions;
	
	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		generalOptions = new GeneralOptions();
	}

	public GeneralOptions getGeneralOptions() {
		return generalOptions;
	}

	public void setGeneralOptions(GeneralOptions gneralOptions) {
		this.generalOptions = gneralOptions;
	}	
}