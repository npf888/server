package com.netherfire.server.db.entity;

public class IndexNotice {
	  
	  private long id;
	  private String cn;
	  private String en;
	  private String tw;
	  private int open;
	  private long updateTime;
	  private long createTime;
	  
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCn() {
		return cn;
	}
	public void setCn(String cn) {
		this.cn = cn;
	}
	public String getEn() {
		return en;
	}
	public void setEn(String en) {
		this.en = en;
	}
	public String getTw() {
		return tw;
	}
	public void setTw(String tw) {
		this.tw = tw;
	}
	public int getOpen() {
		return open;
	}
	public void setOpen(int open) {
		this.open = open;
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
