package de.unihannover.swp2015.robots2.models;

public class GeneralOptions {
	private String remoteUrl;
	private int remotePort;
	
	public GeneralOptions() {
		super();
		this.remoteUrl = "localhost";
		this.remotePort = 0;
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
}
