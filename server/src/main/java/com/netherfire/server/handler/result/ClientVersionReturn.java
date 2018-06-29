package com.netherfire.server.handler.result;

public class ClientVersionReturn {

	private String version;
    private int resourceVersion;
    private String minVersion;
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public int getResourceVersion() {
		return resourceVersion;
	}
	public void setResourceVersion(int resourceVersion) {
		this.resourceVersion = resourceVersion;
	}
	public String getMinVersion() {
		return minVersion;
	}
	public void setMinVersion(String minVersion) {
		this.minVersion = minVersion;
	}
    
    
}
