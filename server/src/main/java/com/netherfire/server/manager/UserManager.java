package com.netherfire.server.manager;

import java.util.concurrent.ConcurrentHashMap;

import com.netherfire.server.manager.pojo.CacheUserInfo;


public class UserManager {

	private static final UserManager manager = new UserManager();
	
	private UserManager(){}
	
	public static UserManager getInstance(){
		return manager;
	}
	
	private ConcurrentHashMap<Long,CacheUserInfo> userCacheMap = new ConcurrentHashMap<Long,CacheUserInfo>();
	private ConcurrentHashMap<Long,CacheUserInfo> noRemoveUserCacheMap = new ConcurrentHashMap<Long,CacheUserInfo>();
	
	
	public void addUser(CacheUserInfo cache,long id){
		userCacheMap.put(id, cache);
		noRemoveUserCacheMap.put(id, cache);
	}
	
	public void removeUser(long id){
		userCacheMap.remove(id);
	}
	
	
	public CacheUserInfo getUserInfo(long id){
		return userCacheMap.get(id);
	}
	public CacheUserInfo getUserInfoFromNoRemoveCacheMap(long id){
		return noRemoveUserCacheMap.get(id);
	}
	
}
