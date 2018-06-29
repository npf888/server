package com.netherfire.server.vo;

public class BoquWeixinUser {
	//{"id":541,
			//"username":"ocFYhv1XH_WWOtwVyQKwDHt_p2Cc",
			//"nickname":"\u5927\u98de",
			//"email":null,"created_at":"2018-03-22 11:29:25",
			//"updated_at":"2018-03-22 11:29:25","mobile":"",
			//"client_id":8,
			//"open_id":null,
			//"status":0,
			//"coin":100,"ticket":0,"name":null,"id_card":"",
			//"avatar":"http:\/\/www.bo-qu.com\/images\/avatars\/541\/1.jpeg"}
	
	
	
	private Integer id;
	private String username;
	private String nickname;
	private String email;
	private String created_at;
	private String updated_at;
	private String mobile;
	private Integer client_id;
	private String open_id;
	private Integer status;
	private String coin;
	private Integer ticket;
	private String name;
	private Integer id_card;
	private String avatar;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getClient_id() {
		return client_id;
	}
	public void setClient_id(Integer client_id) {
		this.client_id = client_id;
	}
	public String getOpen_id() {
		return open_id;
	}
	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCoin() {
		return coin;
	}
	public void setCoin(String coin) {
		this.coin = coin;
	}
	public Integer getTicket() {
		return ticket;
	}
	public void setTicket(Integer ticket) {
		this.ticket = ticket;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId_card() {
		return id_card;
	}
	public void setId_card(Integer id_card) {
		this.id_card = id_card;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	
	
	
}
