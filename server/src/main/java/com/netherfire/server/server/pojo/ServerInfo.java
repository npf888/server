package com.netherfire.server.server.pojo;

public class ServerInfo {

	private int status;
	private String ip;
	private String serverId;
	private int port;
	private int nums;
	private int maxNums;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getNums() {
		return nums;
	}
	public void setNums(int nums) {
		this.nums = nums;
	}
	public int getMaxNums() {
		return maxNums;
	}
	public void setMaxNums(int maxNums) {
		this.maxNums = maxNums;
	}
	
	
}
