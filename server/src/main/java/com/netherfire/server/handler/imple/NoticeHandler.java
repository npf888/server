package com.netherfire.server.handler.imple;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.netherfire.server.handler.BaseHandler;
import com.netherfire.server.handler.response.Response;
import com.netherfire.server.handler.result.IndexNoticeReturn;
import com.netherfire.server.handler.result.RestReturnVO;
import com.netherfire.server.handler.result.ResultCode;
import com.netherfire.server.server.IndexNoticeServer;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * 发公告
 * @author 郭君伟
 *
 */
public class NoticeHandler extends BaseHandler {

	public static Logger log = LogManager.getLogger(NoticeHandler.class);
	
	@Override
	public void handleGet(ChannelHandlerContext ctx, Map<String, List<String>> parameter,FullHttpRequest request, boolean iskeepAlive) {
		
		this.asyTask.addTask(()->{
			
			RestReturnVO vo = new RestReturnVO();
			vo.setErrorCode(ResultCode.SUCCESS);
			
			IndexNoticeReturn iNRvo = IndexNoticeServer.getVO();
			vo.setResult(iNRvo);
			
			FullHttpResponse response = Response.build(JSON.toJSONBytes(vo, features),iskeepAlive);
			response.headers().set("Access-Control-Allow-Origin","*"); // 跨域  
			ctx.write(response).addListener(ChannelFutureListener.CLOSE);
	        ctx.flush();
	        
	        log.info(JSON.toJSONString(vo));
		});
		
		
		
	}

	
}
