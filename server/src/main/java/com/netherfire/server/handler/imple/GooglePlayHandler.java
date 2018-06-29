package com.netherfire.server.handler.imple;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.netherfire.server.Global;
import com.netherfire.server.config.pojo.GooglePay;
import com.netherfire.server.handler.BaseHandler;
import com.netherfire.server.handler.request.GooglePlayRechargeForm;
import com.netherfire.server.handler.response.Response;
import com.netherfire.server.handler.result.GooglePlayRechargeReturn;
import com.netherfire.server.handler.result.RestReturnVO;
import com.netherfire.server.handler.result.ResultCode;
import com.netherfire.server.server.RechargeServer;
import com.netherfire.server.utils.HttpsUtil;
import com.netherfire.server.utils.UtilsIO;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
/**
 * 谷歌充值
 * @author 郭君伟
 *
 */
public class GooglePlayHandler extends BaseHandler {
	
	public static Logger log = LogManager.getLogger(GooglePlayHandler.class);

	@Override
	public void handlePost(ChannelHandlerContext ctx, FullHttpRequest request, boolean iskeepAlive) {
		try{
			ByteBuf byteBuf = request.content();
			String data = UtilsIO.byteBufToStr(byteBuf);
			
			GooglePlayRechargeForm form = JSON.parseObject(data, GooglePlayRechargeForm.class);
			
			log.info("[谷歌验证]----------------------------谷歌验证 传递给谷歌验证的 参数：："+JSON.toJSONString(form));
			
			asyTask.addSynTask(()->{
				
				RestReturnVO vo = new RestReturnVO();
				vo.setErrorCode(ResultCode.FAIL);
				
				int googleID = form.getGoogleId();
				
				String accessToken = RechargeServer.getInstance().getAccessToken(googleID);
				
				if(accessToken != null){
					
					 Map<String,String> params = new HashMap<String,String>();
					 
					 log.info("[谷歌验证]----------------------------开始谷歌验证：：当前的accessToken:"+accessToken);
					 params.put("access_token", accessToken);
					
					 String url = getUrl(form);
					 
					 log.info("[谷歌验证]----------------------------开始谷歌验证：：当前的URL:"+url);
					 if(url != null){
						 String result = HttpsUtil.doGet(url, params, true);
						 log.info("[谷歌验证]----------------------------谷歌验证结束：：返回值："+result);
						 if(result != null){
							 log.info("[谷歌验证]----------------------------谷歌验证结束：：返回值 不为空：");
							 GooglePlayRechargeReturn returnVo = JSON.parseObject(result, GooglePlayRechargeReturn.class);
							 vo.setResult(returnVo);
							 vo.setErrorCode(ResultCode.SUCCESS);
						 }
					 }
				}
				
				FullHttpResponse response = Response.build(JSON.toJSONBytes(vo, features),iskeepAlive);
				ctx.write(response).addListener(ChannelFutureListener.CLOSE);
		        ctx.flush();
				
		        log.info(JSON.toJSONString(vo));
				
			});
		}catch(Exception e){
			log.error(e.toString());
		}
		
	}
	
	private String getUrl(GooglePlayRechargeForm form){
		 
		 GooglePay googlePay = Global.getInstance().getConfig().getGooglePayMap().get(form.getGoogleId());
		 if(googlePay == null){
			 return null;
		 }
			
		 String googlePlayApi = googlePay.getGooglePlayApi();
		
		 StringBuilder sb = new StringBuilder();
		 sb.append(googlePlayApi).append("/").append(form.getPackageName()).append("/purchases/products/")
		 .append(form.getProductId()).append("/tokens/").append(form.getToken());
		
		 return sb.toString();
	}

	
}
