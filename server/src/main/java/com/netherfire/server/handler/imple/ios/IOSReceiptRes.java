package com.netherfire.server.handler.imple.ios;

public class IOSReceiptRes {

	
	private int status;
    private String environment;
    private String latest_receipt;
    private String latest_receipt_info;
    private IOSReceipt receipt;
    
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String getLatest_receipt() {
		return latest_receipt;
	}
	public void setLatest_receipt(String latest_receipt) {
		this.latest_receipt = latest_receipt;
	}
	public String getLatest_receipt_info() {
		return latest_receipt_info;
	}
	public void setLatest_receipt_info(String latest_receipt_info) {
		this.latest_receipt_info = latest_receipt_info;
	}
	public IOSReceipt getReceipt() {
		return receipt;
	}
	public void setReceipt(IOSReceipt receipt) {
		this.receipt = receipt;
	}
    
    
}
