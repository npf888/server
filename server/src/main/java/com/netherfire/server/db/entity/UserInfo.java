package com.netherfire.server.db.entity;

/**
 * 
 * @author GuoJunWei
 *
 */
public class UserInfo {
	
	  /**登陆标识**/
	 private long id;
	 /**昵称 **/
	 private String name;
	 /**用户名**/
	 private String username;
	 /**密码 **/
	 private String password;
	 /**渠道(相同的渠道用户名不能重复，不同的渠道用户名可以重复) **/
	 private String channel;
	 
	 /**博趣 平台的 token_type **/
	 private String tokenType;
	 /**博趣 平台的 access_token（这个平台的唯一标识） **/
	 private String accessToken;
	 /**博趣 平台的  token 过期时间(刷新token时 的当前时间+过期的时间段） **/
	 private Long expiresIn;
	 /**账号id **/
	 private String accountId;
	 /**facebookid **/
	 private String facebookId;
	 
	 /**twitterId **/
	 private String twitterId;
	 
	 /**图像url **/
	 private String img;
	 /**帐号创建时间 **/
	 private long joinTime;
	 /**上次登陆时间 **/
	 private long lastLoginTime;
	 /**权限 **/
	 private int role;
	 /**锁定状态 **/
	 private int lockStatus;
	 /**锁定时间 **/
	 private int muteTime;
	 /**锁定原因 **/
	 private String muteReason;
	 /**用户的手机号 **/
	 private String phoneNum;
	 /**设备MAC地址 **/
	 private String deviceMac;
	 /**设备id**/
	 private String deviceId;
	 /**mac地址 **/
	 private String macAddress;
	 /**安卓ID **/
	 private String androidId;
	 /*需要同步facebook信息到gameserver*/
	 private boolean updateFbInfo;
	 
	 
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getFacebookId() {
		return facebookId;
	}
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public long getJoinTime() {
		return joinTime;
	}
	public void setJoinTime(long joinTime) {
		this.joinTime = joinTime;
	}
	public long getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public int getLockStatus() {
		return lockStatus;
	}
	public void setLockStatus(int lockStatus) {
		this.lockStatus = lockStatus;
	}
	public int getMuteTime() {
		return muteTime;
	}
	public void setMuteTime(int muteTime) {
		this.muteTime = muteTime;
	}
	public String getMuteReason() {
		return muteReason;
	}
	public void setMuteReason(String muteReason) {
		this.muteReason = muteReason;
	}
	public String getDeviceMac() {
		return deviceMac;
	}
	public void setDeviceMac(String deviceMac) {
		this.deviceMac = deviceMac;
	}
	public String getTwitterId() {
		return twitterId;
	}
	public void setTwitterId(String twitterId) {
		this.twitterId = twitterId;
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
	public boolean isUpdateFbInfo() {
		return updateFbInfo;
	}
	public void setUpdateFbInfo(boolean updateFbInfo) {
		this.updateFbInfo = updateFbInfo;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public Long getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	
	 
	  
	
}
