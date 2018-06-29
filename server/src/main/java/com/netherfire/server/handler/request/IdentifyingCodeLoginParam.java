package com.netherfire.server.handler.request;

import java.util.Map;

public class IdentifyingCodeLoginParam {

	
	private String phoneNum;
	private String code;
	
	private String deviceMac;
	private String deviceId;
	private String macAddress;
	private String androidId;
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDeviceMac() {
		return deviceMac;
	}
	public void setDeviceMac(String deviceMac) {
		this.deviceMac = deviceMac;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public String getAndroidId() {
		return androidId;
	}
	public void setAndroidId(String androidId) {
		this.androidId = androidId;
	}
	
	public void setValue(Map<String,String> map){
		if(map.get("phoneNum") != null){
			this.setPhoneNum(String.valueOf(map.get("phoneNum")));
		}
		if(map.get("code") != null){
			this.setCode(String.valueOf(map.get("code")));
		}
		if(map.get("deviceMac") != null){
			this.setDeviceMac(String.valueOf(map.get("deviceMac")));
		}
		if(map.get("deviceId") != null){
			this.setDeviceId(String.valueOf(map.get("deviceId")));
		}
		if(map.get("macAddress") != null){
			this.setMacAddress(String.valueOf(map.get("macAddress")));
		}
		if(map.get("androidId") != null){
			this.setAndroidId(String.valueOf(map.get("androidId")));
		}
		
	}
}
