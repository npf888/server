package com.netherfire.server.handler.imple;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.mysql.jdbc.StringUtils;
import com.netherfire.server.Global;
import com.netherfire.server.handler.BaseHandler;
import com.netherfire.server.handler.request.IdentifyingCodeLoginParam;
import com.netherfire.server.handler.response.Response;
import com.netherfire.server.handler.result.RestReturnVO;
import com.netherfire.server.handler.result.ResultCode;
import com.netherfire.server.handler.result.VisitorLoginReturn;
import com.netherfire.server.manager.pojo.CacheUserInfo;
import com.netherfire.server.server.LoginServer;
import com.netherfire.server.utils.ParamsUtil;
import com.netherfire.server.utils.UtilsIO;
import com.netherfire.server.vo.IdentifyingCode;

/**
 * 验证码登录
 * @author JavaServer
 *
 */
public class IdentifyingCodeLoginHandler extends BaseHandler {
	
	public static Logger log = LogManager.getLogger(IdentifyingCodeLoginHandler.class);

	@Override
	public void handlePost(ChannelHandlerContext ctx, FullHttpRequest request, boolean iskeepAlive) {
		try {
			ByteBuf byteBuf = request.content();
			String data = UtilsIO.byteBufToStr(byteBuf);
			Map<String,String> map = ParamsUtil.getParam(data);
			IdentifyingCodeLoginParam form = new IdentifyingCodeLoginParam();
			
			form.setValue(map);
			
			if(StringUtils.isNullOrEmpty(form.getPhoneNum()) || StringUtils.isNullOrEmpty(form.getCode())){
				log.info("当前用户发送的 参数 有空的:当前参数 "+data);
				return;
			}
			IdentifyingCode identifyingCode = Global.getInstance().getIdentifyingCodeMap().get(form.getPhoneNum().trim());
			//判断是否过期
			if(new Date().getTime() >= identifyingCode.getOverdueTime()){
				log.info("当前用户发送的 验证码 已经过期 需要重新发送："+data+" -- 过期的日期:"+identifyingCode.getOverdueTime());
				return;
			}
			
			//判断是否匹配
			if(!form.getCode().trim().equals(identifyingCode.getCode())){
				log.info("当前用户发送的 验证码 不匹配："+data+" -- "+identifyingCode.toString());
				return;
			}
			
			
			
			//去登陆
			
			
			log.info(data);
			
			asyTask.addSynTask(() -> {
				try{
					RestReturnVO vo = new RestReturnVO();
					vo.setErrorCode(ResultCode.FAIL);
					
					 CacheUserInfo info = LoginServer.phoneNumLogin(map, "");
					 
					 if(info != null){
						 VisitorLoginReturn vlr = new VisitorLoginReturn();
						 vlr.setIp(info.getIp());
						 vlr.setPort(info.getPort());
						 vlr.setUserCode(info.getUserCode());
						 vlr.setUserId(info.getUserInfo().getId());
						 vo.setErrorCode(ResultCode.SUCCESS);
						 vo.setResult(vlr);
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
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	
	
	
	
	
	
}
