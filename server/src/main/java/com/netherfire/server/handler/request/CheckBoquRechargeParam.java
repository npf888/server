package com.netherfire.server.handler.request;


public class CheckBoquRechargeParam {
	//博趣订单ID
	private String channelOrderId;
	//咱们的订单ID
	private String orderId;
	//咱们的订单 对应的 产品的ID productId
	private String productId;
	//用户ID
	private String userId;
	
	
	public String getChannelOrderId() {
		return channelOrderId;
	}
	public void setChannelOrderId(String channelOrderId) {
		this.channelOrderId = channelOrderId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

}
