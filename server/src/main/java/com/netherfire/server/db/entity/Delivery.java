package com.netherfire.server.db.entity;

/**
 * 发货数据
 * @author 郭君伟
 *
 */
public class Delivery {

	  private long id;
	  private String amount;
	  private String extraparam1;
	  private String time;
	  private String sign;
	  private String roleid;
	  private String gold;
	  private String merchantref;
	  private String zoneid;
	  private String pay_type;
	  
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getExtraparam1() {
		return extraparam1;
	}
	public void setExtraparam1(String extraparam1) {
		this.extraparam1 = extraparam1;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public String getGold() {
		return gold;
	}
	public void setGold(String gold) {
		this.gold = gold;
	}
	public String getMerchantref() {
		return merchantref;
	}
	public void setMerchantref(String merchantref) {
		this.merchantref = merchantref;
	}
	public String getZoneid() {
		return zoneid;
	}
	public void setZoneid(String zoneid) {
		this.zoneid = zoneid;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	  
	  
}
