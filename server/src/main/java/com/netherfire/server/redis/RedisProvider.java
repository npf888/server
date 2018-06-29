package com.netherfire.server.redis;

import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.netherfire.server.handler.imple.IdentifyingCodeLoginHandler;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisProvider {

private static final RedisProvider redisProvider = new RedisProvider();
	public static Logger log = LogManager.getLogger(RedisProvider.class);
	private String resource = "jedis";
	
	private JedisPool jedisPool;
	
	private RedisProvider(){}
	
	public static RedisProvider getInstance(){
		return redisProvider;
	}
	
	public void init(){
		
		ResourceBundle bundle = ResourceBundle.getBundle(resource);
		
		JedisPoolConfig jedisPoolCfg = new JedisPoolConfig();
		
		jedisPoolCfg.setMaxTotal(Integer.valueOf(bundle.getString("redis.pool.maxActive")));
		jedisPoolCfg.setMaxIdle(Integer.valueOf(bundle.getString("redis.pool.maxIdle")));
		jedisPoolCfg.setMaxWaitMillis(Integer.valueOf(bundle.getString("redis.pool.maxWait")));
		jedisPoolCfg.setTestOnBorrow(Boolean.valueOf(bundle.getString("redis.pool.testOnBorrow")));
		jedisPoolCfg.setTestOnReturn(Boolean.valueOf(bundle.getString("redis.pool.testOnReturn")));
		log.info("当前绑定的 redis ip："+bundle.getString("redis.ip")+" --- 端口号："+bundle.getString("redis.port"));
		jedisPool = new JedisPool(jedisPoolCfg,bundle.getString("redis.ip"),Integer.valueOf(bundle.getString("redis.port")));
		
		//jedisPool = new JedisPool(jedisPoolCfg,bundle.getString("redis.ip"),Integer.valueOf(bundle.getString("redis.port")),2000,bundle.getString("redis.password"));
	}
	
	public JedisPool getJedisPool() {
		return jedisPool;
	}
	
}
