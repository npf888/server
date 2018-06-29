package com.netherfire.server.handler.core.http;

import static io.netty.handler.codec.http.HttpMethod.GET;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysql.jdbc.StringUtils;
import com.netherfire.server.Global;
import com.netherfire.server.handler.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.QueryStringDecoder;

/**
 * 路由Handler
 * @author 郭君伟
 *
 */
public class RouteHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	private static final Logger logger = LogManager.getLogger(RouteHandler.class);
	

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		//文件上传 文件下载 GET POST 请求
		
		if (!request.decoderResult().isSuccess()) {
            return;
        }
		
		HttpMethod  httpMethod  = request.method();
		
		QueryStringDecoder decoder = new QueryStringDecoder(request.uri()); 
		
	    //路由串不带参数
		String path = decoder.path();
		logger.info("1-当前请求的URL:::"+path);
		//是否保存长连接
		boolean iskeepAlive = HttpUtil.isKeepAlive(request);
		
		if(httpMethod == GET){
			 //判断url 是否在Set 里面 不在就是请求资源、
			logger.info("1-当前请求的URL:::---1");
			 if(Global.getInstance().checkUri(path)){
				 logger.info("1-当前请求的URL:::---2");
				//解析参数
				 Map<String, List<String>> parameter = decoder.parameters();
				 
				 Handler handler = Global.getInstance().getHandler(path);
				 logger.info("1-当前请求的URL:::---2-1"+handler.toString());
				 
				 handler.handleGet(ctx,parameter,request,iskeepAlive);
			 }else{//下载资源
				 logger.info("1-当前请求的URL:::---3");
				 Global.getInstance().getDownloadFile().download(ctx, request);
			}
		}else{//POST 请求 有图片上传
			String contentType = request.headers().get("Content-type");
			if(StringUtils.isNullOrEmpty(contentType)){
				 Handler handler = Global.getInstance().getHandler(path);
				 handler.handlePost(ctx, request,iskeepAlive);
				 return;
			}
			logger.info("2-当前请求的URL:::"+path);
			if(contentType.equals("application/json") || contentType.equals("application/x-www-form-urlencoded")){//上传文件
				 Handler handler = Global.getInstance().getHandler(path);
				 handler.handlePost(ctx, request,iskeepAlive);
			}else if(contentType.contains("multipart/form-data") ){
				/* String key = contentType.split(";")[1].split("=")[1];
				 String newStr = key.substring(1, key.length()-1);
				 Handler handler = Global.getInstance().getHandler(path);
				 handler.handlePostPicture(ctx, request.content(),iskeepAlive,newStr);*/
				 return;
			}
			
			if(contentType.equals("text/html")){
				 Handler handler = Global.getInstance().getHandler(path);
				 handler.handlePost(ctx, request,iskeepAlive);
			}
			
		}
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error("", cause);
		ctx.close();
	}
	
	

}
