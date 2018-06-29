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
import com.netherfire.server.db.entity.UserInfo;
import com.netherfire.server.handler.BaseHandler;
import com.netherfire.server.handler.response.Response;
import com.netherfire.server.handler.result.RegisterLoginReturn;
import com.netherfire.server.handler.result.RestReturnVO;
import com.netherfire.server.handler.result.ResultCode;
import com.netherfire.server.manager.pojo.CacheUserInfo;
import com.netherfire.server.server.LoginServer;
import com.netherfire.server.utils.UtilsIO;

/**
 * h5注册
 * @author 牛鹏飞
 *
 */
public class HtmlRegisterHandler extends BaseHandler {
	
	public static Logger log = LogManager.getLogger(HtmlRegisterHandler.class);
	
	@Override
	public void handlePost(ChannelHandlerContext ctx, FullHttpRequest request, boolean iskeepAlive) {
		try{
			ByteBuf byteBuf = request.content();
			String data = UtilsIO.byteBufToStr(byteBuf);
			
			JSONObject jb = JSON.parseObject(data);
			
			
			//VisitorLoginForm visitorLogin = JSON.parseObject(data, VisitorLoginForm.class);
			
			log.info(data);
			
			asyTask.addSynTask(() -> {
				try{
					RestReturnVO vo = new RestReturnVO();
					vo.setErrorCode(ResultCode.EXIST);
					String username = jb.getString("username");
					String password = jb.getString("password");
					String channel = jb.getString("channel");
					if(!StringUtils.isNullOrEmpty(username) && !StringUtils.isNullOrEmpty(password) && !StringUtils.isNullOrEmpty(channel)){
						//去除空格
						username = username.trim();
						password = password.trim();
						channel = channel.trim();
						UserInfo userinfo = LoginServer.HtmlUserExist(username,channel);
						 
						 //等于空 就去注册
						if(userinfo == null){
						   LoginServer.HtmlUserRegister(jb,username,password,channel,"");
						   vo.setErrorCode(ResultCode.SUCCESS);
						}
					}else{
						vo.setErrorCode(ResultCode.FAIL);
					}
					FullHttpResponse response = Response.build(JSON.toJSONBytes(vo, features),iskeepAlive);
					response.headers().set("Access-Control-Allow-Origin","*"); // 跨域  
					ctx.write(response).addListener(ChannelFutureListener.CLOSE);
			        ctx.flush();
			        
			        log.info(JSON.toJSONString(vo));
				}catch(Exception e){
					log.error("登录失败,异常：",e);
				}
			});
			
		}catch(Exception e){
			log.error(e.toString());
		}
	}

	
}
