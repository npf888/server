package com.netherfire.server.db;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.netherfire.server.db.factory.DataProvider;
import com.netherfire.server.db.mapper.ClientResourceVersionMapper;

public class ClientResourceVersionDaoServer {
	private static final Logger logger = LogManager.getLogger(ClientResourceVersionDaoServer.class);
	/**
	 * 获取数据库资源版本号
	 * @return
	 */
	public static int getResourceVersion(){
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		int maxVersion = 0;
		try{
			 ClientResourceVersionMapper mapper = sqlSession.getMapper(ClientResourceVersionMapper.class);
			 maxVersion = mapper.getMaxVersion();
		}catch(Exception e){
			logger.error("", e);
		}finally{
			sqlSession.close();
		}
		 return maxVersion;
	}
}
