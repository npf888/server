package com.netherfire.server.db.entity;
/**
 * 用来缓存 access_token (这个access_token可不是 拉取用户的 access_token)
 * @author JavaServer
 * 
 * 1、先通过 appid 和 appsecret 获取 access_token
 * 2、再通过 access_token 获取 jsapi_ticket
 * 3、 在 把 jsapi_ticket 和 时间戳   url 和字符串生成   签名
 */
public class MShareUser {

	private Integer id;
	private String open_id;
	private String access_token;
	private Long token_expires_in;//token过期时间  秒
	private String jsapi_ticket;
	private Long ticket_expires_in;//票 过期时间 秒
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOpen_id() {
		return open_id;
	}
	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public Long getToken_expires_in() {
		return token_expires_in;
	}
	public void setToken_expires_in(Long token_expires_in) {
		this.token_expires_in = token_expires_in;
	}
	public String getJsapi_ticket() {
		return jsapi_ticket;
	}
	public void setJsapi_ticket(String jsapi_ticket) {
		this.jsapi_ticket = jsapi_ticket;
	}
	public Long getTicket_expires_in() {
		return ticket_expires_in;
	}
	public void setTicket_expires_in(Long ticket_expires_in) {
		this.ticket_expires_in = ticket_expires_in;
	}
	
	
	
	
	
}
