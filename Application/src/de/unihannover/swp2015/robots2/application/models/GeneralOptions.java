package de.unihannover.swp2015.robots2.application.models;

public class GeneralOptions {
	private String remoteUrl;
	private int remotePort;
	private boolean showIdNotName; 
	private boolean inDebugMode;
	
	public GeneralOptions() {
		super();
		this.remoteUrl = "tcp://127.0.0.1";
		this.remotePort = 1883;
		this.showIdNotName = false;
		this.inDebugMode = true;
	}
	
	public String getRemoteUrl() {
		return remoteUrl;
	}
	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}
	public int getRemotePort() {
		return remotePort;
	}
	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}
	public boolean isShowIdNotName() {
		return showIdNotName;
	}
	public void setShowIdNotName(boolean showIdNotName) {
		this.showIdNotName = showIdNotName;
	}
	public boolean isInDebugMode() {
		return inDebugMode;
	}
	public void setInDebugMode(boolean inDebugMode) {
		this.inDebugMode = inDebugMode;
	}
}
