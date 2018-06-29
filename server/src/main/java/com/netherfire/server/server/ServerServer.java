package com.netherfire.server.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.netherfire.server.Global;
import com.netherfire.server.db.entity.UserInfo;
import com.netherfire.server.redis.RedisProvider;
import com.netherfire.server.server.pojo.ServerInfo;
import com.netherfire.server.utils.ArrayUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class ServerServer {
	private static final Logger logger = LogManager.getLogger(ServerServer.class);
	
	/**
	 * 随机服务器
	 * @param info
	 * @return
	 */
	public static ServerInfo bestServer(UserInfo info){
		List<ServerInfo> list = getAvailableServerList(info.getRole());
		if(list.isEmpty()){
			return null;
		}
		int[] serverWeights = new int[list.size()];
		
		for(int i = 0;i < list.size();i++){
			ServerInfo server = list.get(i);
			serverWeights[i] = server.getMaxNums() - server.getNums();
		}
		List<Integer> randlist = ArrayUtils.randomIndexByWeight(serverWeights,1,false);
		return list.get(randlist.get(0));
	}

	
	public static List<ServerInfo> getAvailableServerList(int role){
		List<ServerInfo> list = new ArrayList<>();
		
		JedisPool pool = RedisProvider.getInstance().getJedisPool();
		
		Jedis jedis = null;
		
		try{
			jedis = pool.getResource();
			//redis 获取服务器列表
			Map<String, String> map = jedis.hgetAll(Global.getInstance().getConfig().getRedisServerKey());
			
			for(Entry<String, String> en : map.entrySet()){
				
				ServerInfo serverInfo = JSON.parseObject(en.getValue(), ServerInfo.class); 
				
				 if(serverInfo.getStatus() == Global.getInstance().getConfig().getServerStatusStop()){
					 continue;
				 }
				 
				 if(serverInfo.getStatus() == Global.getInstance().getConfig().getServerStatusTest()){
					 if(role == Global.getInstance().getConfig().getNormalRole()){
						 continue;
					 }
				 } 
				if( Global.getInstance().getConfig().isBindIp()){
					serverInfo.setIp(Global.getInstance().getConfig().getBindingIp());
				}
				list.add(serverInfo);
			}
		}catch(Exception e){
			logger.error("", e);
			//错误处理
		}finally{
			if(jedis!=null)
			{
				jedis.close();
			}
		}
		return list;
	}
	
	public static ServerInfo getServerByServerId(UserInfo info,String serverId){
		List<ServerInfo> list = getAvailableServerList(info.getRole());
		if(list.isEmpty()){
			return null;
		}
		
		for(ServerInfo server : list){
			if(server.getServerId().equals(serverId)){
				return server;
			}
		}
		return null;
	}
}
