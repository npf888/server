package com.netherfire.server.handler.imple;


import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netherfire.server.handler.BaseHandler;
import com.netherfire.server.handler.WeixinUtils;
import com.netherfire.server.handler.response.Response;
import com.netherfire.server.handler.result.RestReturnVO;
import com.netherfire.server.handler.result.ResultCode;

/**
 * 主要返回一个 url链接， 带有appID的，用户访问微信
 * @author JavaServer
 * http://116.62.175.124:8080/bin/indexWeXin.html
 * 
 * 
 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx8f98ce73361b03fc&
 * redirect_uri=http%3A%2F%2Fdice.bo-qu.com%2Fapi%2Fweixin%2FcomeIn.json&response_type=code&scope=snsapi_userinfo&state=state#wechat_redirect
 * 
 * 
 * 
 * 
 * 
 */
public class WeixinURLHandler extends BaseHandler {
	
	Logger logger = LogManager.getLogger(WeixinURLHandler.class);
	
	@Override
	public void handleGet(ChannelHandlerContext ctx,Map<String, List<String>> parameter,FullHttpRequest request, boolean iskeepAlive) {
		try{
			String url = WeixinUtils.AuthorizeURL
					+"?appid="+WeixinUtils.AppID
					+"&redirect_uri="+URLEncoder.encode(WeixinUtils.Redirect_URL+"/api/weixin/comeIn.json","utf-8")
					+"&response_type=code"
					+"&scope=snsapi_userinfo"
					+"&state=state"
					+"#wechat_redirect";
			logger.info("获取url远程请求返回的url:"+url);
			
			RestReturnVO vo = new RestReturnVO();
			vo.setErrorCode(ResultCode.SUCCESS);
			JSONObject jb = new JSONObject();
			jb.put("authURL", url);
			vo.setResult(jb);
			
			
			FullHttpResponse response = Response.build(JSON.toJSONBytes(vo, features),iskeepAlive);
			response.headers().set("Access-Control-Allow-Origin","*"); // 跨域  
			ctx.write(response).addListener(ChannelFutureListener.CLOSE);
	        ctx.flush();
			
		}catch(Exception e){
			logger.error(e);
		}
	
	}
}
