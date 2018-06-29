package com.netherfire.server.db;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.netherfire.server.db.entity.IndexNotice;
import com.netherfire.server.db.factory.DataProvider;
import com.netherfire.server.db.mapper.IndexNoticeMapper;

public class IndexNoticeDaoServer {
	private static final Logger logger = LogManager.getLogger(IndexNoticeDaoServer.class);
	
	public static IndexNotice getIndexNotice(){
		 SqlSession sqlSession = DataProvider.getInstance().getSession();
		try{
			 IndexNoticeMapper mapper = sqlSession.getMapper(IndexNoticeMapper.class);
			 IndexNotice indexNotice = mapper.getIndexNotice();
			 return indexNotice;
		}catch(Exception e){
			logger.error("", e);
		}finally{
			sqlSession.close();
		}
		return null;
	}
}
