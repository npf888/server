package com.netherfire.server.vo;

import java.util.Map;

/**
 * 短信验证码
 * @author JavaServer
 *
 */
public class PhoneNum {

	private String phoneNum;
	private String code;

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

	
	public void setValue(Map<String,String> map){
		if(map.get("phoneNum") != null){
			this.setPhoneNum(map.get("phoneNum"));
		}
		if(map.get("code") != null){
			this.setCode(map.get("code"));
		}
	}
	
	
}
