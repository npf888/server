package com.netherfire.server.server;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;
import com.netherfire.server.Global;
import com.netherfire.server.config.Config;
import com.netherfire.server.db.UserInfoDaoServer;
import com.netherfire.server.db.createid.CreateUserInfoID;
import com.netherfire.server.db.entity.UserInfo;
import com.netherfire.server.manager.UserManager;
import com.netherfire.server.manager.pojo.CacheUserInfo;
import com.netherfire.server.server.pojo.ServerInfo;
import com.netherfire.server.utils.MathUtils;

/**
 * 
 * @author GuoJunWei
 *
 */
public class LoginServer {
	
	public static Logger log = LogManager.getLogger(LoginServer.class);
	
	
	/**
	 * 锁定检查
	 * @param info
	 * @return  true:角色锁定
	 */
	public static boolean ifLock(UserInfo info){
		
		if(info.getLockStatus() == Config.UNLOCK){
			return false;
		}
		
		//秒
		long time = System.currentTimeMillis()/1000;
		
		return time < info.getMuteTime();
	}
	
	/**
	 * 角色登陆
	 * @param info
	 * @param serverId
	 */
	public static CacheUserInfo login(UserInfo info,String serverId){
		
		ServerInfo  serverInfo;
		if(serverId.equals("")){
			serverInfo = ServerServer.bestServer(info);
		}else{
			serverInfo = ServerServer.getServerByServerId(info,serverId);
		}
		
		if(serverInfo == null){
			log.debug(" 服务器请求错误 serverId = "+serverId);
			return null;
		}
		
		String userCode = String.valueOf(System.currentTimeMillis());
		
		CacheUserInfo cache = new CacheUserInfo();
		cache.setIp(serverInfo.getIp());
		cache.setPort(serverInfo.getPort());
		cache.setUserCode(userCode);
		cache.setUserInfo(info);
		cache.setServerId(Integer.valueOf(serverInfo.getServerId()));
		
		//放入缓存
		UserManager.getInstance().addUser(cache, info.getId());
		return cache;
	}
	
	
	
	
	/**
	 * 修改玩家头像
	 * @param roleid
	 * @param img
	 * @return true:更新成功
	 */
	public static boolean changeUserImg(String roleId,String img){
		boolean fly = UserInfoDaoServer.getUserInfoById(Long.valueOf(roleId));
		if(fly){
			UserInfoDaoServer.updateImg(Long.valueOf(roleId), img);
		}
		return fly;
	}
	
