package com.netherfire.server.db;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysql.jdbc.StringUtils;
import com.netherfire.server.Global;
import com.netherfire.server.db.createid.CreateUserInfoID;
import com.netherfire.server.db.entity.UserInfo;
import com.netherfire.server.db.factory.DataProvider;
import com.netherfire.server.db.mapper.UserInfoMapper;
import com.netherfire.server.utils.MathUtils;

public class UserInfoDaoServer {
	private static final Logger logger = LogManager.getLogger(UserInfoDaoServer.class);
	/**
	 * 根据FBid 查询userId
	 * 
	 * @param list
	 * @return
	 */
	public static List<Long> getUserId(List<Long> list) {
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {
			UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
			List<Long> userIdList = mapper.getIdsByFbId(list);

			return userIdList;
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			sqlSession.close();
		}
		return null;

	}

	/**
	 * 获取对象
	 * 
	 * @param accountId
	 * @return
	 */
	public static UserInfo userInfoByAccountId(String accountId) {
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {
			UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
			UserInfo info = mapper.getUserInfoByAccountId(accountId);

			return info;
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			sqlSession.close();
		}

		return null;
	}

	/**
	 * 获取对象 通过手机号
	 * 
	 * @param deviceMac
	 * @return
	 */
	public static UserInfo userInfoByPhoneNum(String phoneNum) {
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {
			UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
			UserInfo info = mapper.getUserInfoByPhoneNum(phoneNum);
			logger.info("phoneNum: "+phoneNum+">>>"+info);
			return info;
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			sqlSession.close();
		}
		return null;
		
	}
	/**
	 * 获取对象
	 * 
	 * @param deviceMac
	 * @return
	 */
	public static UserInfo userInfoByDeviceMac(String deviceMac) {
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {
			UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
			UserInfo info = mapper.getUserInfoByDeviceMac(deviceMac);
			logger.info("deviceMac: "+deviceMac+">>>"+info);
			return info;
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			sqlSession.close();
		}
		return null;

	}

	/**
	 * 通过 facebookId查询 userInfo 是否存在
	 * @param facebookId
	 * @return
	 */
	public static UserInfo facebookExist(String facebookId){
		UserInfo ui = null;
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {
			UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
			ui = mapper.getUserByFacebookId(facebookId);
		}catch(Exception e){
			logger.error("查询facebook错误："+e);
		}finally{
			sqlSession.close();
		}
		
		return ui;
	}
	
	/**
	 *如果之前以游客登录, 绑定游客账号与facebook账号
	 * 
	 * @param facebookId
	 * @param deviceMac
	 * @return
	 */
	public static UserInfo selectExistedFbOrBindFb(String facebookId, String deviceMac, Map<String, String>paramMap) {
		UserInfo ui = null;
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {
			UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
			ui = mapper.getUserByFacebookId(facebookId);
			if(ui!=null)
			{
				logger.info("existed facebookId login, facebookId: "+facebookId+", deviceMac:"+deviceMac);
//				if(!ui.getDeviceMac().equals(deviceMac))
//				{
//					ui.setDeviceMac(deviceMac);
//					mapper.updateFacebookId(ui.getAccountId(), facebookId);
//					sqlSession.commit();
//					logger.info("existed facebookId login, facebookId: "+facebookId+", bind new deviceMac:"+deviceMac);
//				}
				String name = paramMap.get("name");
				ui.setName(name);
				if(!StringUtils.isNullOrEmpty(paramMap.get("head"))){
					String head = paramMap.get("head");
					String head1 = head.replaceAll("--00--", "&");
					String head2 = head1.replaceAll("------", "=");
					ui.setImg(head2);
				}
				ui.setUpdateFbInfo(true);
				mapper.updateFacebookInfo(ui.getAccountId(), facebookId, name,ui.getImg());
				sqlSession.commit();
				return ui;
			}
			ui = mapper.getUserInfoByDeviceMac(deviceMac);
			if (ui != null) {
				if(ui.getFacebookId().equals("-1"))
				{
					ui.setFacebookId(facebookId);
					String name = paramMap.get("name");
					ui.setName(name);
					ui.setUpdateFbInfo(true);
					ui.setImg(paramMap.get("head"));
					mapper.updateFacebookInfo(ui.getAccountId(), facebookId, name,ui.getImg());
					sqlSession.commit();
					logger.info("successfully bind for facebookId: "+facebookId+", deviceMac:"+deviceMac);
				}
				else
				{
					logger.info("deviceMac:"+deviceMac+" already bind, will create new userinfo");
					ui = null;
					deviceMac = "";
				}
			}
			
			if(ui==null)
			{
				String name = paramMap.get("name");
				String deviceId = paramMap.get("deviceId");
				String macAddress = paramMap.get("macAddress");
				String androidId = paramMap.get("androidId");
//				UserInfoDaoServer.clearDeviceMacOfOldFbIdForNewFbId(deviceMac, fbId);
				//创建
				ui = new UserInfo();
				long id = CreateUserInfoID.getInstance().getUserInfoMaxId();
				ui.setId(id);
				ui.setFacebookId(facebookId);
				ui.setAccountId("-"+id);
				ui.setName(name);
				ui.setChannel("c#_client");//C#登录默认渠道是0
				ui.setImg("head_A.png".replace("A", String.valueOf(MathUtils.random(1,Global.getInstance().getConfig().getDefaultImages()))));
				long time = System.currentTimeMillis();
				ui.setJoinTime(time);
				ui.setLastLoginTime(time);
				ui.setRole(0);//普通的人
				ui.setLockStatus(0);
				ui.setMuteTime(0);
				ui.setMuteReason("");
				ui.setTwitterId("-1");
				
				ui.setDeviceMac(deviceMac);
				
				ui.setDeviceId(deviceId);
				ui.setMacAddress(macAddress);
				ui.setAndroidId(androidId);
//				UserInfo info = UserInfoDaoServer.userInfoByDeviceMac(deviceMac);
//				
//				if(info == null){//保证驱动是唯一的
//					user.setDeviceMac(deviceMac);
//				}
//				//入库
				ui.setUpdateFbInfo(true);
				saveUserInfo(ui);
				logger.info("will create new userinfo for: "+id);
			}
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			sqlSession.close();
		}
		return ui;
	}

