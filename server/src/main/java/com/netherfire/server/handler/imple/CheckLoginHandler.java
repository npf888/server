package com.netherfire.server.handler.imple;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.netherfire.server.db.UserInfoDaoServer;
import com.netherfire.server.db.entity.UserInfo;
import com.netherfire.server.handler.BaseHandler;
import com.netherfire.server.handler.request.CheckLoginForm;
import com.netherfire.server.handler.response.Response;
import com.netherfire.server.handler.result.CheckLoginReturn;
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
/**
 * 验证登陆
 * @author 郭君伟
 *
 */
public class CheckLoginHandler extends BaseHandler {

	public static Logger log = LogManager.getLogger(CheckLoginHandler.class);
	
	@Override
	public void handlePost(ChannelHandlerContext ctx, FullHttpRequest request, boolean iskeepAlive) {
		try{
			ByteBuf byteBuf = request.content();
			String data = UtilsIO.byteBufToStr(byteBuf);
			
			CheckLoginForm checkLogin = JSON.parseObject(data, CheckLoginForm.class);
			
			log.info(JSON.toJSONString(checkLogin));
			
			asyTask.addTask(() ->{
				RestReturnVO vo = new RestReturnVO();
				vo.setErrorCode(ResultCode.FAIL);
				
				long userid = checkLogin.getUserId();
						
				CacheUserInfo cacheUserInfo = UserManager.getInstance().getUserInfo(userid);
				UserInfo ui = null;
				if(cacheUserInfo==null)
				{
					ui = UserInfoDaoServer.userInfoById(userid);
				}
				else
				{
					ui = cacheUserInfo.getUserInfo();
				}
				
				if(ui != null){
					vo.setErrorCode(ResultCode.SUCCESS);
					CheckLoginReturn check = new CheckLoginReturn();
					check.setUserId(userid);
					check.setAccountId(ui.getAccountId());
					check.setFacebookId(ui.getFacebookId());
					check.setName(ui.getName());
					check.setImg(ui.getImg());
					check.setRole(ui.getRole());
					check.setUpdateFbInfo(ui.isUpdateFbInfo());
					check.setDeviceMac(ui.getDeviceMac());
					vo.setResult(check);
					UserManager.getInstance().removeUser(userid);
				}
				else
				{
					log.error("no userInfo for userid: "+userid);
				}
				 
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
