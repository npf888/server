package com.netherfire.server.handler.imple;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.mysql.jdbc.StringUtils;
import com.netherfire.server.Global;
import com.netherfire.server.handler.BaseHandler;
import com.netherfire.server.handler.response.Response;
import com.netherfire.server.handler.result.RestReturnVO;
import com.netherfire.server.handler.result.ResultCode;
import com.netherfire.server.utils.BoquMsgUtils;
import com.netherfire.server.utils.ParamsUtil;
import com.netherfire.server.utils.UtilsIO;
import com.netherfire.server.vo.IdentifyingCode;
import com.netherfire.server.vo.PhoneNum;

/**
 * 阿里云发送短信的接口
 * @author JavaServer
 *
 */
public class AliSendMsgHandler extends BaseHandler {
	
	public static Logger log = LogManager.getLogger(AliSendMsgHandler.class);

	@Override
	public void handlePost(ChannelHandlerContext ctx, FullHttpRequest request, boolean iskeepAlive) {
		try {
			ByteBuf byteBuf = request.content();
			String data = UtilsIO.byteBufToStr(byteBuf);
			Map<String,String> map = ParamsUtil.getParam(data);
			PhoneNum form = new PhoneNum();
			form.setValue(map);
			
			if(StringUtils.isNullOrEmpty(form.getPhoneNum())){
				log.info("当前用户发送的 手机号为空:当前参数  "+data);
				return;
			}
			
//			String num = AliMsgUtils.sendMsg(form.getPhoneNum().trim());
			String num = BoquMsgUtils.sendMsg(form.getPhoneNum().trim());
			log.info("当前用户发送的 验证码:"+num);
			if(StringUtils.isNullOrEmpty(num)){
				RestReturnVO vo = new RestReturnVO();
				vo.setErrorCode(ResultCode.SUCCESS);
				FullHttpResponse response = Response.build(JSON.toJSONBytes(vo, features),iskeepAlive);
				response.headers().set("Access-Control-Allow-Origin","*"); // 跨域  
				ctx.write(response).addListener(ChannelFutureListener.CLOSE);
		        ctx.flush();
				return;
			}
			IdentifyingCode identifyingCode = new IdentifyingCode();
			identifyingCode.setOverdueTime(new Date().getTime()+5*60*1000);
			identifyingCode.setCode(num);
			Global.getInstance().getIdentifyingCodeMap().put(form.getPhoneNum(), identifyingCode);
			
			
			RestReturnVO vo = new RestReturnVO();
			vo.setErrorCode(ResultCode.SUCCESS);
			FullHttpResponse response = Response.build(JSON.toJSONBytes(vo, features),iskeepAlive);
			response.headers().set("Access-Control-Allow-Origin","*"); // 跨域  
			ctx.write(response).addListener(ChannelFutureListener.CLOSE);
	        ctx.flush();
	        log.info(JSON.toJSONString(vo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	
	
	
	
	
	
}
