package com.netherfire.server.handler;

public class WeixinUtils {

	public static final String AppID = "wx8f98ce73361b03fc";
	public static final String AppSecret = "4a044704faa08f5910488fa4c2705cd8";
	
	/**
	 *  拉取用户用到的 URL
	 */
	// 1、 通过用户授权
	//获取code 的 url
	public static final String AuthorizeURL = "https://open.weixin.qq.com/connect/oauth2/authorize";
//	public static final String Redirect_URL = "http://192.168.1.41:8080/shopview/api/wexin/index/indexNew.html";
	public static final String Redirect_URL = "http://dice.bo-qu.com";
	
	// 2、获取网页的token_access
	public static final String ObtainAccessToken = "https://api.weixin.qq.com/sns/oauth2/access_token";
	
	// 3、刷新网页的token_access
	public static final String RefreshAccessToken = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
	
	// 3-1、检验用户信息是否有效
	public static final String CheckAccessToken = "https://api.weixin.qq.com/sns/auth";
	
	// 4、获取用户的信息
	public static final String UserInfo = "https://api.weixin.qq.com/sns/userinfo";
	
	
	/**
	 * 分享用到的URL
	 */
	//获取token 
	public static final String ShareAccessToken = "https://api.weixin.qq.com/cgi-bin/token";
	
	//获取jsapi_ticket 
	public static final String ShareJsapiTicket = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
	
	
	/**
	 * 博趣的微信登录
	 */
	public static final String BoquWeixinURL = "http://www.bo-qu.com/console/oauth/official-account";
	
}
