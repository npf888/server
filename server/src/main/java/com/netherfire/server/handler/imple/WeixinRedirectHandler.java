package com.netherfire.server.handler.imple;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.netherfire.server.Global;
import com.netherfire.server.db.entity.WAccessToken;
import com.netherfire.server.handler.BaseHandler;
import com.netherfire.server.handler.response.Response;
import com.netherfire.server.handler.service.PullUserService;

/**
 * 重定向 微信中的重定向的url
 * @author JavaServer
 *
 */
public class WeixinRedirectHandler extends BaseHandler {
	
	Logger logger = LogManager.getLogger(WeixinURLHandler.class);
	
	@Override
	public void handleGet(ChannelHandlerContext ctx,Map<String, List<String>> parameter, FullHttpRequest request, boolean iskeepAlive) {
		try{
			logger.info("========进入重定向");
			String code = parameter.get("code").get(0);
			String state = parameter.get("state").get(0);
			String redirectUri = Global.getInstance().getConfig().getWeixinRedirectUrl();
			String userUrl = "";
			String result = "";
			try{
				logger.info("========当前code:"+code);
				logger.info("========当前state:"+state);
				//获取到用户 openId
				WAccessToken wAccessToken = PullUserService.getAccessToken(request,code,state);
				logger.info("========当前wAccessToken:"+wAccessToken);
				//拉取用户信息
				userUrl = PullUserService.getUserInfo(request,wAccessToken.getAccess_token(),wAccessToken.getOpenid());
				logger.info("========当前userUrl:"+userUrl);
				String baseUrl = Base64.getEncoder().encodeToString(userUrl.getBytes());
				result = "<script type=\"text/javascript\" >  window.location.href=\""+redirectUri+"?userUrl="+baseUrl+"\"; </script>";
			}catch(Exception e){
				String baseUrl = Base64.getEncoder().encodeToString(userUrl.getBytes());
				result = "<script type=\"text/javascript\" >  window.location.href=\""+redirectUri+"?userUrl="+baseUrl+"\"; </script>";
			}
			
			/*RestReturnVO vo = new RestReturnVO();
			vo.setErrorCode(ResultCode.SUCCESS);
			JSONObject re = new JSONObject();
			re.put("userUrl", userUrl);
			vo.setResult(jb);*/
			
//			FullHttpResponse response = Response.build(JSON.toJSONBytes(vo, features),iskeepAlive);
			FullHttpResponse response = this.build(result.getBytes(),iskeepAlive);
			response.headers().set("Access-Control-Allow-Origin","*"); // 跨域  
			ctx.write(response).addListener(ChannelFutureListener.CLOSE);
	        ctx.flush();
		}catch(Exception e){
			logger.error(e);
		}
		
		
	}
	private  FullHttpResponse build(byte[] date,boolean iskeepAlive){
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(date));
        response.headers().set(Response.CONTENT_TYPE, "text/html");
        response.headers().setInt(Response.CONTENT_LENGTH, response.content().readableBytes());
        if (iskeepAlive) {
        	 response.headers().set(Response.CONNECTION, Response.KEEP_ALIVE);
         }
        return response;
	}
}
