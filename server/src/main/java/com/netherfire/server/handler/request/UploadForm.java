package com.netherfire.server.handler.request;

/**
 * 上传图片数据
 * @author 郭君伟
 *
 */
public class UploadForm {

	private long userId;
	
	private String userCode;
	
	private byte[] content;

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

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
	
	
}
