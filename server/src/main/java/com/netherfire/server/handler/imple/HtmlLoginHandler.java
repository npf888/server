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
import com.netherfire.server.handler.result.RegisterLoginTwoReturn;
import com.netherfire.server.handler.result.RestReturnVO;
import com.netherfire.server.handler.result.ResultCode;
import com.netherfire.server.manager.pojo.CacheUserInfo;
import com.netherfire.server.server.LoginServer;
import com.netherfire.server.utils.ThirdPartyUtil;
import com.netherfire.server.utils.UtilsIO;

/**
 * h5用户登录
 * @author 牛鹏飞
 *
 */
public class HtmlLoginHandler extends BaseHandler {
	
public static Logger log = LogManager.getLogger(HtmlLoginHandler.class);
	
	@Override
	public void handlePost(ChannelHandlerContext ctx, FullHttpRequest request, boolean iskeepAlive) {
		try{
			ByteBuf byteBuf = request.content();
			String data = UtilsIO.byteBufToStr(byteBuf);
			
			JSONObject jb = JSON.parseObject(data);
			
			
			log.info(data);
			
			asyTask.addSynTask(() -> {
				try{
					RestReturnVO vo = new RestReturnVO();
					vo.setErrorCode(ResultCode.NOTEXIST);
					String username = jb.getString("username");
					String password = jb.getString("password");
					String channel = jb.getString("channel");
					if(!StringUtils.isNullOrEmpty(username) && !StringUtils.isNullOrEmpty(password) && !StringUtils.isNullOrEmpty(channel)){
						//去除空格
						username = username.trim();
						password = password.trim();
						channel = channel.trim();
						//根据用户名和密码 还有渠道 查询
						UserInfo userinfo = LoginServer.HtmlUserExist(username,password,channel);
						 //不等于空  就去登录了
						if(userinfo != null){
							 CacheUserInfo info = LoginServer.HtmlUserLogin(userinfo, "");
							 RegisterLoginTwoReturn vlr = new RegisterLoginTwoReturn();
							 vlr.setIp(info.getIp());
							 vlr.setPort(info.getPort());
							 vlr.setUserCode(info.getUserCode());
							 vlr.setUserId(info.getUserInfo().getId());
							 vlr.setUsername(username);
							 vo.setErrorCode(ResultCode.SUCCESS);
							 vo.setResult(vlr);
							 
							//调用第三方的接口
							ThirdPartyUtil.postLogin(jb.getString("channel"),info.getUserInfo());
						}
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
