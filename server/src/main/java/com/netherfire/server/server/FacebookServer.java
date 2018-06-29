package com.netherfire.server.server;

import java.util.ArrayList;
import java.util.List;

import com.netherfire.server.db.UserInfoDaoServer;

/**
 * 
 * @author 郭君伟
 *
 */
public class FacebookServer {

	
	/**
	 * FB id 查找 角色ID
	 * @param list
	 * @return
	 */
	public static List<Long> getUserIdByFbId(List<Long> list){
		
		List<Long> userIdList = UserInfoDaoServer.getUserId(list);
		if(userIdList == null){
			userIdList = new ArrayList<Long>();
		}
		
		return userIdList;
	}
	
}
