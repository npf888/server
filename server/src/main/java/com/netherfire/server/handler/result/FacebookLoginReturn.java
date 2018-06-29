package com.netherfire.server.handler.result;

public class FacebookLoginReturn {
	
	private long userId;
    private String fbId;
    private String userCode;
    private String ip;
    private int port;
    private int newFbId;
    
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getFbId() {
		return fbId;
	}
	public void setFbId(String fbId) {
		this.fbId = fbId;
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
	public int getNewFbId() {
		return newFbId;
	}
	public void setNewFbId(int newFbId) {
		this.newFbId = newFbId;
	}
}
