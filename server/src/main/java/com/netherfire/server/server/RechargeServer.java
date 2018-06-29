package com.netherfire.server.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.netherfire.server.Global;
import com.netherfire.server.config.Config;
import com.netherfire.server.config.pojo.GooglePay;
import com.netherfire.server.server.pojo.GooglePlayCache;
import com.netherfire.server.server.pojo.GooglePlayerRefreshTokenResult;
import com.netherfire.server.utils.HttpsUtil;

/**
 * 支付服务
 * @author 郭君伟
 *
 */
public class RechargeServer {
	
	public static Logger log = LogManager.getLogger(RechargeServer.class);

	private static final RechargeServer server = new RechargeServer();
	
	private RechargeServer(){}
	
	/**key:谷歌ID  **/
	private Map<Integer,GooglePlayCache> accessTokenMap = new HashMap<Integer,GooglePlayCache>();
	
	public static RechargeServer getInstance(){
		return server;
	}
	
	/**
	 * TODO
	 */
	public void init(){
		
		Config config = Global.getInstance().getConfig();
		
		Map<Integer, GooglePay> map = config.getGooglePayMap();
		
		for(Entry<Integer, GooglePay> en : map.entrySet()){
			     int id = en.getKey();
			     GooglePay googlePay = en.getValue();
			     GooglePlayCache cache = refreshCurrentToken(googlePay);
			     if(cache == null){
			    	 log.error(id+"  错误！！！");
			     }else{
			    	 accessTokenMap.put(id, cache);
			     }
			    
		}
		log.info("刷新：：： "+JSON.toJSON(accessTokenMap));
	}
	
	/**
	 * 刷新Token
	 * @param googlePay
	 * @return
	 */
	private GooglePlayCache refreshCurrentToken(GooglePay googlePay){
		
		String url = googlePay.getGooglePlayTokenApi();
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("grant_type", "refresh_token");
		params.put("client_id", googlePay.getGooglePlayClientId());
		params.put("client_secret", googlePay.getGooglePlayClientSecret());
		params.put("refresh_token", googlePay.getGooglePlayRefreshToken());
		
		String  result = HttpsUtil.doPostSSL(url, params);
		
		if(result != null){
			GooglePlayerRefreshTokenResult grResult = JSON.parseObject(result, GooglePlayerRefreshTokenResult.class);
			
			if(Strings.isNullOrEmpty(grResult.getError())){
				GooglePlayCache cache = new GooglePlayCache();
				cache.setAccessToken(grResult.getAccess_token());
				cache.setExpiresIn(System.currentTimeMillis()+(grResult.getExpires_in()/2));//时间的一半就开始刷新
				cache.setGoogleId(googlePay.getGoogleId());
				return cache;
			}else{
				log.error(JSON.toJSONString(grResult));
			}
		}
		return null;
	}
	
	/**
	 * 获取AccessToken
	 * @param googleId
	 * @return
	 */
	public String getAccessToken(int googleId){
		
		GooglePlayCache cache = accessTokenMap.get(googleId);
		
		String accessToken = null;
		
		if(cache != null){
			accessToken = cache.getAccessToken();
			long currTime = System.currentTimeMillis();
			if(cache.getExpiresIn() < currTime){//刷新token
				GooglePay googlePay = Global.getInstance().getConfig().getGooglePayMap().get(googleId);
				GooglePlayCache newCache = refreshCurrentToken(googlePay);
				if(newCache != null){
					accessToken = newCache.getAccessToken();
					accessTokenMap.put(googleId, newCache);
				}
			}
		}
		return accessToken;
	}
	
	
	
	
}
