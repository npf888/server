package com.netherfire.server.handler.service;

import io.netty.handler.codec.http.FullHttpRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.netherfire.server.db.WAccessTokenMapperDao;
import com.netherfire.server.db.entity.WAccessToken;
import com.netherfire.server.handler.WeixinUtils;
import com.netherfire.server.utils.HttpsUtil;
import com.netherfire.server.vo.WAccessTokenJSON;
import com.netherfire.server.vo.WeixinUserInfo;

public class PullUserService {
	static Logger logger = LogManager.getLogger(PullUserService.class);
	public static WAccessToken getAccessToken(FullHttpRequest request,String code, String state) {
		
		
		String url = WeixinUtils.ObtainAccessToken
				+"?appid="+WeixinUtils.AppID
				+"&secret="+WeixinUtils.AppSecret
				+"&code="+code
				+"&grant_type=authorization_code";
		List<Map.Entry<String,String>> headerList = request.headers().entries();
		 Map<String,String> header = new HashMap<String,String>();
		 for(Map.Entry<String,String> entry:headerList){
			  String key = entry.getKey();
			  String value = entry.getValue();
			  header.put(key,value);
		 }
		String json =  HttpsUtil.doGetWithHeader(url, new HashMap<String,String>(), header, true);
		logger.info("获取accessToken 返回的json :"+json);
		WAccessTokenJSON wAccessTokenJSON = JSONObject.parseObject(json, WAccessTokenJSON.class);
		wAccessTokenJSON.setCode(code);
		//先看看数据库里有没有
		WAccessToken params = new WAccessToken();
		params.setOpenid(wAccessTokenJSON.getOpenid());
		List<WAccessToken> wAccessTokenList = (List<WAccessToken>)WAccessTokenMapperDao.queryList(params);
		if(wAccessTokenList != null && wAccessTokenList.size()>0){
			
			logger.info("===更新==================   wAccessTokenList  :"+wAccessTokenList.size());
			
			WAccessToken wAccessToken = wAccessTokenList.get(0);
			//判断用户token 是否过期
			String res = HttpsUtil.doPost(WeixinUtils.CheckAccessToken+"?access_token="+wAccessToken.getAccess_token()+"&openid="+wAccessToken.getOpenid(), new HashMap<String,String>(), true);
			JSONObject jbb = (JSONObject)JSONObject.parse(res);
			int errcode = jbb.getInteger("errcode");
			if(errcode == 0){//是可以的
				logger.info("===更新1==================   wAccessTokenList  :"+wAccessTokenList.size());
				copyToken(wAccessTokenJSON,wAccessToken);
				WAccessTokenMapperDao.update(wAccessToken);
				return wAccessToken;
			}else{//不可以的 然后在重新刷新token
				logger.info("===更新2==================  :"+WeixinUtils.RefreshAccessToken+"?appid="+WeixinUtils.AppID+"&grant_type=refresh_token"+"&refresh_token="+wAccessTokenJSON.getRefresh_token());
				String res2 = HttpsUtil.doPost(WeixinUtils.RefreshAccessToken+"?appid="+WeixinUtils.AppID+"&grant_type=refresh_token"+"&refresh_token="+wAccessTokenJSON.getRefresh_token(), new HashMap<String,String>(), true);
				logger.info("===更新3==================   res2  :"+res2);
				WAccessTokenJSON wAccessTokenJSON2 = JSONObject.parseObject(res2, WAccessTokenJSON.class);
				wAccessTokenJSON2.setCode(code);
				copyToken(wAccessTokenJSON2,wAccessToken);
				WAccessTokenMapperDao.update(wAccessToken);
				return wAccessToken;
				
			}
			
			
			
		}else{
			logger.info("===保存==================   wAccessTokenList  :"+wAccessTokenList);
			WAccessToken wAccessToken = new WAccessToken();
			copyToken(wAccessTokenJSON,wAccessToken);
			WAccessTokenMapperDao.insert(wAccessToken);
			return wAccessToken;
		}
		
	}
	