	public static boolean checkIdentifyingCode(String deviceMac){
		if(StringUtils.isNullOrEmpty(deviceMac)){
			return false;
		}
		UserInfo userInfo =	UserInfoDaoServer.userInfoByDeviceMac(deviceMac);
		if(userInfo == null){
			return false;
		}
		return true;
	}
	
	
	/**
	 * 机器人登陆
	 * @param deviceMac
	 * @param serverId
	 * @return
	 */
	public static CacheUserInfo visitorLogin(Map<String,String> map,String serverId){
		
		String deviceMac = map.get("deviceMac");
		String deviceId = map.get("deviceId");
		String macAddress = map.get("macAddress");
		String androidId = map.get("androidId");
		String img = map.get("img");
		String channel = map.get("channel");
		String username = map.get("username");
		String nickname = map.get("nickname");
		//渠道不为空  但是 用户名确实空的 这样传值就有问题 ，
		if(!StringUtils.isNullOrEmpty(channel) && StringUtils.isNullOrEmpty(username)){
			return null;
		}
		
		
		UserInfo userInfo =	null;
		if(StringUtils.isNullOrEmpty(channel)){//C#登录   用deviceMac查用户
			userInfo =	UserInfoDaoServer.userInfoByDeviceMac(deviceMac);
		}else{
			userInfo =	UserInfoDaoServer.userInfoByUsernameAndChannel(username, channel);
		}
		
		if(userInfo == null){
			long time = System.currentTimeMillis();
			userInfo = new UserInfo();
			long id = CreateUserInfoID.getInstance().getUserInfoMaxId();
			userInfo.setId(id);
//			userInfo.setName("Guest"+id);
			userInfo.setName(id+"");
			if(StringUtils.isNullOrEmpty(channel)){
				userInfo.setChannel("c#_client");//C#登录默认渠道是0
			}else{
				userInfo.setChannel(channel.trim());//传过来的第三方渠道
				userInfo.setUsername(username.trim());//传过来的第三方 用户名
				userInfo.setName(nickname.trim());//传过来的第三方 昵称
			}
			if(StringUtils.isNullOrEmpty(img)){
				userInfo.setImg("head_A.png".replace("A", String.valueOf(MathUtils.random(1,Global.getInstance().getConfig().getDefaultImages()))));
			}else{
				userInfo.setImg(img);
			}
			
			
			
			userInfo.setAccountId("-"+id);
			userInfo.setFacebookId("-1");
			userInfo.setJoinTime(time);
			userInfo.setLastLoginTime(0);
			if(!StringUtils.isNullOrEmpty(deviceMac) && deviceMac.contains("Robot_")){
				userInfo.setRole(Global.getInstance().getConfig().RobotRole);//2机器人
			}else{
				userInfo.setRole(Global.getInstance().getConfig().NormalRole);//普通的人
			}
			
			userInfo.setLockStatus(0);
			userInfo.setMuteTime(0);
			userInfo.setMuteReason("");
			
			
			userInfo.setDeviceMac(deviceMac);
			userInfo.setTwitterId("-1");
			userInfo.setAndroidId(androidId);
			userInfo.setMacAddress(macAddress);
			userInfo.setDeviceId(deviceId);
			UserInfoDaoServer.saveUserInfo(userInfo);
		}else{
			if(!StringUtils.isNullOrEmpty(channel)){
				userInfo.setChannel(channel.trim());//传过来的第三方渠道
			}
			if(!StringUtils.isNullOrEmpty(username)){
				userInfo.setUsername(username.trim());//传过来的第三方 用户名
			}
			if(!StringUtils.isNullOrEmpty(nickname)){
				userInfo.setName(nickname.trim());//传过来的第三方 昵称
			}
			if(!StringUtils.isNullOrEmpty(img)){
				userInfo.setImg(img);
			}
			
			UserInfoDaoServer.update(userInfo);
		}
		 return login(userInfo,serverId);
	}
	
	
	/**
	 * 手机号登录
	 * @param map
	 * @param serverId
	 * @return
	 */
	public static CacheUserInfo phoneNumLogin(Map<String,String> map,String serverId){
		
		String deviceMac = map.get("deviceMac");
		String deviceId = map.get("deviceId");
		String macAddress = map.get("macAddress");
		String androidId = map.get("androidId");
		String phoneNum = map.get("phoneNum");
		
		UserInfo userInfo = UserInfoDaoServer.userInfoByPhoneNum(phoneNum);
		
		if(userInfo == null){
			long time = System.currentTimeMillis();
			userInfo = new UserInfo();
			long id = CreateUserInfoID.getInstance().getUserInfoMaxId();
			userInfo.setId(id);
//			userInfo.setName("Guest"+id);
			userInfo.setName(id+"");
			userInfo.setUsername(null);
			userInfo.setImg("head_A.png".replace("A", String.valueOf(MathUtils.random(1,Global.getInstance().getConfig().getDefaultImages()))));
			userInfo.setChannel("c#_client");//C#登录默认渠道是0
			userInfo.setAccountId("-"+id);
			userInfo.setFacebookId("-1");
			userInfo.setJoinTime(time);
			userInfo.setLastLoginTime(0);
			if(!StringUtils.isNullOrEmpty(deviceMac) && deviceMac.contains("Robot_")){
				userInfo.setRole(Global.getInstance().getConfig().RobotRole);//2机器人
			}else{
				userInfo.setRole(Global.getInstance().getConfig().NormalRole);//普通的人
			}
			
			userInfo.setLockStatus(0);
			userInfo.setMuteTime(0);
			userInfo.setMuteReason("");
			
			
			userInfo.setPhoneNum(phoneNum);
			userInfo.setDeviceMac(deviceMac);
			userInfo.setTwitterId("-1");
			userInfo.setAndroidId(androidId);
			userInfo.setMacAddress(macAddress);
			userInfo.setDeviceId(deviceId);
			UserInfoDaoServer.saveUserInfo(userInfo);
		}else{
			userInfo.setPhoneNum(phoneNum);
			userInfo.setDeviceMac(phoneNum);
			userInfo.setDeviceMac(deviceMac);
			userInfo.setAndroidId(androidId);
			userInfo.setMacAddress(macAddress);
			userInfo.setDeviceId(deviceId);
			UserInfoDaoServer.update(userInfo);
		}
		return login(userInfo,serverId);
	}

