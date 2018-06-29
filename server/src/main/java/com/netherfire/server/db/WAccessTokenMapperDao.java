package com.netherfire.server.db;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.netherfire.server.db.entity.WAccessToken;
import com.netherfire.server.db.factory.DataProvider;
import com.netherfire.server.db.mapper.WAccessTokenMapper;

public class WAccessTokenMapperDao {
	private static final Logger logger = LogManager.getLogger(WAccessTokenMapperDao.class);
	
	
	
	/**
	 * 根据参数查询
	 * 
	 * @param list
	 * @return
	 */
	public static List<WAccessToken> queryList(WAccessToken params) {
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {
			WAccessTokenMapper mapper = sqlSession.getMapper(WAccessTokenMapper.class);
			List<WAccessToken> userIdList = mapper.queryList(params);

			return userIdList;
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			sqlSession.close();
		}
		return null;

	}
	/**
	 * 更新数据
	 * @param entity
	 * @return
	 */
	public static int update(WAccessToken entity){
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {
			WAccessTokenMapper mapper = sqlSession.getMapper(WAccessTokenMapper.class);
			int status = mapper.update(entity);
			sqlSession.commit();
			return status;
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			sqlSession.close();
		}
		return 0;
		
	}
	public static int insert(WAccessToken entity){
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {
			WAccessTokenMapper mapper = sqlSession.getMapper(WAccessTokenMapper.class);
			int status = mapper.insert(entity);
			sqlSession.commit();
			return status;
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			sqlSession.close();
		}
		return 0;
		
	}
}
