package com.netherfire.server.handler.imple;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.io.File;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netherfire.server.Global;
import com.netherfire.server.db.UserInfoDaoServer;
import com.netherfire.server.db.entity.UserInfo;
import com.netherfire.server.handler.BaseHandler;
import com.netherfire.server.handler.response.Response;
import com.netherfire.server.handler.result.RestReturnVO;
import com.netherfire.server.handler.result.ResultCode;
import com.netherfire.server.handler.service.PullUserService;
import com.netherfire.server.utils.UtilsIO;
import com.netherfire.server.vo.WeixinUserInfo;

public class WeixinUserInfoHandler extends BaseHandler {
	
	Logger logger = LogManager.getLogger(WeixinURLHandler.class);
	
	@Override
	public void handlePost(ChannelHandlerContext ctx, FullHttpRequest request, boolean iskeepAlive) {
		try{
			ByteBuf byteBuf = request.content();
			String data = UtilsIO.byteBufToStr(byteBuf);
			JSONObject jb = JSONObject.parseObject(data);
			String userUrl = jb.getString("userUrl");
			logger.info("=============================最终获取用户信息的URL:"+userUrl);
			userUrl = new String(Base64.getDecoder().decode(userUrl.getBytes()));
			WeixinUserInfo weixinUserInfo = PullUserService.getUserInfoInvoke(request,userUrl);
			
			
//			String ss = "{\"openid\":\"om862wFAMcvzKdm7WGPeG2na2zDo\",\"nickname\":\"大飞\",\"sex\":1,\"language\":\"zh_CN\",\"city\":\"Haidian\",\"province\":\"Beijing\",\"country\":\"China\",\"headimgurl\":\"http://thirdwx.qlogo.cn/mmopen/vi_32/hszAoBBmQYC5a3tDYdycSxfickO2wjxYtUZib6RebGgBic92icYSxbBAmatLtq9DWInDR2g04HWQJxmQGwcvyqEHmg/132\",\"privilege\":[]}";
//			WeixinUserInfo weixinUserInfo = JSONObject.parseObject(ss, WeixinUserInfo.class);
			
			//把微信头像下载下来，存起来然后 返回一个新的路径
			String newPath = PullUserService.download(weixinUserInfo.getHeadimgurl(), Global.getInstance().getConfig().getUpdateWeixinPath());
			logger.info("=============================newPath:"+newPath);
			UserInfo userInfo = UserInfoDaoServer.userInfoByUsername(weixinUserInfo.getOpenid());
			if(userInfo != null){
				logger.info("=============================userInfo.getImg():"+userInfo.getImg());
				String oldname = userInfo.getImg().substring(userInfo.getImg().lastIndexOf("/"));
				logger.info("=============================oldname:"+oldname);
				File file = new File(Global.getInstance().getConfig().getUpdateWeixinPath()+oldname);
				if(file.exists()){
					file.delete();
				}
			}
			
			logger.info("=============================weixinUserInfo.setHeadimgurl:"+Global.getInstance().getConfig().getCdnDomain()+"/weixin/"+newPath);
			weixinUserInfo.setHeadimgurl("http://"+Global.getInstance().getConfig().getCdnDomain()+"/weixin/"+newPath);
			RestReturnVO vo = new RestReturnVO();
			vo.setErrorCode(ResultCode.SUCCESS);
			JSONObject re = new JSONObject();
			re.put("weixinUserInfo", weixinUserInfo);
			vo.setResult(re);
			
			FullHttpResponse response = Response.build(JSON.toJSONBytes(vo, features),iskeepAlive);
			response.headers().set("Access-Control-Allow-Origin","*"); // 跨域  
			ctx.write(response).addListener(ChannelFutureListener.CLOSE);
	        ctx.flush();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}

}
