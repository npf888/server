package com.netherfire.server.utils;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.netherfire.server.db.entity.UserInfo;
import com.netherfire.server.thirdparty.Boqu;

/**
 * 请求第三方接口的 工具
 * @author JavaServer
 *
 */
public class ThirdPartyUtil {
	public static final String HangZhou = "hangzhou";
	public static Logger logger = LogManager.getLogger(ThirdPartyUtil.class);
	/**
	 * 博趣 平台
	 * 请求登录
	 */
	public static  void postLogin(String platForm,UserInfo userInfo){
		if(ThirdPartyUtil.HangZhou.equals(platForm)){
			Long expiresIn = userInfo.getExpiresIn();
			//为空 就去刷新token
			if(expiresIn == null){
				Boqu.postLogin(userInfo.getId());
				return;
			}
			//过期了
			long now = new Date().getTime();
			if(now>=expiresIn.longValue()){
				Boqu.postLogin(userInfo.getId());
				return;
			}
		}
	}
}
