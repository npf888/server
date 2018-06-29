package com.netherfire.server.manager.pojo;

import com.netherfire.server.db.entity.UserInfo;

/**
 * 角色缓存数据
 * @author GuoJunWei
 *
 */
public class CacheUserInfo {
	
	private String userCode;
	
	private String ip;
	
	private int port;
	
	private int serverId;
	
	private UserInfo userInfo;

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

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	
	
	
}