	/*public static void main(String[] args){
		String url ="https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=wx8f98ce73361b03fc&grant_type=refresh_token&refresh_token=7_O5hjF5kv7nfu5bG5H-uzYbFuGwlAjGgWCT5QA5Fuc9hOFxwFowNiR5SAhl2QvTaV8Bfaov8eNCq1bEtCLPSgdWkbNpSvS6Fhml3ALuuu5Vo";
		String res2 = HttpsUtil.doPost(url, new HashMap<String,String>(), false);
		logger.info("===更新3==================   res2  :"+res2);
		WAccessTokenJSON wAccessTokenJSON2 = JSONObject.parseObject(res2, WAccessTokenJSON.class);
		logger.info("===更新4==================   res2  :"+wAccessTokenJSON2);
		
	}*/
	
	private static void copyToken(WAccessTokenJSON wAccessTokenJSON,
			WAccessToken wAccessToken) {
		wAccessToken.setCode(wAccessTokenJSON.getCode());
		wAccessToken.setAccess_token(wAccessTokenJSON.getAccess_token());
		wAccessToken.setExpires_in(wAccessTokenJSON.getExpires_in());
		wAccessToken.setOpenid(wAccessTokenJSON.getOpenid());
		wAccessToken.setRefresh_token(wAccessTokenJSON.getRefresh_token());
		wAccessToken.setScope(wAccessTokenJSON.getScope());
	}
	public String getRefreshAccessToken(FullHttpRequest  request, String refreshToken) {
		String url = WeixinUtils.RefreshAccessToken
				+"?appid="+WeixinUtils.AppID
				+"&grant_type=refresh_token"
				+"&refresh_token="+refreshToken;
		String json =  HttpsUtil.doGet(url, new HashMap<String,String>(), true);
		logger.info("获取accessToken 返回的json :"+json);
		return json;
		
	}
	
	public static String getUserInfo(FullHttpRequest request, String accessToken,String openid) {
		String userUrl = WeixinUtils.UserInfo
				+"?access_token="+accessToken
				+"&openid="+openid
				+"&lang=zh_CN ";
		logger.info("======获取用户信息的userUrl:::"+userUrl);
		return userUrl;
	}
	public static WeixinUserInfo getUserInfoInvoke(FullHttpRequest request ,String url) {
		logger.info("======获取用户信息的url:::"+url);
		List<Map.Entry<String,String>> headerList = request.headers().entries();
		 Map<String,String> header = new HashMap<String,String>();
		 for(Map.Entry<String,String> entry:headerList){
			  String key = entry.getKey();
			  String value = entry.getValue();
			  header.put(key,value);
		 }
		URL url1 = null;
		try {
			url1 = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		String json =  HttpsUtil.doGet(url1.toString(), new HashMap<String,String>(), true);
		
		logger.info("======获取  WeixinUserInfo 返回的json :"+json);
		WeixinUserInfo weixinUserInfo = JSONObject.parseObject(json, WeixinUserInfo.class);
		logger.info("========当前weixinUserInfo:"+weixinUserInfo);
		return weixinUserInfo;
	}
	
	
	public static String download(String urlString, String savePath){
		try{
			String filename = UUID.randomUUID().toString()+".png";
			return download(urlString,filename,savePath);
		}catch(Exception e){
			logger.error(e);
		}
		return null;
	}
	
	 private static String download(String urlString, String filename,String savePath) throws Exception {    
	        // 构造URL    
	        URL url = new URL(urlString);    
	        // 打开连接    
	        URLConnection con = url.openConnection();    
	        //设置请求超时为5s    
	        con.setConnectTimeout(5*1000);    
	        // 输入流    
	        InputStream is = con.getInputStream();    
	        
	        // 1K的数据缓冲    
	        byte[] bs = new byte[1024];    
	        // 读取到的数据长度    
	        int len;    
	        // 输出的文件流    
	       File sf=new File(savePath);    
	       if(!sf.exists()){    
	           sf.mkdirs();    
	       }    
	       String newPathName = sf.getPath()+"/"+filename;
	       logger.info("========newPathName:"+newPathName);
	       OutputStream os = new FileOutputStream(newPathName);    
	        // 开始读取    
	        while ((len = is.read(bs)) != -1) {    
	          os.write(bs, 0, len);    
	        }    
	        // 完毕，关闭所有链接    
	        os.close();    
	        is.close();  
	        return filename;
	    }     
}
