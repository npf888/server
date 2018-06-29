package com.netherfire.server.server;

import java.util.List;
import java.util.Map;

import com.netherfire.server.config.Config;
import com.netherfire.server.db.ClientResourceVersionDaoServer;
import com.netherfire.server.db.ClientVersionDaoServer;
import com.netherfire.server.handler.result.ClientVersionReturn;
import com.netherfire.server.redis.RedisProvider;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class ClientversionServer {

	public static ClientVersionReturn getVersion(){
		
		ClientVersionReturn vo = new ClientVersionReturn();
		
		JedisPool jedisPool = RedisProvider.getInstance().getJedisPool();
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			
			//资源版本号版本
			String resourceClientVersion = jedis.get(Config.resourceClientVersionKey);
			if(resourceClientVersion == null){
				int resourceVersion = ClientResourceVersionDaoServer.getResourceVersion();
				jedis.set(Config.resourceClientVersionKey, String.valueOf(resourceVersion));
				vo.setResourceVersion(resourceVersion);
			}else{
				vo.setResourceVersion(Integer.valueOf(resourceClientVersion));
			}
			//获取客户端版本号
			List<String> list = jedis.hmget(Config.clientKey, Config.clientMinVersionKey,Config.clientVersionKey);
			
			if(list.get(0) == null || list.get(1) == null){
				Map<String,String> map = ClientVersionDaoServer.getClientVersion();	
				jedis.hmset(Config.clientKey,map);
				vo.setMinVersion(map.get(Config.clientMinVersionKey));
				vo.setVersion(map.get(Config.clientVersionKey));
			}else{
				vo.setMinVersion(list.get(0));
				vo.setVersion(list.get(1));
			}
		} finally {
			if(jedis!=null) {jedis.close();}
		}
		
		return vo;
	}
}
