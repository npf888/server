package com.netherfire.server.handler.imple;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.netherfire.server.handler.BaseHandler;
import com.netherfire.server.handler.request.FacebookFriendsForm;
import com.netherfire.server.handler.response.Response;
import com.netherfire.server.handler.result.FacebookFriendReturn;
import com.netherfire.server.handler.result.RestReturnVO;
import com.netherfire.server.handler.result.ResultCode;
import com.netherfire.server.server.FacebookServer;
import com.netherfire.server.utils.UtilsIO;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
/**
 * 请求facebook 好友
 * @author 郭君伟
 *
 */
public class FacebookFriendHandler extends BaseHandler {

	public static Logger log = LogManager.getLogger(FacebookFriendHandler.class);
	
	@Override
	public void handlePost(ChannelHandlerContext ctx, FullHttpRequest request, boolean iskeepAlive) {
		try{
			ByteBuf byteBuf = request.content();
			String data = UtilsIO.byteBufToStr(byteBuf);
			
			FacebookFriendsForm facebookFriends = JSON.parseObject(data, FacebookFriendsForm.class);
			
			log.info(JSON.toJSONString(facebookFriends));
			
			asyTask.addTask(()->{
				RestReturnVO vo = new RestReturnVO();
				vo.setErrorCode(ResultCode.SUCCESS);
				
				FacebookFriendReturn returnVo = new FacebookFriendReturn();
				returnVo.setFriendIdList(FacebookServer.getUserIdByFbId(facebookFriends.getFriendIdList()));
				vo.setResult(returnVo);
				
				
				 
				FullHttpResponse response = Response.build(JSON.toJSONBytes(vo, features),iskeepAlive);
				ctx.write(response).addListener(ChannelFutureListener.CLOSE);
		        ctx.flush();
		        
		        log.info(JSON.toJSONString(vo));
		        
			});
			
		}catch(Exception e){
			log.error(e.toString());
		}
	}

	
}
