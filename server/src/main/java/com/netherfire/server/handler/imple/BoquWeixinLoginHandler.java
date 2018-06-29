package com.netherfire.server.handler.imple;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.netherfire.server.Global;
import com.netherfire.server.handler.BaseHandler;
import com.netherfire.server.handler.WeixinUtils;
import com.netherfire.server.handler.response.Response;
import com.netherfire.server.utils.HttpsUtil;

/**
 * 对接 博趣那边的微信登录，  微信登录在博趣那边
 * @author JavaServer
 *
 */
public class BoquWeixinLoginHandler extends BaseHandler {
	
	public static Logger log = LogManager.getLogger(BoquWeixinLoginHandler.class);

	@Override
	public void handleGet(ChannelHandlerContext ctx,Map<String, List<String>> parameter, FullHttpRequest request,boolean iskeepAlive) {
		String client_id = Global.getInstance().getConfig().getBoquPay().getClient_id()+"";
		String client_secret = Global.getInstance().getConfig().getBoquPay().getClient_secret();
		String timestamp = new Date().getTime()+"";
		String url = Global.getInstance().getConfig().getBoquWeixinRedirectUrl();
		String goUrl = WeixinUtils.BoquWeixinURL
				+ "?client_id="+client_id
				+ "&client_secret="+client_secret
				+ "&timestamp="+timestamp
				+ "&redirect_url="+url;
		String res = HttpsUtil.doGet(goUrl,new HashMap<String,String>(), false);
		log.info("BoquWeixinLoginHandler=="+goUrl);
		log.info("res=="+res);
		
		FullHttpResponse response = build(res.getBytes(),iskeepAlive);
		response.headers().set("Access-Control-Allow-Origin","*"); // 跨域  
		ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        ctx.flush();
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
