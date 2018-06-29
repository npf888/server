package com.netherfire.server.handler.request;
/**
 * 
 * 博趣 查询订单返回
 * @author JavaServer
 *
 */
public class CheckBoquRechargeReturn {
	//订单ID
	private String order_id;
	//NULL
    private String pay_data;
    //订单标题
    private String title;
    //价格/分
    private String price;
    //类型
    private String type;
    //0正式，1沙盒服务端需要自行处理沙盒订单
    private int sandbox;
    /*
     * 状态,见备注  
     * init     待支付 
     * sync     付款成功,通知给服务端异常,等待再次同步
     * complete 成功 
     * refund   已退款
     */
    private String status;
    //创建时间
    private String created_at;
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getPay_data() {
		return pay_data;
	}
	public void setPay_data(String pay_data) {
		this.pay_data = pay_data;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getSandbox() {
		return sandbox;
	}
	public void setSandbox(int sandbox) {
		this.sandbox = sandbox;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
    
    
    
}
