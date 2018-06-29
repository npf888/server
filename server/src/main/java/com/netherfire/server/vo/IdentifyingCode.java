package com.netherfire.server.vo;
/**
 * 短信验证码
 * @author JavaServer
 *
 */
public class IdentifyingCode {

	private String code;
	private long overdueTime;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getOverdueTime() {
		return overdueTime;
	}

	public void setOverdueTime(long overdueTime) {
		this.overdueTime = overdueTime;
	}
	
	@Override
	public String toString(){
		return "验证码code:"+code+"---过期时间："+overdueTime;
	}
	
}
