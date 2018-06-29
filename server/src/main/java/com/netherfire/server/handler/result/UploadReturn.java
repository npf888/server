package com.netherfire.server.handler.result;

public class UploadReturn {

    private long userId;
	
	private String img;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
}
