package com.netherfire.server.handler.imple;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;
import com.netherfire.server.db.UserInfoDaoServer;
import com.netherfire.server.db.entity.UserInfo;
import com.netherfire.server.handler.BaseHandler;
import com.netherfire.server.handler.response.Response;
import com.netherfire.server.handler.result.RestReturnVO;
import com.netherfire.server.handler.result.ResultCode;
import com.netherfire.server.handler.result.VisitorLoginReturn;
import com.netherfire.server.manager.pojo.CacheUserInfo;
import com.netherfire.server.server.LoginServer;
import com.netherfire.server.utils.ThirdPartyUtil;
import com.netherfire.server.utils.UtilsIO;

/**
 * 微信登陆
 * @author 郭君伟
 *
 */
public class WeixinLoginHandler extends BaseHandler {
	
	public static Logger log = LogManager.getLogger(WeixinLoginHandler.class);
	
	@Override
	public void handlePost(ChannelHandlerContext ctx, FullHttpRequest request, boolean iskeepAlive) {
		try{
			ByteBuf byteBuf = request.content();
			String data = UtilsIO.byteBufToStr(byteBuf);
			
			JSONObject jb = JSON.parseObject(data);
			Map<String,String> map = new HashMap<String,String>();
			for(String key:jb.keySet()){
				map.put(key, jb.getString(key));
			}
			
			
			//VisitorLoginForm visitorLogin = JSON.parseObject(data, VisitorLoginForm.class);
			
			log.info(data);
			
			asyTask.addSynTask(() -> {
				try{
					RestReturnVO vo = new RestReturnVO();
					vo.setErrorCode(ResultCode.FAIL);

					 CacheUserInfo info = LoginServer.visitorLogin(map, "");
					 
					 if(info != null){
						 VisitorLoginReturn vlr = new VisitorLoginReturn();
						 vlr.setIp(info.getIp());
						 vlr.setPort(info.getPort());
						 vlr.setUserCode(info.getUserCode());
						 vlr.setUserId(info.getUserInfo().getId());
						 vo.setErrorCode(ResultCode.SUCCESS);
						 vo.setResult(vlr);
						 
						//调用第三方的接口
						ThirdPartyUtil.postLogin(jb.getString("channel"),info.getUserInfo());
					 }
					
					 
					FullHttpResponse response = Response.build(JSON.toJSONBytes(vo, features),iskeepAlive);
					response.headers().set("Access-Control-Allow-Origin","*"); // 跨域  
					ctx.write(response).addListener(ChannelFutureListener.CLOSE);
					//Test.getInstance().add(map.get("deviceMac"), ctx);
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
