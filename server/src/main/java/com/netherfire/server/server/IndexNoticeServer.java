package com.netherfire.server.server;

import com.alibaba.fastjson.JSON;
import com.netherfire.server.config.Config;
import com.netherfire.server.db.IndexNoticeDaoServer;
import com.netherfire.server.db.entity.IndexNotice;
import com.netherfire.server.handler.result.IndexNoticeReturn;
import com.netherfire.server.redis.RedisProvider;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class IndexNoticeServer {

	public static IndexNoticeReturn getVO(){
		IndexNoticeReturn vo = new IndexNoticeReturn();
		JedisPool jedisPool = RedisProvider.getInstance().getJedisPool();
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			
			String index_notice = jedis.get(Config.index_notice);
			
			if(index_notice == null){
				IndexNotice indexNotice = IndexNoticeDaoServer.getIndexNotice();
				vo.setCn(indexNotice.getCn());
				vo.setEn(indexNotice.getEn());
				vo.setTw(indexNotice.getTw());
				vo.setOpen(indexNotice.getOpen());
				jedis.set(Config.index_notice, JSON.toJSONString(vo));
			}else{
				vo = JSON.parseObject(index_notice,IndexNoticeReturn.class);
			}			
		} finally {
			if(jedis!=null)
			{
				jedis.close();
			}
		}
		return vo;
	}
}
