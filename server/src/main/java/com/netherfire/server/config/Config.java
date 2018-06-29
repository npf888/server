package com.netherfire.server.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.netherfire.server.config.pojo.BoquPay;
import com.netherfire.server.config.pojo.GooglePay;
import com.netherfire.server.config.pojo.IosPay;

/**
 * 配置数据
 * @author 郭君伟
 *
 */
public class Config {
	private static final Logger logger = LogManager.getLogger(Config.class);
	
	/**接听端口号 **/
	private int port;
	/**异步执行线程数 **/
	private int threadCount;
	/**下载资源路径 **/
	private String loadPath;
	/**图片上传路径 **/
	private String updatePath;
	/**微信 图片上传路径 **/
	private String updateWeixinPath;
	
	/**默认GameView 开始ID **/
	private long def_delivery_ID;
	
	/**默认ID **/
	private long userinfo_maxi_id;
	
	/**微信登录的时候会重定向 到 某个接口，然后接口会返回 一条 javascript 代码，这段代码 就是用于跳转的，下面的 url就是要跳转的页面**/
	private String weixinRedirectUrl;
	/**博趣微信登录的 重定向 url **/
	private String boquWeixinRedirectUrl;
	/**调用博趣的接口 发送验证码 **/
	private String boquSendMsgUrl;
	
	/** true:绑定ip地址**/
	private boolean isBindIp;
	/** IP **/
	private String bindingIp;
	/** CDN 域名 **/
	private String cdnDomain;
	
	/**服务器列表Key **/
	private String redisServerKey = "servers";
	
	/**服务器状态   **/
	public   int serverStatusTest = 0;
	public   int serverStatusRun = 1;
	public   int serverStatusStop = 2;
	
	/** 人物类型 **/				
	public int NormalRole  = 0;
	public int GMRole  = 1;
	public int RobotRole  = 2;
	
	/**图片长度 **/
	public int defaultImages = 12;
	
	/**登陆服务器绑定IP **/
	public String serverIp;
	/**登陆服务器绑定IP **/
	public String proxyIP;
	/**登陆服务器绑定端口 **/
	public String proxyPort;
	
	public static final String  resourceClientVersionKey = "resource_client_version";
	public static final String  clientVersionKey         = "client_version";
    public static final String	clientMinVersionKey      = "client_min_version";
	public static final String	clientKey                = "client";
	
	
	/** 没有解锁 **/
	public static final int UNLOCK = 0;
	/** 解锁**/
	public static final int LOCK = 1;
	
	/**公告key **/
	public static final String	index_notice = "index_notice";
	
	/**IOS 支付相关 **/
	public static final int IOSPAYSTAT21007 = 21007;
	public static final int IOSPAYSTAT0 = 0;
	
	
	private Map<Integer,GooglePay> googlePayMap = new HashMap<Integer,GooglePay>();
	
	
	
	private IosPay iosPay = new IosPay();
	private BoquPay boquPay = new BoquPay();
	
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public String getLoadPath() {
		return loadPath;
	}

	public void setLoadPath(String loadPath) {
		this.loadPath = loadPath;
	}

	public long getUserinfo_maxi_id() {
		return userinfo_maxi_id;
	}

	public void setUserinfo_maxi_id(long userinfo_maxi_id) {
		this.userinfo_maxi_id = userinfo_maxi_id;
	}

	public long getDef_delivery_ID() {
		return def_delivery_ID;
	}

	public void setDef_delivery_ID(long def_delivery_ID) {
		this.def_delivery_ID = def_delivery_ID;
	}

	

	public String getRedisServerKey() {
		return redisServerKey;
	}

	public void setRedisServerKey(String redisServerKey) {
		this.redisServerKey = redisServerKey;
	}

	public int getServerStatusTest() {
		return serverStatusTest;
	}

	public void setServerStatusTest(int serverStatusTest) {
		this.serverStatusTest = serverStatusTest;
	}

	public int getServerStatusRun() {
		return serverStatusRun;
	}

	public void setServerStatusRun(int serverStatusRun) {
		this.serverStatusRun = serverStatusRun;
	}

	public int getServerStatusStop() {
		return serverStatusStop;
	}

	public void setServerStatusStop(int serverStatusStop) {
		this.serverStatusStop = serverStatusStop;
	}

	public int getNormalRole() {
		return NormalRole;
	}

	public void setNormalRole(int normalRole) {
		NormalRole = normalRole;
	}

	public int getGMRole() {
		return GMRole;
	}

	public void setGMRole(int gMRole) {
		GMRole = gMRole;
	}

	public int getRobotRole() {
		return RobotRole;
	}

	public void setRobotRole(int robotRole) {
		RobotRole = robotRole;
	}

	public int getDefaultImages() {
		return defaultImages;
	}

	public void setDefaultImages(int defaultImages) {
		this.defaultImages = defaultImages;
	}

	public Map<Integer, GooglePay> getGooglePayMap() {
		return googlePayMap;
	}

	public void setGooglePayMap(Map<Integer, GooglePay> googlePayMap) {
		this.googlePayMap = googlePayMap;
	}

