package com.netherfire.server.db;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.netherfire.server.config.Config;
import com.netherfire.server.db.entity.ClientVersion;
import com.netherfire.server.db.factory.DataProvider;
import com.netherfire.server.db.mapper.ClientVersionMapper;

public class ClientVersionDaoServer {
	private static final Logger logger = LogManager.getLogger(ClientVersionDaoServer.class);
	
	public static Map<String,String> getClientVersion(){
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		Map<String,String> map = new HashMap<String,String>();
		try{
			ClientVersionMapper mapper = sqlSession.getMapper(ClientVersionMapper.class);
			ClientVersion clientVersion = mapper.getClientVersion();
			map.put(Config.clientMinVersionKey, clientVersion.getMinVersion());
			map.put(Config.clientVersionKey, clientVersion.getVersion());
		}catch(Exception e){
			logger.error("", e);
		}finally{
			sqlSession.close();
		}
		return map;
	}
}