	/**
	 * h5用户注册 判断用户是否存在
	 */
	
	public static UserInfo HtmlUserExist(String username,String channel){
		return UserInfoDaoServer.userInfoByUsernameAndChannel(username,channel);
	}
	/**
	 * h5用户登录  判断用户是否存在
	 */
	
	public static UserInfo HtmlUserExist(String username,String password,String channel){
		return UserInfoDaoServer.userInfoByUsernameAndPasswordAndChannel(username,password,channel);
	}
	/**
	 * 微信登录   判断用户是否存在
	 */
	
	public static UserInfo WeixinUserExist(String username,String password,String channel){
		return UserInfoDaoServer.userInfoByUsernameAndChannel(username,channel);
	}
	
	/**
	 * h5用户 注册
	 * @param deviceMac
	 * @param serverId
	 * @return
	 */
	public static boolean HtmlUserRegister(JSONObject jb,String username,String password,String channel,String serverId){
		
		String deviceMac = (String)jb.get("deviceMac");
		String deviceId = (String)jb.get("deviceId");
		String macAddress = (String)jb.get("macAddress");
		String androidId = (String)jb.get("androidId");
		
		long time = System.currentTimeMillis();
		UserInfo userInfo = new UserInfo();
		long id = CreateUserInfoID.getInstance().getUserInfoMaxId();
		userInfo.setId(id);
		userInfo.setName(username);
		userInfo.setUsername(username);
		userInfo.setPassword(password);
		userInfo.setChannel(channel);
		userInfo.setImg("head_A.png".replace("A", String.valueOf(MathUtils.random(1,Global.getInstance().getConfig().getDefaultImages()))));
		userInfo.setAccountId("-"+id);
		userInfo.setFacebookId("-1");
		userInfo.setJoinTime(time);
		userInfo.setLastLoginTime(0);
		userInfo.setRole(Global.getInstance().getConfig().NormalRole);//普通的人
		
		userInfo.setLockStatus(0);
		userInfo.setMuteTime(0);
		userInfo.setMuteReason("");
		userInfo.setDeviceMac(deviceMac);
		userInfo.setTwitterId("-1");
		userInfo.setAndroidId(androidId);
		userInfo.setMacAddress(macAddress);
		userInfo.setDeviceId(deviceId);
		UserInfoDaoServer.saveUserInfo(userInfo);
		
		return true;
	}
	
	/**
	 * h5用户 登录
	 * @param deviceMac
	 * @param serverId
	 * @return
	 */
	public static CacheUserInfo HtmlUserLogin(UserInfo userInfo,String serverId){
		return login(userInfo,serverId);
	}
	
	/**
	 * 创建用户
	 */
	
	public static UserInfo saveOrUpdateFBUserInfo(boolean save,JSONObject jb){
		UserInfo userInfo = new UserInfo();
		if(save){
			long time = System.currentTimeMillis();
			long id = CreateUserInfoID.getInstance().getUserInfoMaxId();
			userInfo.setId(id);
			userInfo.setAccountId("-"+id);
			userInfo.setJoinTime(time);
		}
		
		userInfo.setUpdateFbInfo(true);
		
		userInfo.setChannel(jb.getString("channel"));
		userInfo.setFacebookId(jb.getString("facebookId"));
		userInfo.setName(jb.getString("nickname"));
		userInfo.setImg(jb.getString("img"));
		userInfo.setLastLoginTime(0);
		userInfo.setRole(Global.getInstance().getConfig().NormalRole);//普通的人
		
		userInfo.setLockStatus(0);
		userInfo.setMuteTime(0);
		userInfo.setMuteReason("");
		userInfo.setDeviceMac("");
		userInfo.setTwitterId("-1");
		userInfo.setAndroidId("");
		userInfo.setMacAddress("");
		userInfo.setDeviceId("");
		
		if(save){
			UserInfoDaoServer.saveUserInfo(userInfo);
		}else{
			UserInfoDaoServer.update(userInfo);
		}
		
		return userInfo;
	}
	
}
