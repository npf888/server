 package com.netherfire.server.db.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.netherfire.server.db.entity.UserInfo;

/**
 * 
 * 数据操作接口
 * @author GuoJunWei
 *
 */
public interface UserInfoMapper {
	
	
	public UserInfo getUserInfoByAccountId(String accountId);
	public UserInfo getUserInfoByUsernameAndChannel(String username,String channel);
	public UserInfo getUserInfoByUsernameAndPasswordAndChannel(String name,String password,String channel);
	
	public UserInfo getUserInfoByDeviceMac(String deviceMac);
	public UserInfo getUserInfoByPhoneNum(String phoneNum);
	
	public UserInfo getUserByFacebookId(String facebookId);
	
	public UserInfo getUserByUsername(String username);
	public UserInfo getUserByAccessToken(String accessToken);
	
	public List<Long> getIdsByFbId(List<Long> fbid);
	
	public void saveUserInfo(UserInfo info);
	
	public int updateDeviceMac(@Param(value="accountId")String accountId,@Param(value="deviceMac")String deviceMac);
	
	public int updateFacebookInfo(@Param(value="accountId")String accountId, 
			@Param(value="facebookId")String facebookId, 
			@Param(value="name")String name,
			@Param(value="img")String img 
			);
	
	public void updateTwitterId(@Param(value="accountId")String accountId,@Param(value="twitterId")String twitterId);
	
	public Long getMaxId();
	
	public UserInfo getUserInfoById(@Param(value="id") Long id);
	
	public void updateImg(@Param(value="id")Long id,@Param(value="img")String img);
	
	public void updateAccessToken(@Param(value="id")Long id,
			@Param(value="tokenType")String tokenType,
			@Param(value="accessToken")String accessToken,
			@Param(value="expiresIn")Long expiresIn);
	
	public void resetUpdateFbInfo(@Param(value="id")Long id);
	
	public void update(UserInfo userInfo);
	
 
}
