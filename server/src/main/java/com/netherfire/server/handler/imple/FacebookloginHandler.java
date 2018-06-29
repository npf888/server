package com.netherfire.server.handler.imple;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.netherfire.server.db.UserInfoDaoServer;
import com.netherfire.server.db.entity.UserInfo;
import com.netherfire.server.handler.BaseHandler;
import com.netherfire.server.handler.response.Response;
import com.netherfire.server.handler.result.FacebookLoginReturn;
import com.netherfire.server.handler.result.RestReturnVO;
import com.netherfire.server.handler.result.ResultCode;
import com.netherfire.server.manager.pojo.CacheUserInfo;
import com.netherfire.server.server.LoginServer;
import com.netherfire.server.utils.ParamsUtil;
import com.netherfire.server.utils.UtilsIO;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
/**
 * faceBook登陆
 * @author 郭君伟
 *
 */
public class FacebookloginHandler extends BaseHandler {

	public static Logger log = LogManager.getLogger(FacebookloginHandler.class);
	
	@Override
	public void handlePost(ChannelHandlerContext ctx, FullHttpRequest request, boolean iskeepAlive) {
		try{
			ByteBuf byteBuf = request.content();
			String data = UtilsIO.byteBufToStr(byteBuf);
			
			//FacebookLoginForm loginForm = JSON.parseObject(data,FacebookLoginForm.class);
			
			Map<String,String> paramMap = ParamsUtil.getParam(data);
			
			log.info(data);
			
			asyTask.addSynTask(() -> {
				String fbId = paramMap.get("userId");
				String deviceMac = paramMap.get("deviceMac");
				UserInfo userInfo = UserInfoDaoServer.selectExistedFbOrBindFb(fbId, deviceMac, paramMap);
				
				RestReturnVO vo = new RestReturnVO();
				vo.setErrorCode(ResultCode.FAIL);
				
				
				//放入缓存
				CacheUserInfo cacheUserInfo = LoginServer.login(userInfo, "");
				
				log.info("cacheUserInfo>>>"+cacheUserInfo);
				if(cacheUserInfo != null){
					FacebookLoginReturn returnVo = new FacebookLoginReturn();
					returnVo.setFbId(paramMap.get("userId"));
					returnVo.setIp(cacheUserInfo.getIp());
					returnVo.setUserCode(cacheUserInfo.getUserCode());
					returnVo.setPort(cacheUserInfo.getPort());
					returnVo.setUserId(cacheUserInfo.getUserInfo().getId());
					returnVo.setNewFbId(userInfo.isUpdateFbInfo()?1:0);
					vo.setResult(returnVo);
					vo.setErrorCode(ResultCode.SUCCESS);
				}
				
				
				FullHttpResponse response = Response.build(JSON.toJSONBytes(vo, features),iskeepAlive);
				response.headers().set("Access-Control-Allow-Origin","*"); // 跨域  
				ctx.write(response).addListener(ChannelFutureListener.CLOSE);
		        ctx.flush();
		        
		        log.info(JSON.toJSONString(vo));
		        
				
			});
		}catch(Exception e){
			log.error(e.toString());
		}
	}
}
