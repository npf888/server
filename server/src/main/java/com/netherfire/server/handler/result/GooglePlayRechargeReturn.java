package com.netherfire.server.handler.result;

public class GooglePlayRechargeReturn {

	private String kind;
    private String purchaseTimeMillis;
    private String developerPayload;
    private int purchaseState;
    private int consumptionState;
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
	public String getDeveloperPayload() {
		return developerPayload;
	}
	public void setDeveloperPayload(String developerPayload) {
		this.developerPayload = developerPayload;
	}
	public int getPurchaseState() {
		return purchaseState;
	}
	public void setPurchaseState(int purchaseState) {
		this.purchaseState = purchaseState;
	}
	public int getConsumptionState() {
		return consumptionState;
	}
	public void setConsumptionState(int consumptionState) {
		this.consumptionState = consumptionState;
	}
    
    


}
