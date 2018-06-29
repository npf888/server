package com.netherfire.server.thirdparty;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.netherfire.server.Global;
import com.netherfire.server.db.UserInfoDaoServer;
import com.netherfire.server.utils.HttpsUtil;

/**
 * 博趣平台
 * @author JavaServer
 *
 */
public class Boqu {
	public static Logger logger = LogManager.getLogger(Boqu.class);
	/**
	 * 博趣 平台
	 * 请求登录
	 */
	public static  void postLogin(long uid){
		try{
			Map<String,String> params = new HashMap<String,String>();
			params.put("client_id", String.valueOf(Global.getInstance().getConfig().getBoquPay().getClient_id()));
			params.put("client_secret", Global.getInstance().getConfig().getBoquPay().getClient_secret());
			params.put("uid", uid+"");
			String url = Global.getInstance().getConfig().getBoquPay().getTokenUrl();
			String response = HttpsUtil.doPost(url, params, false);
			JSONObject rjb = JSONObject.parseObject(response);
			String tokenType = rjb.getString("token_type");
			String accessToken = rjb.getString("access_token");
			long second = rjb.getLong("expires_in");//秒
			long secondMill = new Date().getTime()+second*1000;
			//把token更新一下
			UserInfoDaoServer.updateAccessToken(uid,tokenType, accessToken,secondMill);
		}catch(Exception e){
			logger.error("调用第三方平台<博趣> 的问题：",e);	
		}
	}
}
