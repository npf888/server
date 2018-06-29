package com.netherfire.server.handler.request;

public class GooglePlayRechargeForm {
	
	private int googleId;
	private String packageName;
	private String productId;
	private String token;
	
	public int getGoogleId() {
		return googleId;
	}
	public void setGoogleId(int googleId) {
		this.googleId = googleId;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
}
