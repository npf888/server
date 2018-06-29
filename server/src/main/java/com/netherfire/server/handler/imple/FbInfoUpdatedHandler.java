package com.netherfire.server.handler.imple;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.netherfire.server.db.UserInfoDaoServer;
import com.netherfire.server.db.entity.UserInfo;
import com.netherfire.server.handler.BaseHandler;
import com.netherfire.server.handler.request.ResetUpdateFbInfoForm;
import com.netherfire.server.handler.response.Response;
import com.netherfire.server.handler.result.RestReturnVO;
import com.netherfire.server.handler.result.ResultCode;
import com.netherfire.server.manager.UserManager;
import com.netherfire.server.manager.pojo.CacheUserInfo;
import com.netherfire.server.utils.UtilsIO;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class FbInfoUpdatedHandler extends BaseHandler {

	public static Logger log = LogManager.getLogger(FbInfoUpdatedHandler.class);
	
	@Override
	public void handlePost(ChannelHandlerContext ctx, FullHttpRequest request, boolean iskeepAlive) {
		try{
			ByteBuf byteBuf = request.content();
			String data = UtilsIO.byteBufToStr(byteBuf);
			ResetUpdateFbInfoForm resetUpdateFbInfoForm = JSON.parseObject(data, ResetUpdateFbInfoForm.class);
			
			
			long userid = resetUpdateFbInfoForm.getUserId();
			asyTask.addSynTask(() -> {
				RestReturnVO vo = new RestReturnVO();
				vo.setErrorCode(ResultCode.FAIL);
				log.info("will update updateFbInfo for: " + data);
				CacheUserInfo cui = UserManager.getInstance().getUserInfo(userid);
				if(cui!=null)
				{
					UserInfo ui = cui.getUserInfo();
					if(ui!=null)
					{
						ui.setUpdateFbInfo(true);
					}
				}
				UserInfoDaoServer.resetFaceInfo(userid);
				vo.setErrorCode(ResultCode.SUCCESS);
				
				FullHttpResponse response = Response.build(JSON.toJSONBytes(vo, features),iskeepAlive);
				ctx.write(response).addListener(ChannelFutureListener.CLOSE);
		        ctx.flush();
		        log.info(JSON.toJSONString(vo));
			});
		}catch(Exception e){
			log.error("", e);
		}
		
		
		
	}
	
}
