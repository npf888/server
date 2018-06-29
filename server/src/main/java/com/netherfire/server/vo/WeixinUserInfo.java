package com.netherfire.server.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信的用户对象
 * @author JavaServer
 *
 */
public class WeixinUserInfo {

	
	private String openid;
	private String nickname;
	private String sex;
	private String province;
	private String city;
	private String country;
	private String headimgurl;
	private List<String> privilege = new ArrayList<String>();
	private String unionid;
	
	
	
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public List<String> getPrivilege() {
		return privilege;
	}
	public void setPrivilege(List<String> privilege) {
		this.privilege = privilege;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	
	@Override
	public String toString(){
		
		String str = " openid::"+ openid
			   +" nickname::"+nickname
			   +" sex::"+sex
			   +" province::"+province
			   +" city::"+city
			   +" country::"+country
			   +" headimgurl::"+headimgurl;
		for(String ss :privilege){
			str+="privilege::"+ss;
		}
		str+=" unionid::"+unionid;
		return str;
	}
	
	
}
