package com.netherfire.server.db.entity;

public class ClientVersion {

	private long id;
	private String version;
	private String minVersion;
	private long updateTime;
	private long createTime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getMinVersion() {
		return minVersion;
	}
	public void setMinVersion(String minVersion) {
		this.minVersion = minVersion;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	
}
