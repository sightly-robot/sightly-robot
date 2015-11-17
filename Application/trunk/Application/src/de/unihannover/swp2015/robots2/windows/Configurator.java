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

public class Configurator extends Window implements Bindable
{
	@BXML private TabPane tabs;
	
	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
	}	
}