	/**
	 * 获取UserInfo 最大ID
	 * 
	 * @return
	 */
	public static Long getUserInfoMaxId() {
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {

			UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
			Long id = mapper.getMaxId();
			if (id == null) {
				return 0l;
			}

			return id;
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			sqlSession.close();
		}

		return 0l;
	}

	/**
	 * 保存新创建的UserInfo
	 * 
	 * @param info
	 */
	public static void saveUserInfo(UserInfo info) {
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {
			UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
			info.setName(filterEmoji(info.getName()));
			logger.info("============================================="+info.getName());
			mapper.saveUserInfo(info);
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			sqlSession.close();
		}

	}
	
	public static String filterEmoji(String source) { 
        if(source != null)
        {
            Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
            Matcher emojiMatcher = emoji.matcher(source);
            if ( emojiMatcher.find())
            {
                source = emojiMatcher.replaceAll("*");
                return source ;
            }
        return source;
       }
       return source; 
    }
	public static void update(UserInfo info) {
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {
			UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
			mapper.update(info);
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			sqlSession.close();
		}
		
	}
	
	public static void clearDeviceMacOfOldFbIdForNewFbId(String deviceMac, String faceBookId) {
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {
			UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
			UserInfo ui = mapper.getUserInfoByDeviceMac(deviceMac);
			if(ui!=null )
			{
				ui.setDeviceMac(null);
				mapper.updateDeviceMac(ui.getAccountId(), ui.getDeviceMac());
				sqlSession.commit();
				logger.info("will clear deviceMac :"+deviceMac+" of old fbId: "+ui.getFacebookId()+", for new fbId: "+faceBookId);
			}
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			sqlSession.close();
		}

	}


	/**
	 * 更新twitterId
	 * 
	 * @param accountId
	 * @param twitterId
	 */
	public static void updateTwitterId(String accountId, String twitterId) {
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {

			UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
			mapper.updateTwitterId(accountId, twitterId);
			sqlSession.commit();

		} catch (Exception e) {
			logger.error("", e);
		} finally {
			sqlSession.close();
		}

	}

	/**
	 * 
	 * @param id
	 * @return true:角色存在
	 */
	public static boolean getUserInfoById(long id) {
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {

			UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
			UserInfo info = mapper.getUserInfoById(id);

			return info != null;
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			sqlSession.close();
		}
		return false;
	}

	public static UserInfo userInfoById(long id) {
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {
			UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
			UserInfo info = mapper.getUserInfoById(id);
			return info;
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			sqlSession.close();
		}
		return null;
	}
	public static UserInfo userInfoByUsername(String username) {
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {
			UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
			UserInfo info = mapper.getUserByUsername(username);
			return info;
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			sqlSession.close();
		}
		return null;
	}
	public static UserInfo userInfoByAccessToken(String accessToken) {
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {
			UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
			UserInfo info = mapper.getUserByAccessToken(accessToken);
			return info;
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			sqlSession.close();
		}
		return null;
	}
	
	/**
	 * 更新图片
	 * 
	 * @param id
	 * @param img
	 */
	public static void updateAccessToken(long id, String tokenType,String accessToken,long expiresIn) {
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {
			UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
			mapper.updateAccessToken(id,tokenType, accessToken,expiresIn);
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			sqlSession.close();
		}

	}
	
	public static void resetFaceInfo(long id) {
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {
			UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
			mapper.resetUpdateFbInfo(id);
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			sqlSession.close();
		}

	}

	/**
	 * 通过用户名 获取对象
	 * 
	 * @param accountId
	 * @return
	 */
	public static UserInfo userInfoByUsernameAndChannel(String username,String channel) {
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {
			UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
			UserInfo info = mapper.getUserInfoByUsernameAndChannel(username,channel);
			return info;
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			sqlSession.close();
		}

		return null;
	}

	public static UserInfo userInfoByUsernameAndPasswordAndChannel(String username,String password,String channel) {
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {
			UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
			UserInfo info = mapper.getUserInfoByUsernameAndPasswordAndChannel(username,password,channel);
			return info;
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			sqlSession.close();
		}
		
		return null;
	}
	
	/**
	 * 更新图片
	 * 
	 * @param id
	 * @param img
	 */
	public static void updateImg(long id, String img) {
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {
			UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
			mapper.updateImg(id, img);
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			sqlSession.close();
		}

	}
	
}
