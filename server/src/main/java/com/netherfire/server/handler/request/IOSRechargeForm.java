package com.netherfire.server.handler.request;

public class IOSRechargeForm {

	private long orderId;
	private String receiptData;
	private boolean sandbox;
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public String getReceiptData() {
		return receiptData;
	}
	public void setReceiptData(String receiptData) {
		this.receiptData = receiptData;
	}
	public boolean isSandbox() {
		return sandbox;
	}
	public void setSandbox(boolean sandbox) {
		this.sandbox = sandbox;
	}
	
	
}
