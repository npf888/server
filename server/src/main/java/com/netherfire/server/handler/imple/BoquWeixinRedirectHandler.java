package com.netherfire.server.handler.imple;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.io.File;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;
import com.netherfire.server.Global;
import com.netherfire.server.db.UserInfoDaoServer;
import com.netherfire.server.db.entity.UserInfo;
import com.netherfire.server.handler.BaseHandler;
import com.netherfire.server.handler.response.Response;
import com.netherfire.server.handler.service.PullUserService;
import com.netherfire.server.manager.pojo.CacheUserInfo;
import com.netherfire.server.server.LoginServer;
import com.netherfire.server.utils.HttpsUtil;
import com.netherfire.server.utils.ThirdPartyUtil;
import com.netherfire.server.vo.BoquWeixinUser;

/**
 * 对接 博趣那边的微信登录，  微信登录在博趣那边
 * @author JavaServer
 *
 *
 * 初始调用 博文的入口
 * http://www.bo-qu.com/console/oauth/official-account?client_id=8&client_secret=1Pkbu1X9mxjTtBw9YscmniqoUV92TAZxpslxD45V&
 * timestamp=1521688879770&redirect_url=http://dice.bo-qu.com:80/api/boquweixin/redirect.json
 * 
 * 
 * http://www.bo-qu.com/console/oauth/official-account?client_id=8&client_secret=1Pkbu1X9mxjTtBw9YscmniqoUV92TAZxpslxD45V&timestamp=1521688879770&redirect_url=http://sv.netherfire.com/api/boquweixin/redirect.json
 */
public class BoquWeixinRedirectHandler extends BaseHandler {
	
	public static Logger log = LogManager.getLogger(BoquWeixinRedirectHandler.class);

	@Override
	public void handleGet(ChannelHandlerContext ctx,Map<String, List<String>> parameter, FullHttpRequest request,boolean iskeepAlive) {
		String tokenType = "";
		String expiresIn = "";
		String accessToken = "";
		if(parameter.containsKey("token_type")){
			tokenType = parameter.get("token_type").get(0);
		}
		if(parameter.containsKey("expires_in")){
			expiresIn = parameter.get("expires_in").get(0);
		}
		if(parameter.containsKey("access_token")){
			accessToken = parameter.get("access_token").get(0);
		}
		//任何一个等于空 或者没有值 都不行
		if(StringUtils.isNullOrEmpty(tokenType) || StringUtils.isNullOrEmpty(expiresIn) || StringUtils.isNullOrEmpty(accessToken)){
			log.info("tokenType"+tokenType+" expiresIn"+expiresIn+" "+accessToken+" 只有问题");
			return;
		}
		
		//查看token有没有过期  过期了 还得刷新
		UserInfo userInfo =	UserInfoDaoServer.userInfoByAccessToken(accessToken);
		if(userInfo != null){
			ThirdPartyUtil.postLogin(ThirdPartyUtil.HangZhou,userInfo);
			userInfo=UserInfoDaoServer.userInfoByUsername(userInfo.getUsername());
			accessToken=userInfo.getAccessToken();
		}
		
		
		//查询用户
		Map<String,String> header = new HashMap<String,String>();
		header.put("Authorization", tokenType+" "+accessToken);
		String res = HttpsUtil.doGetWithHeader(Global.getInstance().getConfig().getBoquPay().getUserUrl(),new HashMap<String,String>(),header, false);
		
		BoquWeixinUser boquWeixinUser = JSONObject.parseObject(res, BoquWeixinUser.class);
		log.info("博趣微信返回："+res);
		
		
		
		//登录
		Map<String,String> map = new HashMap<String,String>();
		map.put("channel", ThirdPartyUtil.HangZhou);
		map.put("username", boquWeixinUser.getUsername());
		map.put("nickname", boquWeixinUser.getNickname());
		
		
		//把微信头像下载下来，存起来然后 返回一个新的路径
		String newPath = PullUserService.download(boquWeixinUser.getAvatar(), Global.getInstance().getConfig().getUpdateWeixinPath());
		log.info("=============================newPath:"+newPath);
		if(userInfo != null){
			log.info("=============================userInfo.getImg():"+userInfo.getImg());
			String oldname = userInfo.getImg().substring(userInfo.getImg().lastIndexOf("/"));
			log.info("=============================oldname:"+oldname);
			File file = new File(Global.getInstance().getConfig().getUpdateWeixinPath()+oldname);
			if(file.exists()){
				file.delete();
			}
		}
		String img = "http://"+Global.getInstance().getConfig().getCdnDomain()+"/weixin/"+newPath;
		log.info("=============================weixinUserInfo.setHeadimgurl:"+img);
		map.put("img", img);
		CacheUserInfo info = LoginServer.visitorLogin(map, "");
		
		//把token 存上
		if(userInfo == null){
			long second = Long.valueOf(expiresIn);//秒
			long secondMill = new Date().getTime()+second*1000;
			//把token更新一下
			UserInfoDaoServer.updateAccessToken(info.getUserInfo().getId(),tokenType, accessToken,secondMill);
		}
		
		//替换成新生成的头像
		
		boquWeixinUser.setAvatar(img);
		
	
		String base64Res = Base64.getEncoder().encodeToString(JSONObject.toJSONString(boquWeixinUser).getBytes());
		
		String redirectUri = Global.getInstance().getConfig().getWeixinRedirectUrl();
		String ss =  "<script type=\"text/javascript\" >  window.location.href=\""+redirectUri+"?userUrl="+base64Res+"\"; </script>";
		FullHttpResponse response = Response.buildHtml(ss.getBytes(),iskeepAlive);
		response.headers().set("Access-Control-Allow-Origin","*"); // 跨域  
		ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        ctx.flush();
			
		
		
	}

}
