package com.netherfire.server.server.pojo;

/**
 * 谷歌刷新数据
 * @author 郭君伟
 *
 */
public class GooglePlayCache {

	private int googleId;
	
	private String accessToken;
	
	private long expiresIn;

	public int getGoogleId() {
		return googleId;
	}

	public void setGoogleId(int googleId) {
		this.googleId = googleId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}
	
	
}
