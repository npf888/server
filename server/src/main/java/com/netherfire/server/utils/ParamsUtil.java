package com.netherfire.server.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;

public class ParamsUtil {
	private static final Logger logger = LogManager.getLogger(ParamsUtil.class);
	
	public static Map<String,String> getParam(String params) throws UnsupportedEncodingException{
		Map<String,String> map = new HashMap<String,String>();
		
		String[] strs = params.split("&");
		
		for(String str : strs){
			String[] param = str.split("=");
			if(param == null || param.length<=1){
				continue;
			}
			map.put(param[0], URLDecoder.decode(param[1], "UTF-8"));
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception {
		String str = "AA%3aC6%3a58%3aBF%3a54%3a64&androidId=c1b83b9903ed956d&macAddress=AA%3aC6%3a58%3aBF%3a54%3a64&deviceId=474524121509248";
		logger.info(URLDecoder.decode(str, "UTF-8"));
		Map<String,String> map = getParam(str);
		System.out.println(JSON.toJSONString(map));
	}
}
