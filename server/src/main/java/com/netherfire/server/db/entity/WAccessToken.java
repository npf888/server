package com.netherfire.server.db.entity;

public class WAccessToken {
	private Integer id;
	private String code;
	private String access_token;
	private Long expires_in;
	private String refresh_token;
	private String openid;
	private String scope;
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public Long getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(Long expires_in) {
		this.expires_in = expires_in;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	@Override
	public String  toString(){
		return "id="+id+" --- accessToken="+access_token+" --- expiresIn="+expires_in
				       +" --- refreshToken="+refresh_token+" --- openid="+openid
				       +" --- scope="+scope;
	}
}
