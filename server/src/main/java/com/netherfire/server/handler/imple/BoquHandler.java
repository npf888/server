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
import com.netherfire.server.Global;
import com.netherfire.server.db.UserInfoDaoServer;
import com.netherfire.server.db.entity.UserInfo;
import com.netherfire.server.handler.BaseHandler;
import com.netherfire.server.handler.request.CheckBoquRechargeParam;
import com.netherfire.server.handler.request.CheckBoquRechargeRes;
import com.netherfire.server.handler.request.CheckBoquRechargeReturn;
import com.netherfire.server.handler.response.Response;
import com.netherfire.server.handler.result.RestReturnVO;
import com.netherfire.server.handler.result.ResultCode;
import com.netherfire.server.utils.HttpsUtil;
import com.netherfire.server.utils.UtilsIO;
/**
 * 博趣的 查询成功的订单是否成功
 * @author JavaServer
 *
 */
public class BoquHandler extends BaseHandler {
	
	public static Logger log = LogManager.getLogger(BoquHandler.class);

	@Override
	public void handlePost(ChannelHandlerContext ctx, FullHttpRequest request, boolean iskeepAlive) {
		try{
			ByteBuf byteBuf = request.content();
			String data = UtilsIO.byteBufToStr(byteBuf);
			
			CheckBoquRechargeParam form = JSON.parseObject(data, CheckBoquRechargeParam.class);
			
			log.info("[博趣验证]----------------------------博趣  传递给 博趣验证的 参数：："+JSON.toJSONString(form));
			
			asyTask.addSynTask(()->{
				
				RestReturnVO vo = new RestReturnVO();
				vo.setErrorCode(ResultCode.FAIL);
				
				
				UserInfo userInfo = UserInfoDaoServer.userInfoById(Long.valueOf(form.getUserId()));
				if(userInfo == null){//用户ID不对
					FullHttpResponse response = Response.build(JSON.toJSONBytes(vo, features),iskeepAlive);
					ctx.write(response).addListener(ChannelFutureListener.CLOSE);
			        ctx.flush();
			        log.info("[博趣验证]----------------------------博趣 传递给 博趣验证的 参数 userInfo 为 null ");
					return;
				}
				
				//获取 访问 博趣平台的token
				String accessToken = userInfo.getAccessToken();
				
				if(accessToken != null){
					
					 Map<String,String> params = new HashMap<String,String>();
					 
					 log.info("[博趣验证]----------------------------博趣 开始博趣验证：：当前的accessToken:"+accessToken);
					 params.put("access_token", accessToken);
					
					 String url = getUrl(form);
					 
					 log.info("[博趣验证]----------------------------开始博趣验证：：当前的URL:"+url);
					 if(url != null){
						 Map<String,String> headers = new HashMap<String,String>();
						 headers.put("Authorization", "Bearer "+userInfo.getAccessToken());
						 String result = HttpsUtil.doGetWithHeader(url, params,headers, true);
						 log.info("[博趣验证]----------------------------博趣验证结束：：返回值："+result);
						 if(result != null){
							 log.info("[博趣验证]----------------------------博趣验证结束：：返回值 不为空：");
							 CheckBoquRechargeReturn returnVo = JSON.parseObject(result, CheckBoquRechargeReturn.class);
							 if(returnVo.getStatus().equals("complete")){//完成了才算成功了
								 vo.setErrorCode(ResultCode.SUCCESS);
							 }
							 CheckBoquRechargeRes res = new CheckBoquRechargeRes();
							 res.setChannelOrderId(form.getChannelOrderId());
							 res.setOrderId(Long.valueOf(form.getOrderId()));
							 res.setProductId(form.getProductId());
							 vo.setResult(res);
						 }
					 }
				}
				
				FullHttpResponse response = Response.build(JSON.toJSONBytes(vo, features),iskeepAlive);
				ctx.write(response).addListener(ChannelFutureListener.CLOSE);
		        ctx.flush();
				
		        log.info(JSON.toJSONString(vo));
				
			});
		}catch(Exception e){
			log.error(e.toString());
		}
		
	}
	
	private String getUrl(CheckBoquRechargeParam form){
		 String url = Global.getInstance().getConfig().getBoquPay().getUrl();
		 StringBuilder sb = new StringBuilder(url);
		 sb.append("?order_id=").append(form.getChannelOrderId());
		 return sb.toString();
	}

}
