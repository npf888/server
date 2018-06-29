package com.netherfire.server.handler.request;

/**
 * 验证登陆
 * @author 郭君伟
 *
 */
public class CheckLoginForm {
	
	private long userId;
	
	private String userCode;
	
	private int serverId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	
	
}
