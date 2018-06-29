package com.netherfire.server.db.createid;

import com.netherfire.server.Global;
import com.netherfire.server.db.UserInfoDaoServer;

/**
 * UserInfo ID 生成器
 * @author 郭君伟
 *
 */
public class CreateUserInfoID {
	
	private static final CreateUserInfoID info = new CreateUserInfoID();
	
	private long iD = Global.getInstance().getConfig().getUserinfo_maxi_id();
	
	private CreateUserInfoID(){}
	
	public static CreateUserInfoID getInstance(){
		return info;
	}
	
	public void init(){
		long maxId = UserInfoDaoServer.getUserInfoMaxId();
//		if(maxId != 0){
		if(maxId > 618){
			iD = maxId;
		}
	}
	
	public synchronized long getUserInfoMaxId(){
		return ++iD;
	}

}