	public IosPay getIosPay() {
		return iosPay;
	}

	public void setIosPay(IosPay iosPay) {
		this.iosPay = iosPay;
	}

	
	
	public String getUpdatePath() {
		return updatePath;
	}

	public void setUpdatePath(String updatePath) {
		this.updatePath = updatePath;
	}
	
	

	public boolean isBindIp() {
		return isBindIp;
	}

	public void setBindIp(boolean isBindIp) {
		this.isBindIp = isBindIp;
	}

	public String getBindingIp() {
		return bindingIp;
	}

	public void setBindingIp(String bindingIp) {
		this.bindingIp = bindingIp;
	}

	
	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public BoquPay getBoquPay() {
		return boquPay;
	}

	public void setBoquPay(BoquPay boquPay) {
		this.boquPay = boquPay;
	}

	public String getProxyIP() {
		return proxyIP;
	}

	public void setProxyIP(String proxyIP) {
		this.proxyIP = proxyIP;
	}

	public String getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getWeixinRedirectUrl() {
		return weixinRedirectUrl;
	}

	public void setWeixinRedirectUrl(String weixinRedirectUrl) {
		this.weixinRedirectUrl = weixinRedirectUrl;
	}
	
	

	public String getBoquWeixinRedirectUrl() {
		return boquWeixinRedirectUrl;
	}

	public void setBoquWeixinRedirectUrl(String boquWeixinRedirectUrl) {
		this.boquWeixinRedirectUrl = boquWeixinRedirectUrl;
	}

	public String getUpdateWeixinPath() {
		return updateWeixinPath;
	}

	public void setUpdateWeixinPath(String updateWeixinPath) {
		this.updateWeixinPath = updateWeixinPath;
	}

	public String getCdnDomain() {
		return cdnDomain;
	}

	public void setCdnDomain(String cdnDomain) {
		this.cdnDomain = cdnDomain;
	}


	public String getBoquSendMsgUrl() {
		return boquSendMsgUrl;
	}

	public void setBoquSendMsgUrl(String boquSendMsgUrl) {
		this.boquSendMsgUrl = boquSendMsgUrl;
	}

	public static void main(String[] args) {
		Config c = new Config();
		c.setPort(80);
		c.setThreadCount(50);
		c.setLoadPath("E:/load/");
		c.setDef_delivery_ID(10000);
		c.setUserinfo_maxi_id(10000);
		
		IosPay iosPay = new IosPay();
		iosPay.setProduction("aaa");
		iosPay.setSandbox("oooo");
		c.setIosPay(iosPay);
	
		GooglePay googlePay1 = new GooglePay();
		googlePay1.setGoogleId(1);
		googlePay1.setGooglePlayApi("https://www.googleapis.com/androidpublisher/v2/applications");
		googlePay1.setGooglePlayClientId("720633076003-dp68r3ifp4jvkpe905oru778f3v9glev.apps.googleusercontent.com");
		googlePay1.setGooglePlayClientSecret("_4Bf2cVK2SU-IsI_evn6EHmH");
		googlePay1.setGooglePlayCode("4/LI2HdXPLu3TzZRszPiHO_qSLCnyC8Xt01qNpSPyCKiE");
		googlePay1.setGooglePlayRedirectUri("urn:ietf:wg:oauth:2.0:oob");
		googlePay1.setGooglePlayTokenApi("https://accounts.google.com/o/oauth2/token");
		googlePay1.setGooglePlayRefreshToken("1/1vg9xqPHOL7ZpWBaoblb8AaIYjYdHwsPu-lWS8LBKIlIgOrJDtdun6zK6XiATCKT1111111111");
		
		
		GooglePay googlePay2 = new GooglePay();
		googlePay2.setGoogleId(3);
		googlePay2.setGooglePlayApi("https://www.googleapis.com/androidpublisher/v2/applications");
		googlePay2.setGooglePlayClientId("720633076003-dp68r3ifp4jvkpe905oru778f3v9glev.apps.googleusercontent.com");
		googlePay2.setGooglePlayClientSecret("_4Bf2cVK2SU-IsI_evn6EHmH");
		googlePay2.setGooglePlayCode("4/LI2HdXPLu3TzZRszPiHO_qSLCnyC8Xt01qNpSPyCKiE");
		googlePay2.setGooglePlayRedirectUri("urn:ietf:wg:oauth:2.0:oob");
		googlePay2.setGooglePlayTokenApi("https://accounts.google.com/o/oauth2/token");
		googlePay2.setGooglePlayRefreshToken("1/1vg9xqPHOL7ZpWBaoblb8AaIYjYdHwsPu-lWS8LBKIlIgOrJDtdun6zK6XiATCKT33333333333");
		
		c.getGooglePayMap().put(1, googlePay1);
		c.getGooglePayMap().put(3, googlePay2);
		
		c.setUpdatePath("/game/texas/texas_http/src/public/head/");
		
		logger.info(JSON.toJSONString(c));
	}

	
	
	
}
