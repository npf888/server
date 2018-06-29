package com.netherfire.server.handler.result;

public class LoginReturn {

	private long userId;
    private String sdkId;
    private String userCode;
    private String ip;
    private int port;
    
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getSdkId() {
		return sdkId;
	}
	public void setSdkId(String sdkId) {
		this.sdkId = sdkId;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
    
    
}
