package com.netherfire.server.handler.result;

public class IOSRechargeReturn {
	
	private String kind;
	private String purchaseTimeMillis;
	private String productId;
	private String developerPayload;
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getPurchaseTimeMillis() {
		return purchaseTimeMillis;
	}
	public void setPurchaseTimeMillis(String purchaseTimeMillis) {
		this.purchaseTimeMillis = purchaseTimeMillis;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getDeveloperPayload() {
		return developerPayload;
	}
	public void setDeveloperPayload(String developerPayload) {
		this.developerPayload = developerPayload;
	}
	
	
}
