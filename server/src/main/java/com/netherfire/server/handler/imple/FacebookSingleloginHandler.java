package com.netherfire.server.handler.imple;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;
import com.netherfire.server.db.UserInfoDaoServer;
import com.netherfire.server.db.entity.UserInfo;
import com.netherfire.server.handler.BaseHandler;
import com.netherfire.server.handler.response.Response;
import com.netherfire.server.handler.result.FacebookLoginReturn;
import com.netherfire.server.handler.result.RestReturnVO;
import com.netherfire.server.handler.result.ResultCode;
import com.netherfire.server.manager.pojo.CacheUserInfo;
import com.netherfire.server.server.LoginServer;
import com.netherfire.server.utils.UtilsIO;
/**
 * faceBook登陆
 * @author 郭君伟
 *
 */
public class FacebookSingleloginHandler extends BaseHandler {

	public static Logger log = LogManager.getLogger(FacebookSingleloginHandler.class);
	
	@Override
	public void handlePost(ChannelHandlerContext ctx, FullHttpRequest request, boolean iskeepAlive) {
		try{
			ByteBuf byteBuf = request.content();
			String data = UtilsIO.byteBufToStr(byteBuf);
			JSONObject paramMap = JSON.parseObject(data);
			
			log.info(data);
			
			asyTask.addSynTask(() -> {
				RestReturnVO vo = new RestReturnVO();
				vo.setErrorCode(ResultCode.FAIL);
				String channel = paramMap.getString("channel");//固定facebook
				String facebookId = paramMap.getString("facebookId");
				String nickname = paramMap.getString("nickname");
				String img = paramMap.getString("img");
				if(StringUtils.isNullOrEmpty(channel) 
						 || StringUtils.isNullOrEmpty(facebookId) 
						 || StringUtils.isNullOrEmpty(nickname) 
						 || StringUtils.isNullOrEmpty(img) ){
					FullHttpResponse response = Response.build(JSON.toJSONBytes(vo, features),iskeepAlive);
					response.headers().set("Access-Control-Allow-Origin","*"); // 跨域  
					ctx.write(response).addListener(ChannelFutureListener.CLOSE);
			        ctx.flush();
			        return;
				}
				UserInfo userInfo = UserInfoDaoServer.facebookExist(facebookId);
				
				if(userInfo == null){//创建
					userInfo=LoginServer.saveOrUpdateFBUserInfo(true,paramMap);
				}else{//更新
					userInfo=LoginServer.saveOrUpdateFBUserInfo(false,paramMap);
				}
				
				//放入缓存
				CacheUserInfo cacheUserInfo = LoginServer.login(userInfo, "");
				
				log.info("cacheUserInfo>>>"+cacheUserInfo);
				if(cacheUserInfo != null){
					FacebookLoginReturn returnVo = new FacebookLoginReturn();
					returnVo.setFbId(facebookId);
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
